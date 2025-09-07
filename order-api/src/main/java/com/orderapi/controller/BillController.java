package com.orderapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.orderapi.common.QueryPageParam;
import com.orderapi.common.Result;
import com.orderapi.entity.*;
import com.orderapi.enums.BillEnum;
import com.orderapi.enums.OrderEnum;
import com.orderapi.mapper.BillMapper;
import com.orderapi.service.BillService;
import com.orderapi.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xyl
 * @since 2023-05-08
 */
@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private BillService billService;


    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
        return billService.listPage(query);
    }

    @GetMapping("/checked")
    @Transactional(rollbackFor = Exception.class)
    public Result checked(@RequestParam Long billId) {
        return billService.checked(billId);
    }
}
