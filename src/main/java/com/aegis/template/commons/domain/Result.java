package com.aegis.template.commons.domain;


import com.aegis.template.commons.utils.ResponsePropsUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 不带分页的返回值
 * <p>
 * 规范文档：https://confluence.aegis-info.com/x/G4AT
 * 非 分页形式：
 * {
 * "code": 0,
 * "data": {}|[],
 * "msg": ""
 * }
 * @author: wg
 * @create: 2019/3/23
 */
@Data
@ApiModel("返回结果")
public class Result<T> {
  @ApiModelProperty(value = "接口返回状态", example = "0")
  protected Integer code;
  @ApiModelProperty(value = "接口返回数据")
  protected T data;
  @ApiModelProperty(value = "接口返回信息", example = "ok")
  protected String msg;

  public Result(T data) {
    this.data = data;
    this.code = 0;
    this.msg = "ok";
  }

  public Result(T data, Integer code, String msg) {
    this.data = data;
    this.code = code;
    this.msg = msg;
  }

  public Result() {
  }

  public Result(Integer code) {
    this(null, code);
  }

  public Result(T data, Integer code) {
    this.data = data;
    this.code = code;
    this.msg = ResponsePropsUtils.getByKey(String.valueOf(code), "未知CODE码值");
  }

  public static <T> Result<T> result(T data, Integer code) {
    return new Result<>(data, code);
  }

  public static <T> Result<T> result(Integer code) {
    return new Result<>(null, code);
  }

  public static <T> Result<T> result(Integer code, String msg) {
    return new Result<>(null, code, msg);
  }

  public static <T> Result<T> success(T data) {
    return new Result<>(data);
  }
}