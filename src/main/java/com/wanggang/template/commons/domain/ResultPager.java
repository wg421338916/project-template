package com.wanggang.template.commons.domain;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @description: 带分页的返回值
 * "data": {
 * "pageIndex": 1,
 * "pageSize": 10,
 * "total": 10
 * "list":[]
 * }
 * @author: wg
 * @create: 2019/3/23
 */
@Data
@ApiModel("返回结果")
public class ResultPager<T> extends Result<Pager<T>> {
    public ResultPager() {
    }

    public ResultPager(Integer pageIndex, Integer pageSize, Integer total, List<T> dataList) {
        this.data = new Pager<>(pageIndex, pageSize, total, dataList);
        this.msg = "ok";
        this.code = 0;
    }

    public static <T> ResultPager<T> success(Integer pageIndex, Integer pageSize, Integer total, List<T> dataList) {
        return new ResultPager<>(pageIndex, pageSize, total, dataList);
    }
}
