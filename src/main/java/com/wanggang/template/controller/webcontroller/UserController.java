package com.wanggang.template.controller.webcontroller;

import com.wanggang.template.commons.annotation.RefuseRepeatEhCacheSubmit;
import com.wanggang.template.commons.domain.Result;
import com.wanggang.template.commons.domain.ResultPager;
import com.wanggang.template.model.bo.MyBO;
import com.wanggang.template.model.bo.UserDetailsBO;
import com.wanggang.template.model.entity.User;
import com.wanggang.template.model.vo.UserVO;
import com.wanggang.template.service.UserService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * demo
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 12:39
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
@Validated
@RestController
@RequestMapping("web/v1/user")
@Api(tags = "用户管理")
public class UserController extends WebBaseController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation("添加用户")
    @PostMapping
    public Boolean addUser(@ApiParam("用户实体") @RequestBody @Valid UserVO userVo) {
        User user = new User();

        BeanUtils.copyProperties(userVo, user);

        return userService.save(user);
    }

    @ApiOperation("根据用户ID获取用户")
    @GetMapping("{id}")
    public UserVO findUserById(@ApiParam("用户ID") @PathVariable String id) {
        UserVO vo = new UserVO();

        User user = userService.findOneById(id);

        BeanUtils.copyProperties(user, vo);

        return vo;
    }

    /**
     * vo 只能在controller层，不可以出现在service层，做为controller的参数和返回值
     * bo 可以作为service的参数和返回值
     *
     * @return
     */
    @ApiOperation("vo,bo，dto对象使用教学")
    @GetMapping("/other")
    public UserVO other() {
        UserVO vo = new UserVO();

        MyBO remote = userService.getRemote(new MyBO());

        vo.setRealName(remote.getName());
        vo.setCreateTime(LocalDateTime.now());

        return vo;
    }

    @GetMapping("/refuse")
    @RefuseRepeatEhCacheSubmit(prefix = "refuse")
    public UserDetailsBO refuse() throws InterruptedException {

        TimeUnit.SECONDS.sleep(3);

        return getUser();
    }

    @GetMapping("/page")
    public ResultPager<String> demo() {
        return ResultPager.success(1, 10, 100, Lists.newArrayList());
    }

    @GetMapping("/no_page")
    public Result<String> demo2() {
        return Result.success("成功");
    }
}
