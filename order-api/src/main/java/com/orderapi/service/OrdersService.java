package com.orderapi.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.orderapi.common.QueryPageParam;
import com.orderapi.common.Result;
import com.orderapi.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.orderapi.vo.request.CreateInsuranceRequest;
import com.orderapi.vo.request.CreateOrderRequest;
import com.orderapi.vo.request.UpdateStateRequest;
import com.orderapi.vo.request.UploadUrlRequest;

import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xyl
 * @since 2023-05-08
 */
public interface OrdersService extends IService<Orders> {

    IPage pageCC(IPage<Orders> page, Wrapper wrapper);
    public Result createOrder(CreateOrderRequest request);
    public Result updateStateById(UpdateStateRequest request);
    public Result findList(Long userId);
    public Result uploadUrl(UploadUrlRequest request);
    public Result createInsurance(CreateInsuranceRequest request) throws IOException;
    public Result listPage(QueryPageParam query);
    public Result outDevices(Long orderId,Long adminId);
    public Result cancelOrder(Long orderId);
    public Result showPoint1();
    public Result showPoint2();
}
