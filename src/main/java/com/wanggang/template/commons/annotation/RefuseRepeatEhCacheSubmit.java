package com.wanggang.template.commons.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 拒绝同一用户重复提交数据，controller切面操作
 * 作用在web controller上面，如果不允许重复提交返回前端702码值
 * 具体业务实现：RefuseRepeatSubmitEhCacheAspect
 *
 * @author 王刚
 * @version 1.0
 * @description controller切面操作
 * @date 2019/12/27
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RefuseRepeatEhCacheSubmit {
    /**
     * 锁前缀，防止不通的方法，重复锁相同的key值
     *
     * @return
     */
    String prefix() default "";

    /**
     * 锁超时时间毫秒,默认0
     *
     * @return
     */
    int time() default 0;
}
