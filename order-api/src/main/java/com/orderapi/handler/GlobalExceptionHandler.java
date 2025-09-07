package com.orderapi.handler;

import com.orderapi.common.Result;
import com.orderapi.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author: wuze
 * @description:
 * @date: 2023/02/16
 */
@Slf4j
@SuppressWarnings("ALL")
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception e) {
        log.error("未知异常{}", e);
        return Result.fail(500,"未知异常:"+e.getMessage());
    }

    @ExceptionHandler(GlobalException.class)
    public Result exceptionHandler(GlobalException e) {
        log.error("code:{},msg:{}",e.getCode(),e.getMessage());
        return Result.fail(e.getCode(),e.getMessage());
    }

}
