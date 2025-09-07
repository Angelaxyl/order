package com.orderapi.vo.request;

import com.orderapi.vo.response.CartResponse;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: xueyalin
 * @description:
 * @date: 2023/05/08
 */
@Data
public class CreateOrderRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long installerId;

    private List<CartResponse> cartResponse;

    private Integer total;
}