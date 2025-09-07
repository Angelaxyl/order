package com.orderapi.enums;

import lombok.Getter;

/**
 * @author: xueyalin
 * @description:
 * @date: 2023/05/01
 */
public enum InstallerStateEnum {
    /**
     * 未分配
     */
    UNASSIGNED(0,"未分配"),
    /**
     * 已分配
     */
    ASSIGNED(1,"已分配");

    @Getter
    private Integer code;

    @Getter
    private String state;

    InstallerStateEnum(Integer code, String state) {
        this.code = code;
        this.state = state;
    }
}
