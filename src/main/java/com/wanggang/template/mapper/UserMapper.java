package com.wanggang.template.mapper;

import com.wanggang.template.model.entity.User;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户持久化层demo
 *
 * @author wg
 * @version 1.0
 * @date 2020/1/20 16:44
 * @since 1.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * demo
     *
     * @param wrapper
     * @return
     */
    @Select("select * from mp_user ${ew.customSqlSegment}")
    List<User> selectAll(@Param(Constants.WRAPPER) Wrapper<User> wrapper);

    /**
     * demo
     *
     * @param wrapper
     * @return
     */
    List<User> selectAll2(@Param(Constants.WRAPPER) Wrapper<User> wrapper);

    /**
     * demo
     *
     * @param page
     * @return
     */
    IPage<User> selectAll3(Page<User> page);
}
