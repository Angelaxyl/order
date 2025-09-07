package com.orderapi.enums;

import lombok.Getter;

/**
 * @author: xueyalin
 * @description:
 * @date: 2023/05/08
 */
public enum OrderEnum {
    /**
     * 未支付
     */
    NOT_PAID(0,"未支付"),
    /**
     * 已支付
     */
    PAID(1,"已支付"),
    /**
     * 已上传
     */
    UPLOADED(2,"已上传"),
    /**
     * 已审核
     */
    CHECKED(3,"已审核"),
    /**
     * 已出库
     */
    OUTDEVICES(4,"已出库"),
    /**
     * 已结束
     */
    ENDED(5,"已结束");


    @Getter
    private Integer code;

    @Getter
    private String state;

    OrderEnum(Integer code, String state) {
        this.code = code;
        this.state = state;
    }
}