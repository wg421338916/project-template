package com.aegis.template.controller;

import com.aegis.template.commons.utils.IpUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 基础controller BaseController
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 12:39
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
public class BaseController {
  public String getIp(HttpServletRequest request) {
    return IpUtils.getIp(request);
  }
}
