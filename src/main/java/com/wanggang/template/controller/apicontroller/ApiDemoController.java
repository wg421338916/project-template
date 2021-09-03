package com.wanggang.template.controller.apicontroller;

import com.wanggang.template.controller.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基于auth2 验证的demo
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 18:00
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
@RestController
@RequestMapping("api/v1")
public class ApiDemoController extends BaseController {

    @GetMapping("product/{id}")
    public String getProduct(@PathVariable String id) {
        //for debug
        return "product id : " + id;
    }

    @GetMapping("order/{id}")
    public String getOrder(@PathVariable String id) {
        //for debug
        return "order id : " + id;
    }
}
