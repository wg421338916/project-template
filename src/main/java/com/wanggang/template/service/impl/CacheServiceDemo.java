package com.wanggang.template.service.impl;

import com.wanggang.template.model.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * RedisCacheServiceDemo
 *
 * @author wg
 * @version 1.0
 * @date 2020/5/7 19:20
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
@Slf4j
@Service
public class CacheServiceDemo {
    @CacheEvict(value = "eh-sysCache", key = "#uid")
    public int delUserInfoByIdInEhCache(String uid) {
        log.info("CacheEvict delUserByIdInEhCache 有请求过来了");
        return 1;
    }

    @CacheEvict(value = "bjtemplate:user_details", key = "#uid")
    public int delUserInfoByIdInRedis(String uid) {
        log.info(" CacheEvict 有请求过来了");
        return 1;
    }

    @Cacheable(value = "eh-sysCache", key = "#uid", unless = "#result == null")
    public UserVO getUserDetailsByUidInEhCache(String uid) {
        log.info(" Cacheable getUserDetailsByUidInEhCache 有请求过来了");
        UserVO userVO = new UserVO();
        userVO.setRealName("test");
        userVO.setId(uid);
        return userVO;
    }

    @Cacheable(value = "bjtemplate:user_details", key = "#uid", unless = "#result == null")
    public UserVO getUserDetailsByUidInRedis(String uid) {
        log.info(" Cacheable 有请求过来了");
        UserVO userVO = new UserVO();
        userVO.setRealName("test");
        userVO.setId(uid);
        return userVO;
    }

    @CachePut(value = "eh-sysCache", key = "#user.id")
    public UserVO updateUserInfoInEhCache(UserVO user) {
        log.info(" CachePut updateUserInfoInEhCache 有请求过来了");
        user.setRealName("test2");
        return user;
    }

    @CachePut(value = "bjtemplate:user_details", key = "#user.id")
    public UserVO updateUserInfoInRedis(UserVO user) {
        log.info(" CachePut 有请求过来了");
        user.setRealName("test2");
        return user;
    }
}
