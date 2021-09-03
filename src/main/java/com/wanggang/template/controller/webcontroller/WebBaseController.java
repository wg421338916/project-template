package com.wanggang.template.controller.webcontroller;

import com.wanggang.template.commons.constants.CustomerCodeConstants;
import com.wanggang.template.commons.exception.BaseException;
import com.wanggang.template.controller.BaseController;
import com.wanggang.template.model.bo.UserDetailsBO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 基础Controller
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 18:20
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
public class WebBaseController extends BaseController {
    public UserDetailsBO getUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            throw BaseException.create(CustomerCodeConstants.C808);
        }

        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            throw BaseException.create(CustomerCodeConstants.C808);
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetailsBO)) {
            throw BaseException.create(CustomerCodeConstants.C808);
        }

        return (UserDetailsBO) principal;
    }
}