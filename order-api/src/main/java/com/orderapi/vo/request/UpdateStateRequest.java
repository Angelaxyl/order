package com.orderapi.vo.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: xueyalin
 * @description:orderController/updateStateById
 * @date: 2023/05/08
 */
@Data
public class UpdateStateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orderId;;

    private Integer orderState;
}