package com.aegis.template.commons.component;

import cn.hutool.core.util.StrUtil;
import com.aegis.template.commons.annotation.RefuseRepeatEhCacheSubmit;
import com.aegis.template.commons.constants.CustomerCodeConstants;
import com.aegis.template.commons.exception.BaseException;
import com.aegis.template.commons.utils.cache.CacheLock;
import com.aegis.template.commons.utils.cache.ehcache.EhCacheUtils;
import com.aegis.template.model.bo.UserDetailsBO;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.concurrent.TimeUnit;

/**
 * 拒绝重复提交数据，controller切面操作
 *
 * @author wg
 * @version 1.0
 * @date 2020/5/12 16:47
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
@Configuration
@Aspect
@Slf4j
public class RefuseRepeatSubmitEhCacheAspect {
  private static final String LOCK_CACHE = "refuseRepeatSubmitLock";

  @Around("pointCut() && @annotation(refuseRepeatSubmit)")
  public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint, RefuseRepeatEhCacheSubmit refuseRepeatSubmit) throws Throwable {
    String key = getUserId();

    if (StringUtils.isBlank(key) || refuseRepeatSubmit == null) {
      log.warn("检测到key为空，不进行重复提交判断");
      return proceedingJoinPoint.proceed();
    }

    key = refuseRepeatSubmit.prefix() + key;
    try (CacheLock lock = EhCacheUtils.getLock(LOCK_CACHE, key)) {
      boolean locked = lock.tryLock(refuseRepeatSubmit.time(), TimeUnit.SECONDS);
      if (!locked) {
        throw BaseException.create(CustomerCodeConstants.C702);
      }
      return proceedingJoinPoint.proceed();
    }
  }

  private String getUserId() {
    SecurityContext context = SecurityContextHolder.getContext();
    if (context == null) {
      return StrUtil.EMPTY;
    }

    Authentication authentication = context.getAuthentication();
    if (authentication == null) {
      return StrUtil.EMPTY;
    }

    Object principal = authentication.getPrincipal();
    if (!(principal instanceof UserDetailsBO)) {
      return StrUtil.EMPTY;
    }

    UserDetailsBO bo = (UserDetailsBO) principal;

    return bo.getId();
  }

  @Pointcut(value = "@annotation(com.aegis.template.commons.annotation.RefuseRepeatEhCacheSubmit)")
  public void pointCut() {
    throw new UnsupportedOperationException();
  }
}
