package com.orderapi.context;

import com.orderapi.entity.User;
import com.orderapi.enums.GlobalExceptionEnum;
import com.orderapi.exception.GlobalException;

import java.util.Objects;

import static com.orderapi.enums.GlobalExceptionEnum.USER_EMPTY;

/**
 * @author: wuze
 * @description:
 * @date: 2023/02/17
 */
public class UserContext {

    private static ThreadLocal<User> context = new ThreadLocal<>();

    public static User getUser(){
        User user = context.get();
        if(Objects.isNull(user)){
            throw new GlobalException(USER_EMPTY.getCode(),USER_EMPTY.getMsg());
        }
        return user;
    }

    public static void setUser(User user){
        context.set(user);
    }
}
