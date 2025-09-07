package com.orderapi.entity;

import lombok.Data;

/**
 * @author: xueyalin
 * @description:
 * @date: 2023/05/06
 */
@Data
public class RecordRes extends Record {
    private String username;
    private String adminname;
    private String deviceName;
}
