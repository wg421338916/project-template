package com.wanggang.template.commons.interceptor.handler;

import com.wanggang.template.commons.constants.CustomerCodeConstants;
import com.wanggang.template.commons.domain.Result;
import com.wanggang.template.commons.utils.JacksonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 没有登录
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 8:35
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
public class CustomerAuthNoLoginHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse response, AuthenticationException e) throws IOException {
        Result<Serializable> res = Result.result(CustomerCodeConstants.C808);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JacksonUtil.object2Json(res));
    }
}
