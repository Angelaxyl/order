package com.orderapi.vo.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: xueyalin
 * @description:
 * @date: 2023/05/07
 */
@Data
public class CartResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private Long userId;

    private DeviceResponse deviceResponse;

    private Integer num;

}
