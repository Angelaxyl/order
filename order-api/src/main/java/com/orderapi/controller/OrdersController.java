package com.orderapi.controller;


import com.orderapi.common.QueryPageParam;
import com.orderapi.common.Result;
import com.orderapi.vo.request.CreateInsuranceRequest;
import com.orderapi.vo.request.CreateOrderRequest;
import com.orderapi.vo.request.UpdateStateRequest;
import com.orderapi.vo.request.UploadUrlRequest;
import com.orderapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xyl
 * @since 2023-05-08
 */
@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService orderService;

    @PostMapping("/createOrder")
    public Result createOrder(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    //根据订单的id修改订单的状态
    @PostMapping("/updateStateById")
    public Result updateStateById(@RequestBody UpdateStateRequest request) {
        return  orderService.updateStateById(request);
    }

    @GetMapping("/findList")
    public Result findList(@RequestParam Long userId){
        return orderService.findList(userId);
    }

    //上传订单附件和电子保单
    @PostMapping("/uploadUrl")
    public Result uploadUrl(@RequestBody UploadUrlRequest request) {
        return  orderService.uploadUrl(request);
    }

    //生成电子保单
    @PostMapping("/createInsurance")
    public Result createInsurance(@RequestBody CreateInsuranceRequest request) throws IOException {
        return orderService.createInsurance(request);
    }


    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
        return orderService.listPage(query);
    }

    @GetMapping("/outDevices")
    public Result outDevices(@RequestParam Long orderId,@RequestParam Long adminId) {
        return orderService.outDevices(orderId,adminId);
    }

    @GetMapping("/cancelOrder")
    public Result cancelOrder(@RequestParam Long orderId) {
        return orderService.cancelOrder(orderId);
    }

    @GetMapping("/showPoint1")
    public Result showPoint1() {
        return orderService.showPoint1();
    }
    @GetMapping("/showPoint2")
    public Result showPoint2() {
        return orderService.showPoint2();
    }

}
