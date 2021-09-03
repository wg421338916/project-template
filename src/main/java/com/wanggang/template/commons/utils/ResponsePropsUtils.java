package com.wanggang.template.commons.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.setting.dialect.Props;

/**
 * 获取 输出编码对应的描述
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 12:39
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
public final class ResponsePropsUtils {
    private static final Props PROPS;

    static {
        PROPS = new Props("properties/response.properties", CharsetUtil.CHARSET_UTF_8);
    }

    private ResponsePropsUtils() {
    }

    /**
     * 输出编码对应的描述
     *
     * @param key        key值
     * @param defaultVal 如果不存在，则返回defaultVal
     * @return 结果
     */
    public static String getByKey(String key, String defaultVal) {
        return PROPS.getProperty(key, defaultVal);
    }

    /**
     * 输出编码对应的描述
     *
     * @param key
     * @return 结果
     */
    public static String getByKey(String key) {
        return PROPS.getProperty(key);
    }
}
