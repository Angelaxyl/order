package com.orderapi.controller;

import com.orderapi.common.QueryPageParam;
import com.orderapi.common.Result;
import com.orderapi.vo.request.DeviceRequest;
import com.orderapi.vo.response.DeviceResponse;
import com.orderapi.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xyl
 * @since 2023-05-02
 */
@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    //上传图片
    @PostMapping("/upload")
    public Result upload(MultipartFile file){
        return deviceService.upload(file);
    }

    //新增
    @PostMapping("/save")
    @Transactional(rollbackFor = Exception.class)
    public Result save(@RequestBody DeviceRequest deviceRequest){
        return deviceService.save(deviceRequest);
    }

    @PostMapping("/update")
    @Transactional(rollbackFor = Exception.class)
    public Result update(@RequestBody DeviceRequest deviceRequest) throws InterruptedException {
        return deviceService.update(deviceRequest);
    }

    //删除
    @GetMapping("/del")
    public Result del(@RequestParam Long id){
        return deviceService.del(id);
    }

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
        return deviceService.listPage(query);
    }

    @GetMapping("/findList")
    public List<DeviceResponse> findList(){
        return deviceService.findList();
    }

    @GetMapping("/findById")
    public DeviceResponse findById(@RequestParam("id") Long id){
        return deviceService.findById(id);
    }
}
