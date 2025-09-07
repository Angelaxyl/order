package com.orderapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.orderapi.common.QueryPageParam;
import com.orderapi.common.Result;
import com.orderapi.common.SnowFlakeIdWorker;
import com.orderapi.entity.Device;
import com.orderapi.entity.Record;
import com.orderapi.service.DeviceService;
import com.orderapi.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xyl
 * @since 2023-05-04
 */
@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
        return recordService.listPage(query);
    }
    //出入库
    @PostMapping("/save")
    public Result save(@RequestBody Record record){
        return recordService.saveRecord(record);
    }
}
