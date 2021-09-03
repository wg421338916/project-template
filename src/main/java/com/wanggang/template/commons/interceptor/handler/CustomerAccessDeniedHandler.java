package com.wanggang.template.commons.interceptor.handler;

import com.wanggang.template.commons.constants.CustomerCodeConstants;
import com.wanggang.template.commons.domain.Result;
import com.wanggang.template.commons.utils.JacksonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 没有权限
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 18:00
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
public class CustomerAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) throws IOException {
        Result<Serializable> res = Result.result(CustomerCodeConstants.C809);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JacksonUtil.object2Json(res));
    }
}
