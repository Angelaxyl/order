package com.orderapi.enums;

import lombok.Getter;

/**
 * @author: xueyalin
 * @description:
 * @date: 2023/05/08
 */
public enum BillEnum {
    /**
     * 未支付
     */
    NOT_PAID(0,"未支付"),
    /**
     * 已支付
     */
    PAID(1,"已支付"),
    /**
     * 审核通过
     */
    CHECKED(2,"审核通过");


    @Getter
    private Integer code;

    @Getter
    private String state;

    BillEnum(Integer code, String state) {
        this.code = code;
        this.state = state;
    }
}