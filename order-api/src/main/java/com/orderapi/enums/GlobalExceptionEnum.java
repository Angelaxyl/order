package com.orderapi.enums;

import lombok.Getter;

/**
 * @author: wuze
 * @description:
 * @date: 2023/02/16
 */
public enum GlobalExceptionEnum {

    USER_REPEAT(1024, "用户已存在"),
    USERNAME_REPEAT(1025, "用户名已存在"),
    WX_OPENID_REPEAT(1025, "微信账号重复注册"),
    PHONE_REPEAT(1026, "电话号码已存在"),
    USER_EMPTY(1027,"用户不存在"),
    ROLE_ERROR(1028,"用户身份不正确"),
    PASSWORD_ERROR(1029,"用户密码错误"),
    FOLDER_REPEAT(1030,"文件夹已存在"),
    FILE_REPEAT(1031,"文件名重复"),
    FILE_NOT_FOUND(1032,"文件不存在"),
    FILE_HANDLER_ERR(1033,"文件处理器错误"),
    FILE_NAME_EMPTY(1034,"文件名不能为空"),
    PID_ERROR(1035,"父文件夹选择错误"),
    DATA_ERR(1036,"数据错误"),
    NOT_FIND(1037,"找不到记录"),
    SHARE_ID_NOT_FIND(1038,"共享id不存在或密码错误"),
    SHARE_DATA_ERR(1039,"共享失效"),
    USER_AUTH_ERR(1040,"用户权限不够"),
    SPACE_NOT_ENOUGH(1041,"磁盘空间不足"),
    EMAIL_ERR(1042,"邮箱格式不正确"),
    EMAIL_REPEAT(1043,"邮箱已被注册"),
    CODE_SEND(1044,"验证码已发送，请5分钟后重试"),
    CODE_SEND_FAIL(1045,"验证码发送失败，请重试"),
    CODE_ERR(1046,"验证码错误"),
    NOT_CODE(1047,"请先获取验证码"),
    EMAIL_EMPTY(1048,"邮箱不存在"),
    PASSWORD_ERR(1049,"两次密码不一致"),
    ;

    @Getter
    private Integer code;
    @Getter
    private String msg;

    GlobalExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
