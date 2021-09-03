package com.wanggang.template.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.http.HttpUtil;
import com.wanggang.template.commons.constants.CustomerCodeConstants;
import com.wanggang.template.commons.exception.BaseException;
import com.wanggang.template.commons.utils.JacksonUtil;
import com.wanggang.template.commons.utils.retry.DispatcherQueue;
import com.wanggang.template.mapper.UserMapper;
import com.wanggang.template.model.bo.MyBO;
import com.wanggang.template.model.dto.MyDTO;
import com.wanggang.template.model.entity.User;
import com.wanggang.template.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * 用户实现服务
 *
 * @author wg
 * @version 1.0
 * @date 2020/2/10 19:58
 * @since 1.0.0
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String TEST = "test";
    private InnerUserService innerUserService;
    private UserMapper userMapper;
    private final DispatcherQueue<User> queue = null;

    public UserServiceImpl(UserMapper userMapper) throws IOException {
        this.userMapper = userMapper;
        this.innerUserService = new InnerUserService(userMapper);

//    queue = RetryUtils.getExponentialPersistenceQueue(User.class, "test");
//    queue.register(user -> {
//      log.info("test");
//      this.userMapper.insert(user);
//      return true;
//    });
    }

    @Override
    public boolean batchSave(List<User> users) {
        return this.innerUserService.saveBatch(users);
    }

    @Override
    public User findOneById(String id) {
        Assert.notNull(id, "id不能为NULL");

        return this.userMapper.selectById(id);
    }

    @Override
    public User findOneByUserAge(Integer age) {
        Assert.notNull(age, "age不能为NULL");

        return this.innerUserService.getOne(Wrappers.<User>lambdaQuery().eq(User::getAge, age), false);
    }

    @Override
    public Boolean save(User user) {
        Assert.notNull(user, "用户不能为NULL");

        if (TEST.equalsIgnoreCase(user.getRealName())) {
            throw BaseException.create(CustomerCodeConstants.C700);
        }

        queue.put(user);

        return true;
    }

    @Override
    public MyBO getRemote(MyBO bo) {
        MyBO res = new MyBO();

        String xxx = HttpUtil.get("xxx?name=" + bo.getName());
        if (StringUtils.isNotBlank(xxx)) {

            // dto 是和其他系统交互的对象
            MyDTO myDto = JacksonUtil.json2Object(xxx, MyDTO.class);

            BeanUtils.copyProperties(myDto, res);
        }

        return res;
    }


    private class InnerUserService extends ServiceImpl<UserMapper, User> {
        public InnerUserService(UserMapper userMapper) {
            super.baseMapper = userMapper;
        }
    }
}
