package com.aegis.template.commons.annotation;

import java.lang.annotation.*;

/**
 * 对返回值data部分进行加密，controller切面操作
 * 作用在web controller上面，如果有data有返回值，则进行对称加密
 * <p>
 * 对称加密介绍：https://blog.csdn.net/gulang03/article/details/81175854
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
public @interface EncryptResponse {
  boolean value() default true;
}
