package com.aegis.template.commons.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ip 获取
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 12:39
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
@Slf4j
public class IpUtils {

  private static final int LENGTH = 15;

  private IpUtils() {
  }

  public static String getIp() {
    String address = "";
    try {
      address = InetAddress.getLocalHost().getHostAddress();
      return address;
    } catch (UnknownHostException e) {
      log.error("", e);
    }
    return address;
  }

  /**
   * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
   *
   * @param request
   * @return
   */
  public static String getIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (Boolean.FALSE.equals(checkIp(ip))) {
      return splitIp(ip);
    }

    if (Boolean.TRUE.equals(checkIp(ip))) {
      ip = request.getHeader("Proxy-Client-IP");
    }

    if (Boolean.TRUE.equals(checkIp(ip))) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }

    if (Boolean.TRUE.equals(checkIp(ip))) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }

    if (Boolean.TRUE.equals(checkIp(ip))) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }

    if (Boolean.TRUE.equals(checkIp(ip))) {
      ip = request.getRemoteAddr();
    }

    return ip;
  }

  private static Boolean checkIp(String ip) {
    return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
  }

  private static String splitIp(String someIps) {
    if (someIps == null) {
      return null;
    }

    if (someIps.length() > LENGTH) {
      String[] ips = someIps.split(",");
      for (int index = 0; index < ips.length; index++) {
        String strIp = ips[index];
        if (Boolean.FALSE.equals(checkIp(strIp))) {
          return strIp;
        }
      }
    }

    return someIps;
  }
}
