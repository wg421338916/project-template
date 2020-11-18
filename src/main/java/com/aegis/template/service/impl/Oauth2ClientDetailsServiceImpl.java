package com.aegis.template.service.impl;

import com.aegis.template.mapper.Oauth2SettingsMapper;
import com.aegis.template.model.entity.Oauth2Settings;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

/**
 * client 用户认证服务
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 21:42
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
@Service
public class Oauth2ClientDetailsServiceImpl implements ClientDetailsService {
  private Oauth2SettingsMapper oauth2SettingsMapper;

  public Oauth2ClientDetailsServiceImpl(Oauth2SettingsMapper oauth2SettingsMapper) {
    this.oauth2SettingsMapper = oauth2SettingsMapper;
  }

  @Override
  public ClientDetails loadClientByClientId(String key) {
    if(StringUtils.isBlank(key)){
      throw new UsernameNotFoundException("没有找到对应的key");
    }

    Oauth2Settings oauth2Settings = oauth2SettingsMapper.selectOne(Wrappers.<Oauth2Settings>lambdaQuery().eq(Oauth2Settings::getAppKey, key));
    if (oauth2Settings == null) {
      throw new UsernameNotFoundException("没有找到对应的key");
    }

    BaseClientDetails baseClientDetails = new BaseClientDetails();
    baseClientDetails.setClientId(oauth2Settings.getAppKey());
    baseClientDetails.setClientSecret(oauth2Settings.getAppSecret());
    return baseClientDetails;
  }
}
