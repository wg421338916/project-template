package com.aegis.template.service;

import com.aegis.template.model.bo.MyBO;
import com.aegis.template.model.entity.User;

import java.util.List;

/**
 * 用户接口服务
 *
 * @author wg
 * @version 1.0
 * @date 2020/2/10 19:57
 * @since 1.0.0
 */
public interface UserService {
  /**
   * demo
   * @param users
   * @return
   */
  boolean batchSave(List<User> users);

  /**
   * demo
   * @param id
   * @return
   */
  User findOneById(String id);

  /**
   * demo
   * @param age
   * @return
   */
  User findOneByUserAge(Integer age);

  /**
   * demo
   * @param user
   * @return
   */
  Boolean save(User user);

  /**
   * demo
   * @param bo
   * @return
   */
  MyBO getRemote(MyBO bo);
}
