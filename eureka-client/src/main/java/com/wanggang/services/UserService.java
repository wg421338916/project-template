package com.wanggang.services;

import org.springframework.cloud.openfeign.FeignClient;
import wanggang.IUserApi;

/**
 * @author Administrator
 */
@FeignClient(name = "user-server")
public interface UserService extends IUserApi {
}
