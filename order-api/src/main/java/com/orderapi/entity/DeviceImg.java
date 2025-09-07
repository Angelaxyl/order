package com.orderapi.entity;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author xyl
 * @since 2023-05-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="DeviceImg对象", description="")
public class DeviceImg implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备_图片id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "设备id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deviceId;

    @ApiModelProperty(value = "图片路径")
    private String imgPath;


}
