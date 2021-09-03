package com.wanggang.template.commons.constants;

import com.wanggang.template.commons.config.GlobalConfig;
import lombok.Getter;

import java.io.Serializable;

/**
 * redis key 常量
 * <p>
 * key 规范： 项目名称：模块名称：key值，如;obpc:coco:user:1234556，其中项目名称从配置中读取
 * <p>
 * 在写key的时候需要注意该key是否已经被别人使用,redis key常量默认都写到该类中
 *
 * @author wg
 * @version 1.0
 * @date 2020/4/14 18:12
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
public class RedisKeyConstants implements Serializable {
    public static final RedisKeyConstants DEMO1 = new RedisKeyConstants("moduleName:keyName1");
    public static final RedisKeyConstants DEMO2 = new RedisKeyConstants("moduleName", "keyName2");

    private static final long serialVersionUID = 1L;
    @Getter
    private String key;

    private RedisKeyConstants() {
    }

    private RedisKeyConstants(String... keys) {
        this.key = String.format("%s:%s", GlobalConfig.getName(), String.join(":", keys));
    }

    public static RedisKeyConstants get(String key) {
        return new RedisKeyConstants(key);
    }

    public static RedisKeyConstants get(RedisKeyConstants parentKey, String key) {
        RedisKeyConstants redisKeyConstants = new RedisKeyConstants();
        redisKeyConstants.key = String.format("%s:%s", parentKey.getKey(), key);
        return redisKeyConstants;
    }

    @Override
    public String toString() {
        return this.key;
    }
}
