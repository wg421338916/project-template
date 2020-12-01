package wanggang.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import wanggang.IUserApi;

import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
@RestController
public class UserController implements IUserApi {

  @Value("${demo.sleep}")
  private Long sleep;
  @Value("${demo.whichOne}")
  private String whichOne;

  @Override
  @GetMapping("users/{id}")
  public String getUser(@PathVariable("id") String id) throws InterruptedException {
    System.out.println("接收到请求[/users/" + id + "]");

    TimeUnit.SECONDS.sleep(sleep);

    return "testUser" + whichOne;
  }

}