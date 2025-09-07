package com.orderapi.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
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
@ApiModel(value="OrderDetails对象", description="")
public class OrderDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "定位设备id")
    private Long deviceId;

    @ApiModelProperty(value = "订购数量")
    private Integer num;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
