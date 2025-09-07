package com.orderapi.vo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: xueyalin
 * @description:
 * @date: 2023/05/09
 */
@Data
public class BindSimRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String sim;
}