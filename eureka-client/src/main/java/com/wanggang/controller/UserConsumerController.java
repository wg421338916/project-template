package com.wanggang.controller;

import com.wanggang.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 */
@RestController
public class UserConsumerController {

  @Autowired
  UserService userApi;

  @GetMapping("find")
  public String getUser() throws InterruptedException {
    System.out.println("开始请求");
    String test = userApi.getUser("test");
    System.out.println("请求完成");
    return test;
  }
}
