package com.orderapi.vo.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author: xueyalin
 * @description:
 * @date: 2023/05/02
 */
@Data
public class DeviceResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String deviceName;

    private String deviceModel;

    private Integer price;

    private Integer stock;

    private String introduction;

    private List<String> urls;

    private Date createTime;

    private Date updateTime;

}