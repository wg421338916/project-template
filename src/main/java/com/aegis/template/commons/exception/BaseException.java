package com.aegis.template.commons.exception;

import com.aegis.template.commons.utils.ResponsePropsUtils;
import lombok.Getter;

import java.text.MessageFormat;

/**
 * 业务异常,不需要捕获
 *
 * @author wg
 * @version 1.0.0
 * @date 2020/2/14 2:36 下午
 * @since 1.0.0
 */
public class BaseException extends RuntimeException implements ResponseException {
  @Getter
  protected final Integer code;

  public BaseException(Integer code) {
    super();
    this.code = code;
  }

  public static BaseException create(Integer code) {
    return new BaseException(code);
  }

  public String getBusinessDes() {
    return MessageFormat.format("错误码:{0},错误详情:{1}", code, ResponsePropsUtils.getByKey(code.toString(), "未知异常"));
  }
}
