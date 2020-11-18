package com.aegis.template.model.entity;

import com.aegis.template.model.enums.GenderEnum;
import com.aegis.template.model.po.UserDescriptionPO;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * user 实体demo
 *
 * @author wg
 * @version 1.0
 * @date 2020/1/20 16:41
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "mp_user", autoResultMap = true)
public class User extends BaseEntity implements Serializable {
  /**
   * 年龄
   */
  private Integer age;
  /**
   * 描述
   */
  @TableField(typeHandler = FastjsonTypeHandler.class)
  private UserDescriptionPO description;
  /**
   * 邮箱
   */
  private String email;
  @TableField(exist = false)
  private String firstName;
  /**
   * 性别
   */
  private GenderEnum gender;
  /**
   * 唯一标识，自增id
   */
  @TableId(type = IdType.AUTO)
  private Long id;
  /**
   * 管理id
   */
  private Long managerId;
  /**
   * 用户名称
   */
  @TableField(value = "name", condition = SqlCondition.LIKE, updateStrategy = FieldStrategy.DEFAULT)
  private String realName;

  private transient String secondName;
  /**
   * 乐观锁版本字段
   */
  @Version
  private Integer version;

}
