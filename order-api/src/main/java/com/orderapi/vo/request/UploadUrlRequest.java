package com.orderapi.vo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: xueyalin
 * @description:
 * @date: 2023/05/08
 */
@Data
public class UploadUrlRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orderId;

    private Integer orderState;

    private String insuranceUrl;

    private String billUrl;
}