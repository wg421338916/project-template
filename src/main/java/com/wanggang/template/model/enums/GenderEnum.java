package com.wanggang.template.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 性别
 * <p>
 * 参考:
 * https://blog.csdn.net/u011943534/article/details/105543879/
 *
 * @author wg
 * @version 1.0
 * @date 2020/2/12 11:43
 * @since 1.0.0
 */
public enum GenderEnum {
    /**
     * 未知
     */
    DEFAULT(0, "未知"),

    /**
     * 男
     */
    MAN(1, "男"),

    /**
     * 女
     */
    WOMAN(2, "女");

    @EnumValue
    private final int code;
    private final String descp;

    GenderEnum(int code, String descp) {
        this.code = code;
        this.descp = descp;
    }
}
