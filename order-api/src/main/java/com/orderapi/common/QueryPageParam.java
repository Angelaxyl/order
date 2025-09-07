package com.orderapi.common;

import lombok.Data;

import java.util.HashMap;

/**
 * @author: xueyalin
 * @description:分页的入参的封装
 */
@Data
public class QueryPageParam {

    // 每页多少条记录
    private static int PAGE_SIZE = 20;
    // 当前页
    private static int PAGE_NUM = 1;

    private  int pageSize=PAGE_SIZE;
    private  int pageNum=PAGE_NUM;

    private HashMap param = new HashMap();
}
