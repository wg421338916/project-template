package com.aegis.template.controller;

import cn.hutool.core.lang.Assert;
import com.aegis.template.commons.annotation.DecryptRequest;
import com.aegis.template.commons.annotation.EncryptResponse;
import com.aegis.template.commons.constants.CustomerCodeConstants;
import com.aegis.template.commons.domain.Result;
import com.aegis.template.commons.domain.ResultPager;
import com.aegis.template.commons.exception.BaseException;
import com.aegis.template.commons.utils.cache.ehcache.EhCacheUtils;
import com.aegis.template.model.vo.UserVO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

/**
 * demo
 *
 * @author wg
 * @version 1.0
 * @date 2020/5/8 13:01
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
@Slf4j
@RestController
public class NoAuthController {

  @GetMapping("/args")
  public void args() {

  }

  @EncryptResponse
  @GetMapping("/demo")
  public UserVO demo() throws IOException {
    UserVO vo = new UserVO();
    vo.setRealName("realname");
    vo.setCreateTime(LocalDateTime.now());
    vo.setId("111");
    ClassPathResource classPathResource = new ClassPathResource("Config/demo.txt");

    InputStream inputStream = classPathResource.getInputStream();

    BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

    String s = bf.readLine();
    log.info(s);
    return vo;
  }

  /**
   * 重启获取数据，看是否会丢失
   *
   * @return
   */
  @GetMapping("/get")
  public String get() {
    return EhCacheUtils.get("demo", "demo150", String.class);
  }

  @GetMapping("/int")
  public Integer intDemo() {
    return 1;
  }

  @GetMapping("/res")
  public Result<Serializable> res() {
    return Result.success(null);
  }

  @GetMapping("/throw")
  public void throwDemo() {
    throw BaseException.create(CustomerCodeConstants.C800);
  }

  @GetMapping("/throw_args_error")
  public void throwArgsError() {
    Assert.notNull(null, "对象不能为null");
  }

  @GetMapping("/void")
  public void voidDemo() {
    put();
  }

  @GetMapping("/put")
  public String put() {
    IntStream.range(0, 200).forEach(i -> EhCacheUtils.putIfAbsent("demo", "demo" + i, i + ""));

    return "ok";
  }

  @EncryptResponse
  @GetMapping("/string1")
  public String getString1() {
    return "aaaaaaaaaa";
  }

  @GetMapping("/string2")
  public String getString2() {
    return "aaaaaaaaaa";
  }

  @EncryptResponse
  @GetMapping("/page")
  public ResultPager<String> demo3() {
    return ResultPager.success(1, 10, 100, Lists.newArrayList());
  }

  @EncryptResponse
  @GetMapping("/no_page")
  public Result<String> demo2() {
    return Result.success("成功");
  }

  @DecryptRequest
  @PostMapping("/demo4")
  public String demo4(@RequestBody String body) {
    return body;
  }
}
