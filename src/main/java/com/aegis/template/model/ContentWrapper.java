package com.aegis.template.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.Data;

/**
 * 导出工具类
 *
 * @author wg
 * @version 1.0
 * @date 2020/06/09 2:37 下午
 * @since 1.0
 */
@Data
public class ContentWrapper {
  private String data;
  private Integer type;

}
