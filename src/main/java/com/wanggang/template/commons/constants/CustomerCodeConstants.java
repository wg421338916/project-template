package com.wanggang.template.commons.constants;

/**
 * 自定义返回码，码范围 700 - 9999
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 12:39
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
public final class CustomerCodeConstants extends SystemCodeConstants {


    public static final Integer C1234 = 1234;
    public static final Integer C700 = 700;
    public static final Integer C701 = 701;
    /**
     * 拒绝重复提交数据，请稍后在试
     */
    public static final Integer C702 = 702;

    /**
     * 验证码不正确
     */
    public static final Integer C800 = 800;
    /**
     * 不支持的登录类型
     */
    public static final Integer C801 = 801;
    /**
     * 登录失败
     */
    public static final Integer C802 = 802;

    /**
     * 账户名或者密码输入错误
     */
    public static final Integer C803 = 803;
    /**
     * 账户被锁定，请联系管理员
     */
    public static final Integer C804 = 804;
    /**
     * 密码过期，请联系管理员
     */
    public static final Integer C805 = 805;
    /**
     * 账户过期，请联系管理员
     */
    public static final Integer C806 = 806;
    /**
     * 账户被禁用，请联系管理员
     */
    public static final Integer C807 = 807;
    /**
     * 没有登录
     */
    public static final Integer C808 = 808;
    /**
     * 没有权限访问
     */
    public static final Integer C809 = 809;

    private CustomerCodeConstants() {
    }
}
