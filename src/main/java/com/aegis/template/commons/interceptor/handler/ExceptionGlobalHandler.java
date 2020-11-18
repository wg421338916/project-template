package com.aegis.template.commons.interceptor.handler;

import cn.hutool.core.util.StrUtil;
import com.aegis.template.commons.constants.CustomerCodeConstants;
import com.aegis.template.commons.constants.SystemCodeConstants;
import com.aegis.template.commons.domain.Result;
import com.aegis.template.commons.exception.BaseException;
import com.aegis.template.commons.utils.ResponsePropsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

/**
 * 全局异常处理
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 12:39
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
@RestController
public class ExceptionGlobalHandler implements ErrorController {

  @Override
  public String getErrorPath() {
    return "/error";
  }

  /**
   * 捕获404
   *
   * @return
   */
  @GetMapping("/error")
  public Result<Serializable> handleError() {
    return Result.result(SystemCodeConstants.C404);
  }

  @ExceptionHandler(Exception.class)
  protected Result<Serializable> handleException(HttpServletRequest request, Exception ex) {
    //业务异常
    if (ex instanceof BaseException) {
      BaseException bs = (BaseException) ex;

      //业务异常不打印堆栈信息
      log.warn(bs.getBusinessDes() + "-" + ex.getMessage());

      return bs.getResponse();
    }

    //参数异常
    if (ex instanceof MethodArgumentNotValidException) {
      StringBuilder sb = new StringBuilder();

      BindingResult bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
      if (bindingResult.hasErrors()) {
        List<ObjectError> errorList = bindingResult.getAllErrors();
        errorList.forEach(error -> {
          FieldError fieldError = (FieldError) error;
          sb.append(fieldError.getField() + ":" + fieldError.getDefaultMessage() + ";");
        });
      }

      log.warn(sb.toString(), ex);
      return Result.result(SystemCodeConstants.C400, sb.toString());
    }

    if (ex instanceof IllegalArgumentException) {
      IllegalArgumentException illegalArgumentException = (IllegalArgumentException) ex;
      log.warn("参数异常:{}", ex.getMessage());

      if (StrUtil.isNotBlank(illegalArgumentException.getMessage())) {
        return Result.result(SystemCodeConstants.C400, illegalArgumentException.getMessage());
      }

      return Result.result(SystemCodeConstants.C400, ResponsePropsUtils.getByKey(SystemCodeConstants.C400.toString(), illegalArgumentException.getMessage()));
    }

    if (ex instanceof AccessDeniedException) {
      log.warn("权限异常", ex);
      return Result.result(CustomerCodeConstants.C809, ResponsePropsUtils.getByKey(CustomerCodeConstants.C809.toString()));
    }

    if (ex instanceof HttpRequestMethodNotSupportedException) {
      log.warn("Request method 异常", ex);
      return Result.result(SystemCodeConstants.C404, ResponsePropsUtils.getByKey(SystemCodeConstants.C404.toString()));
    }

    //其他异常
    String desc = String.format("系统异常,地址:%s", request.getRequestURI());
    log.error(desc, ex);
    return Result.result(SystemCodeConstants.C500, ResponsePropsUtils.getByKey(SystemCodeConstants.C500.toString(), ex.getMessage()));
  }
}
