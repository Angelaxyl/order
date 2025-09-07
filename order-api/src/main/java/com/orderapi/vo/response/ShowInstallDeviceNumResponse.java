package com.orderapi.vo.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: xueyalin
 * @description:
 * @date: 2023/05/24
 */
@Data
public class ShowInstallDeviceNumResponse implements Serializable {
    private static final long serialVersionUID = 1L;


    private String userName;

    private String name;

    private Integer num;
}
