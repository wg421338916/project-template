package com.aegis.template.commons.utils.retry.annotation;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import com.aegis.template.commons.utils.retry.*;
import io.micrometer.core.lang.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.*;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 异步重试队列基于注解配置
 * <p>
 *
 * @author 王刚
 * @version 1.0
 * @date 2019/12/27
 * @since 1.0.0
 */
@Slf4j
public class RetrySyncConfiguration extends AbstractPointcutAdvisor
    implements IntroductionAdvisor, InitializingBean, InstantiationAwareBeanPostProcessor {

  private final transient ConcurrentMap<String, RetrySyncInfo> queues = new ConcurrentHashMap<>();
  private transient Advice advice;
  private ThreadLocal<Boolean> canRun = new InheritableThreadLocal<>();
  private transient Pointcut pointcut;

  private static class AnnotationMethodsResolver {

    private Class<? extends Annotation> annotationType;

    public AnnotationMethodsResolver(Class<? extends Annotation> annotationType) {
      this.annotationType = annotationType;
    }

    public boolean hasAnnotatedMethods(Class<?> clazz) {
      final AtomicBoolean found = new AtomicBoolean(false);
      ReflectionUtils.doWithMethods(clazz, method -> {
            if (found.get()) {
              return;
            }
            Annotation annotation = AnnotationUtils.findAnnotation(method, annotationType);
            if (annotation != null) {
              found.set(true);
            }
          }
      );
      return found.get();
    }
  }

  private final class AnnotationAwareRetryOperationsInterceptor implements IntroductionInterceptor {
    @Override
    public boolean implementsInterface(Class<?> intf) {
      return RetrySyncQueue.class.isAssignableFrom(intf);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
      RetrySyncQueue annotation = AnnotationUtil.getAnnotation(invocation.getMethod(), RetrySyncQueue.class);
      if (annotation == null || queues.get(annotation.name().toUpperCase()) == null) {
        return invocation.proceed();
      }

      Boolean aBoolean = canRun.get();
      if (aBoolean != null && aBoolean) {
        Object proceed = invocation.proceed();
        canRun.set(false);
        return proceed;
      }

      return queues.get(annotation.name().toUpperCase()).getQueue().put(invocation.getArguments()[0], annotation.delayMillis(), annotation.canRepeat());
    }
  }

  private final class AnnotationClassOrMethodFilter extends AnnotationClassFilter {

    private final AnnotationMethodsResolver methodResolver;

    AnnotationClassOrMethodFilter(Class<? extends Annotation> annotationType) {
      super(annotationType, true);
      this.methodResolver = new AnnotationMethodsResolver(annotationType);
    }

    @Override
    public boolean matches(Class<?> clazz) {
      return super.matches(clazz) || this.methodResolver.hasAnnotatedMethods(clazz);
    }
  }

  private final class AnnotationClassOrMethodPointcut extends StaticMethodMatcherPointcut {

    private final MethodMatcher methodResolver;

    AnnotationClassOrMethodPointcut(Class<? extends Annotation> annotationType) {
      this.methodResolver = new AnnotationMethodMatcher(annotationType);
      setClassFilter(new AnnotationClassOrMethodFilter(annotationType));
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
      return super.getClassFilter().matches(targetClass) || this.methodResolver.matches(method, targetClass);
    }

    @Override
    public boolean equals(Object other) {
      if (this == other) {
        return true;
      }
      if (!(other instanceof AnnotationClassOrMethodPointcut)) {
        return false;
      }
      AnnotationClassOrMethodPointcut otherAdvisor = (AnnotationClassOrMethodPointcut) other;
      return ObjectUtils.nullSafeEquals(this.methodResolver, otherAdvisor.methodResolver);
    }
  }

  @Override
  @Nullable
  public Object postProcessAfterInitialization(Object bean, String beanName) {
    RetrySyncListener annotation = AnnotationUtil.getAnnotation(bean.getClass(), RetrySyncListener.class);
    if (annotation == null) {
      annotation = AnnotationUtil.getAnnotation(bean.getClass().getSuperclass(), RetrySyncListener.class);
    }

    if (annotation == null) {
      return bean;
    }

    Method[] methods = ReflectUtil.getMethods(bean.getClass());
    Arrays.stream(methods).forEach(method -> {
      try {
        RetrySyncQueue syncQueue = AnnotationUtil.getAnnotation(method, RetrySyncQueue.class);
        if (syncQueue == null) {
          return;
        }

        Assert.notNull(syncQueue.name(), "队列名称不能为NULL");

        Parameter[] parameters = method.getParameters();
        if (parameters.length != 1) {
          throw new Exception("使用@RetrySyncQueue的方法，参数必须为1个");
        }
        Class<?> returnType = method.getReturnType();
        if (Boolean.class != returnType && boolean.class != returnType) {
          throw new Exception("使用@RetrySyncQueue的方法，返回值必须为Boolean");
        }

        RetrySyncPolicy policy = syncQueue.policy();
        RetryPolicy complexPolicy = new ComplexPolicy(policy.maxTryCount(), TimeUnit.SECONDS.toMillis(policy.initialIntervalSec()), TimeUnit.SECONDS.toMillis(policy.maxIntervalSec()), policy.multiplier());

        DispatcherQueue<Object> exponentialMemoryQueue;
        if (syncQueue.isPersistent()) {
          exponentialMemoryQueue = RetryUtils.getPersistenceQueue(syncQueue.name().toUpperCase(), complexPolicy);
        } else {
          exponentialMemoryQueue = RetryUtils.getMemoryQueue(syncQueue.name().toUpperCase(), complexPolicy);
        }

        RetrySyncInfo retrySyncInfo = new RetrySyncInfo();
        retrySyncInfo.setBean(bean);
        retrySyncInfo.setMethod(method);
        retrySyncInfo.setQueue(exponentialMemoryQueue);

        queues.put(syncQueue.name().toUpperCase(), retrySyncInfo);
        exponentialMemoryQueue.register(this::handler);
      } catch (Exception ex) {
        throw new RuntimeException("【失败重试组件】初始化队列失败,msg:" + ex.getMessage());
      }
    });

    return bean;
  }

  private boolean handler(RetryWrapperMessage<Object> obj) {
    RetrySyncInfo retrySyncInfo = queues.get(obj.getQueueName().toUpperCase());
    try {
      canRun.set(true);
      return (Boolean) retrySyncInfo.getMethod().invoke(retrySyncInfo.getBean(), obj.getPayload());
    } catch (Exception ex) {
      log.error("【失败重试组件】执行目标方法", ex);
      return false;
    }
  }

  @Override
  public void afterPropertiesSet() {
    Set<Class<? extends Annotation>> retryableAnnotationTypes = new LinkedHashSet<>(1);
    retryableAnnotationTypes.add(RetrySyncQueue.class);
    this.pointcut = buildPointcut(retryableAnnotationTypes);
    this.advice = buildAdvice();
  }

  protected Pointcut buildPointcut(Set<Class<? extends Annotation>> retryAnnotationTypes) {
    ComposablePointcut result = null;
    for (Class<? extends Annotation> retryAnnotationType : retryAnnotationTypes) {
      Pointcut filter = new AnnotationClassOrMethodPointcut(retryAnnotationType);
      if (result == null) {
        result = new ComposablePointcut(filter);
      } else {
        result.union(filter);
      }
    }
    return result;
  }

  protected Advice buildAdvice() {
    return new AnnotationAwareRetryOperationsInterceptor();
  }

  @Override
  public ClassFilter getClassFilter() {
    return pointcut.getClassFilter();
  }

  @Override
  public void validateInterfaces() {
  }

  @Override
  public Class<?>[] getInterfaces() {
    return new Class[]{RetrySyncQueue.class};
  }

  @Override
  public Advice getAdvice() {
    return this.advice;
  }

  @Override
  public Pointcut getPointcut() {
    return this.pointcut;
  }
}
