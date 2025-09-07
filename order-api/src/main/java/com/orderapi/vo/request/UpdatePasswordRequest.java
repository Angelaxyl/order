package com.orderapi.vo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: xueyalin
 * @description:
 * @date: 2023/05/16
 */
@Data
public class UpdatePasswordRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;;

    private String originPass;

    private String pass;

    private String checkPass;
}
