package com.orderapi.entity;

import lombok.Data;

/**
 * @author: xueyalin
 * @description:
 * @date: 2023/05/09
 */
@Data
public class OrdersRes extends Orders{
    private String userName;
    private String installerName;
}
