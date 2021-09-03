package com.wanggang.template.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * demo
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 12:39
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO implements Serializable {
    /**
     * 创建时间
     */
    @Excel(name = "创建时间", format = "yyyy-MM-dd HH:mm:ss", orderNum = "1", width = 35)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;
    @ApiModelProperty(value = "用户id")
    private String id;
    /**
     * 用户名称
     */
    @Excel(name = "用户名称", orderNum = "1", width = 35)
    @NotEmpty(message = "用户名称不能为NULL")
    @ApiModelProperty(value = "用户名称")
    private String realName;

    @Override
    public int hashCode() {
        return this.realName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        UserVO t = (UserVO) obj;
        return t.getRealName().equals(this.getRealName());
    }
}
