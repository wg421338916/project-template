package wanggang;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Administrator
 */
public interface IUserApi {
  /**
   * 获取用户信息
   *
   * @param id
   * @return 用户信息
   */
  @GetMapping("users/{id}")
  String getUser(@PathVariable("id") String id);
}
