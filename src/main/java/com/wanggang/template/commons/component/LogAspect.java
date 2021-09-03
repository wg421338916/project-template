package com.wanggang.template.commons.component;

import com.wanggang.template.commons.utils.IpUtils;
import com.wanggang.template.commons.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 王刚
 * @version 1.0
 * @description controller切面操作
 * @date 2019/12/27
 * @since 1.0.0
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    /**
     * 请求完返回
     *
     * @param joinPoint 切面对象
     * @param ret       返回结果
     */
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(JoinPoint joinPoint, Object ret) {
        String retData = getUrl(joinPoint) + ",返回结果:" + JacksonUtil.object2Json(ret);

        log.info(retData);
    }

    private String getUrl(JoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return Strings.EMPTY;
        }

        HttpServletRequest request = requestAttributes.getRequest();
        Signature signature = joinPoint.getSignature();

        StringBuilder builder = new StringBuilder();
        builder.append(IpUtils.getIp(request))
                .append(";")
                .append(request.getRequestURI())
                .append(";")
                .append(request.getMethod())
                .append(";")
                .append(request.getHeader("User-Agent"))
                .append(";")
                .append(signature.getDeclaringTypeName())
                .append(".")
                .append(signature.getName());

        return builder.toString();
    }

    /**
     * 请求完异常
     *
     * @param joinPoint 切面对象
     */
    @AfterThrowing(pointcut = "webLog()")
    public void doAfterThrowing(JoinPoint joinPoint) {
        String retData = getUrl(joinPoint);

        log.info(retData);
    }

    @Before(value = "webLog()")
    public void doBefore(JoinPoint joinPoint) {
        StringBuilder builder = new StringBuilder(getUrl(joinPoint));

        String[] params = ((CodeSignature) joinPoint.getStaticPart().getSignature()).getParameterNames();
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        int i = 0;
        builder.append(",参数:");
        for (Object arg : joinPoint.getArgs()) {
            if (arg == null || arg == attr.getRequest() || arg == attr.getResponse()) {
                //过滤掉参数为request和response的
                continue;
            }

            String argument = "";
            if (arg instanceof MultipartFile) {
                argument = "文件";
            } else {
                argument = JacksonUtil.object2Json(arg);
            }

            builder.append("&")
                    .append(params[i])
                    .append("=")
                    .append(argument);

            i += 1;
        }

        log.info(builder.toString());
    }

    /**
     * 定义一个切入点.
     * 解释下：
     * <p>
     * ~ 第一个 * 代表任意修饰符及任意返回值.
     * ~ 第二个 * 任意包名
     * ~ 第三个 * 定义在controller包或者子包
     * ~ 第四个 * 任意方法
     * ~ .. 匹配任意数量的参数.
     * </p>
     */
    @Pointcut(value = "execution(public * com.wanggang..*.controller..*.*(..))")
    public void webLog() {
        throw new UnsupportedOperationException();
    }
}
