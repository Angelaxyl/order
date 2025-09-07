package com.orderapi.controller;


import com.orderapi.common.Result;
import com.orderapi.vo.request.BindSimRequest;
import com.orderapi.service.OutDevicesForSimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xyl
 * @since 2023-05-09
 */
@RestController
@RequestMapping("/out-devices-for-sim")
public class OutDevicesForSimController {
    @Autowired
    private OutDevicesForSimService outDevicesForSimService;

    @GetMapping("/findByOrderId")
    public Result findByOrderId(@RequestParam Long orderId){
        return outDevicesForSimService.findByOrderId(orderId);
    }

    @PostMapping("/bind")
    public Result bind(@RequestBody List<BindSimRequest> list){
        return outDevicesForSimService.bind(list);
    }
}
