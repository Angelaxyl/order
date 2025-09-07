package com.orderapi.service;

import com.orderapi.common.Result;
import com.orderapi.entity.OutDevicesForSim;
import com.baomidou.mybatisplus.extension.service.IService;
import com.orderapi.vo.request.BindSimRequest;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xyl
 * @since 2023-05-09
 */
public interface OutDevicesForSimService extends IService<OutDevicesForSim> {
    public Result findByOrderId(Long orderId);
    public Result bind(List<BindSimRequest> list);

}
