package com.orderapi.vo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: xueyalin
 * @description:
 * @date: 2023/05/12
 */
@Data
public class CreateInsuranceRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;
    private String cardNumber;
    private String price;
}
