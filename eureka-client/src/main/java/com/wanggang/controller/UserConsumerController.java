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
    return userApi.getUser("test");
  }
}
