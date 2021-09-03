package com.wanggang.template.commons.interceptor.handler;

import com.wanggang.template.commons.domain.Result;
import com.wanggang.template.commons.utils.JacksonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 登录失败返回
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 8:35
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
public class CustomerAuthLogoutHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse response, Authentication authentication) throws IOException {
        Result<Serializable> res = Result.success("退出成功");

        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JacksonUtil.object2Json(res));
    }
}
