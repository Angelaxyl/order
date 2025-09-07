package com.orderapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.orderapi.common.QueryPageParam;
import com.orderapi.common.Result;
import com.orderapi.entity.Bill;
import com.orderapi.entity.Device;
import com.orderapi.entity.Orders;
import com.orderapi.enums.BillEnum;
import com.orderapi.enums.OrderEnum;
import com.orderapi.mapper.BillMapper;
import com.orderapi.mapper.DeviceMapper;
import com.orderapi.service.BillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.orderapi.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xyl
 * @since 2023-05-08
 */
@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements BillService {
    @Resource
    private BillMapper billMapper;

    @Autowired
    private OrdersService orderService;
    @Override
    public IPage pageCC(IPage<Bill> page, Wrapper wrapper) {
        return billMapper.pageCC(page,wrapper);
    }

    @Override
    public Result listPage(QueryPageParam query) {
        HashMap param = query.getParam();

        String billId = (String)param.get("billId");
        String orderId = (String)param.get("orderId");
        String userId = (String)param.get("userId");
        String billState = (String)param.get("billState");

        Page<Bill> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        QueryWrapper<Bill> queryWrapper = new QueryWrapper();

        if(StringUtils.isNotBlank(billId) && !"null".equals(billId)){
            queryWrapper.eq("bill.id",billId);
        }
        if(StringUtils.isNotBlank(orderId) && !"null".equals(orderId)){
            queryWrapper.eq("bill.order_id",orderId);
        }
        if(StringUtils.isNotBlank(userId) && !"null".equals(userId)){
            queryWrapper.eq("bill.user_id",userId);
        }
        if(StringUtils.isNotBlank(billState) && !"null".equals(billState)){
            queryWrapper.eq("bill.bill_state",billState);
        }

        IPage result = pageCC(page,queryWrapper);
        return Result.suc(result.getRecords(),result.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result checked(Long billId) {
        Bill bill = billMapper.selectById(billId);
        Orders order = orderService.getById(bill.getOrderId());
        if (!Integer.valueOf(bill.getBillState()).equals(BillEnum.PAID.getCode())) {
            return Result.fail();
        }
        if (!Integer.valueOf(order.getOrderState()).equals(OrderEnum.UPLOADED.getCode())) {
            return Result.fail();
        }
        bill.setBillState(BillEnum.CHECKED.getCode().toString());
        updateById(bill);
        order.setOrderState(OrderEnum.CHECKED.getCode().toString());
        orderService.updateById(order);
        return Result.suc();
    }
}
