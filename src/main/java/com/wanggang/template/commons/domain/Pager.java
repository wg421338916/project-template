package com.wanggang.template.commons.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description: 返回值对象, pageIndex默认从1开始
 * <p>
 * 规范文档：https://confluence.wanggang-info.com/x/G4AT
 * "pager":{
 * "pageIndex": 1,
 * "pageSize": 10,
 * "total": 10
 * "list":[]
 * }
 * @author: wg
 * @create: 2019/3/23
 */
@Data
public class Pager<T> {
    @ApiModelProperty(value = "接口返回数据列表")
    private List<T> list;
    @ApiModelProperty(value = "分页索引，从1开始")
    private Integer pageIndex;
    @ApiModelProperty(value = "分页大小")
    private Integer pageSize;
    @ApiModelProperty(value = "总数据量")
    private Integer total;

    public Pager() {
    }

    public Pager(Integer pageIndex, Integer pageSize, Integer total, List<T> dataList) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.total = total;
        this.list = dataList;
    }
}
