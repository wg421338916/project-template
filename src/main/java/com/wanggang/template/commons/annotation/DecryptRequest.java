package com.wanggang.template.commons.annotation;

import java.lang.annotation.*;

/**
 * 对传入controller的参数进行解密
 * 作用在web controller上面
 * <p>
 * 对称加密解密介绍：https://blog.csdn.net/gulang03/article/details/81175854
 *
 * @author 王刚
 * @version 1.0
 * @description controller切面操作
 * @date 2019/12/27
 * @since 1.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DecryptRequest {
    boolean value() default true;
}
