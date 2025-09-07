package com.orderapi.exception;

import lombok.Data;

/**
 * @author: wuze
 * @description:
 * @date: 2023/02/16
 */
@Data
public class GlobalException extends RuntimeException {
    private static final long serialVersionUID = -4006387037704180514L;

    private Integer code;
    private String message;

    public GlobalException(Integer code , String message) {
        this.code = code;
        this.message = message;
    }

    public GlobalException() {
        super();
    }

    public GlobalException(String message) {
        super(message);
    }

    public GlobalException(String message, Throwable cause) {
        super(message, cause);
    }

    public GlobalException(Throwable cause) {
        super(cause);
    }

    protected GlobalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
