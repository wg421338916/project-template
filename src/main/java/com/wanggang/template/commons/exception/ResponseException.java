package com.wanggang.template.commons.exception;

import com.wanggang.template.commons.domain.Result;

import java.io.Serializable;

/**
 * 异常公共接口类
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 12:39
 * @Copyright © 2020-2021 北京王刚信息科技有限公司
 * @since 1.0.0
 */
public interface ResponseException {
    /**
     * 获取默认输出
     *
     * @return
     */
    default Result<Serializable> getResponse() {
        return Result.result(getCode());
    }

    /**
     * 获取错误码
     *
     * @return
     */
    Integer getCode();
}
