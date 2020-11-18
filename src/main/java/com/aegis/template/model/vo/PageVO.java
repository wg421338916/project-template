package com.aegis.template.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * vo page实体
 *
 * @author wg
 * @version 1.0
 * @date 2020/2/18 11:50
 * @since 1.0.0
 */
@Data
public class PageVO<T> {

  @ApiModelProperty(value = "对象列表")
  private List<T> data;
  @ApiModelProperty(value = "总数量")
  private Long total;

}
