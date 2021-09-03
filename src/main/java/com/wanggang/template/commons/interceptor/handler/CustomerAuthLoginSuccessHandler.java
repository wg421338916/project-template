package com.wanggang.template.commons.interceptor.handler;

import com.wanggang.template.commons.domain.Result;
import com.wanggang.template.commons.utils.JacksonUtil;
import com.wanggang.template.model.bo.UserDetailsBO;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录成功返回
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 8:54
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
public class CustomerAuthLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object obj = authentication.getPrincipal();
        if (!(obj instanceof UserDetailsBO)) {
            response.sendRedirect("/");
            return;
        }

        UserDetailsBO userBo = (UserDetailsBO) obj;

        Result<UserDetailsBO> res = Result.success(userBo);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JacksonUtil.object2Json(res));
    }
}
