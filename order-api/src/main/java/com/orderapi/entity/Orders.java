package com.orderapi.entity;

import java.time.LocalDateTime;
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
 * @since 2023-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Order对象", description="")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "操作人id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty(value = "账单id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long billId;

    @ApiModelProperty(value = "总额")
    private Integer total;

    @ApiModelProperty(value = "安装人员id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long installerId;

    @ApiModelProperty(value = "订单状态")
    private String orderState;

    @ApiModelProperty(value = "电子保单")
    private String insuranceUrl;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
