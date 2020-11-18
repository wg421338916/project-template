package com.aegis.template.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aegis.template.model.bo.UserDetailsBO;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户认证服务
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 13:07
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  private static final String ADMIN = "admin";

  @Override
  public UserDetails loadUserByUsername(String s) {
    if (StringUtils.isBlank(s)) {
      throw new UsernameNotFoundException("没有找到对应的key");
    }

    List<GrantedAuthority> authorityList = Lists.newArrayList();
    String sth = StrUtil.EMPTY;
    if (ADMIN.equalsIgnoreCase(s)) {
      sth = new BCryptPasswordEncoder().encode("123456");
      authorityList.add(new SimpleGrantedAuthority("ADMIN"));
    }


    UserDetailsBO userDetailsBO = new UserDetailsBO(s, sth, authorityList);
    userDetailsBO.setId("1111111111111111111111");
    return userDetailsBO;
  }
}
