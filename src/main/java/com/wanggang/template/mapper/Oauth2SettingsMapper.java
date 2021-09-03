package com.wanggang.template.mapper;

import com.wanggang.template.model.entity.Oauth2Settings;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * oauth持久化层demo
 *
 * @author wg
 * @version 1.0
 * @date 2020/1/20 16:44
 * @since 1.0
 */
@Mapper
public interface Oauth2SettingsMapper extends BaseMapper<Oauth2Settings> {
}
