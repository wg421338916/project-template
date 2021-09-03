package com.wanggang.template.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * oauth 验证实体类
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/24 15:38
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "oauth_settings", autoResultMap = true)
public class Oauth2Settings extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 唯一标识，自增id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * appKey 验证key
     */
    private String appKey;

    /**
     * appKey 验证Secret
     */
    private String appSecret;

    /**
     * 描述
     */
    private String description;
}
