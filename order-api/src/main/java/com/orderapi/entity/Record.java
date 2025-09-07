package com.orderapi.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2023-05-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Record对象", description="")
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "日志id")
    private Long id;

    @ApiModelProperty(value = "操作人id，管理员id")
    private Long adminId;

    @ApiModelProperty(value = "申请人id，使用人员id")
    private Long userId;

    @ApiModelProperty(value = "定位设备id")
    private Long deviceId;

    @ApiModelProperty(value = "数量")
    private Integer count;

    @TableField(exist = false)
    private String action;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
