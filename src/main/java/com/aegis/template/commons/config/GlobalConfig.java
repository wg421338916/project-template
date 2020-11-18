package com.aegis.template.commons.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 全局配置类
 *
 * @author 王刚
 * @version 1.0
 * @description 全局配置类
 * @date 2019/12/27
 * @since 1.0.0
 */
@Configuration
@ConfigurationProperties(prefix = "aegis")
public class GlobalConfig {
  /**
   * 项目名称
   */
  private static String name;
  /**
   * 上传路径
   */
  private static String profile = "/data";
  /**
   * 版本
   */
  private static String version;

  /**
   * 获取头像上传路径
   */
  public static String getAvatarPath() {
    return getProfile() + "/avatar";
  }

  public static String getProfile() {
    return profile.concat("/").concat(name);
  }

  public void setProfile(String profile) {
    setProfileStatic(profile);
  }

  private static void setProfileStatic(String profile) {
    GlobalConfig.profile = profile;

  }

  /**
   * 获取下载路径
   */
  public static String getDownloadPath() {
    return getProfile() + "/download/";
  }

  public static String getName() {
    return name;
  }

  public void setName(String name) {
    setNameStatic(name);
  }

  private static void setNameStatic(String name) {
    GlobalConfig.name = name;
  }

  /**
   * 获取上传路径
   */
  public static String getUploadPath() {
    return getProfile() + "/upload";
  }

  public static String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    setVersionStatic(version);
  }

  private static void setVersionStatic(String version) {
    GlobalConfig.version = version;
  }

  @Override
  public String toString() {
    return String.format("程序默认名称:%s,程序数据目录:%s",GlobalConfig.getName(),GlobalConfig.getProfile());
  }
}
