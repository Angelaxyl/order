package com.orderapi.enums;

import lombok.Getter;

/**
 * @author: xueyalin
 * @description:
 * @date: 2023/05/01
 */
public enum RoleEnum {
    /**
     * 管理员
     */
    ADMIN(0,"管理员"),
    /**
     * 使用人员
     */
    USER(1,"使用人员"),
    /**
     * 安装人员
     */
    INSTALLER(2,"安装人员");


    @Getter
    private Integer code;

    @Getter
    private String state;

    RoleEnum(Integer code, String state) {
        this.code = code;
        this.state = state;
    }
}
