package com.wanggang.template.controller.webcontroller;

import com.wanggang.template.commons.utils.ExcelUtils;
import com.wanggang.template.model.vo.UserVO;
import com.google.common.collect.Lists;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户登录 controller
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 12:39
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
@RestController
@RequestMapping("web/v1/login")
public class LoginControllerDemo extends WebBaseController {
    @PostMapping("download")
    public void download(HttpServletResponse response) throws IOException {
        List<UserVO> vos = Lists.newArrayList();
        vos.add(new UserVO(LocalDateTime.now(), "test1", "1"));
        vos.add(new UserVO(LocalDateTime.now(), "test2", "2"));

        ExcelUtils.exportExcel(vos, "test", "name", UserVO.class, "test", true, response);
    }

    @PostMapping("failure")
    public String loginFailure() {
        return "失败";
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("success")
    public String loginSuccess() {

        return "登录成功";
    }
}
