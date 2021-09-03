package com.wanggang.template.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * dto 可作为持久化层的附属类类型，只在service和dao层，不渗透到controller层
 *
 * @author wg
 * @version 1.0
 * @date 2020/2/12 13:28
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
public class UserDescriptionPO implements Serializable {
    private static final long serialVersionUID = 1905122041950251207L;
    private String desc;
    private String remark;
}

