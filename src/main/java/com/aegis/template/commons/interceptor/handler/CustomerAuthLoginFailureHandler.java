package com.aegis.template.commons.interceptor.handler;

import com.aegis.template.commons.constants.CustomerCodeConstants;
import com.aegis.template.commons.domain.Result;
import com.aegis.template.commons.exception.NotSupportedLoginTypeException;
import com.aegis.template.commons.exception.VerificationCodeErrorException;
import com.aegis.template.commons.utils.JacksonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
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
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
public class CustomerAuthLoginFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    Result<Serializable> res = null;

    Integer code = CustomerCodeConstants.C802;
    if (exception instanceof BadCredentialsException ||
        exception instanceof UsernameNotFoundException) {
      code = CustomerCodeConstants.C803;
    } else if (exception instanceof LockedException) {
      code = CustomerCodeConstants.C804;
    } else if (exception instanceof CredentialsExpiredException) {
      code = CustomerCodeConstants.C805;
    } else if (exception instanceof AccountExpiredException) {
      code = CustomerCodeConstants.C806;
    } else if (exception instanceof DisabledException) {
      code = CustomerCodeConstants.C807;
    } else if (exception instanceof NotSupportedLoginTypeException) {
      NotSupportedLoginTypeException ex = (NotSupportedLoginTypeException) exception;
      res = ex.getResponse();
    } else if (exception instanceof VerificationCodeErrorException) {
      VerificationCodeErrorException ex = (VerificationCodeErrorException) exception;
      res = ex.getResponse();
    }

    if (res == null) {
      res = Result.result(code);
    }

    response.setStatus(HttpStatus.OK.value());
    response.setContentType("application/json;charset=utf-8");
    response.getWriter().write(JacksonUtil.object2Json(res));
  }
}
