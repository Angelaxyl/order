package com.orderapi.vo.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: xueyalin
 * @description:
 * @date: 2023/05/24
 */
@Data
public class ShowInstallDeviceNumRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date startDate;
    private Date endDate;
}
