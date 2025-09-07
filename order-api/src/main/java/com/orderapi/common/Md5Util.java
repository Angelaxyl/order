package com.orderapi.common;

import org.springframework.util.DigestUtils;

/**
 * @author: xueyalin
 * @description: md5加密工具类
 */
public class Md5Util {

    /**
     * 封装一个方法 对密码进行加密
     */
    public static String getMD5Pwd(String password, String salt) {
        String md5Pwd = password + salt;
        return DigestUtils.md5DigestAsHex(md5Pwd.getBytes());
    }

}
