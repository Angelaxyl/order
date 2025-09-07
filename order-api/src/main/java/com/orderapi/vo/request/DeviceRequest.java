package com.orderapi.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: xueyalin
 * @description:
 * @date: 2023/05/02
 */
@Data
public class DeviceRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String deviceName;

    private String deviceModel;

    private Integer price;

    private Integer stock;

    private String introduction;

    private List<String> urls;

}
