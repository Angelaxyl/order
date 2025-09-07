package com.orderapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.orderapi.common.Result;
import com.orderapi.entity.Orders;
import com.orderapi.entity.OutDevicesForSim;
import com.orderapi.entity.OutDevicesForSimRes;
import com.orderapi.entity.User;
import com.orderapi.enums.InstallerStateEnum;
import com.orderapi.enums.OrderEnum;
import com.orderapi.mapper.OutDevicesForSimMapper;
import com.orderapi.mapper.UserMapper;
import com.orderapi.vo.request.BindSimRequest;
import com.orderapi.service.OrdersService;
import com.orderapi.service.OutDevicesForSimService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xyl
 * @since 2023-05-09
 */
@Service
public class OutDevicesForSimServiceImpl extends ServiceImpl<OutDevicesForSimMapper, OutDevicesForSim> implements OutDevicesForSimService {
    @Autowired
    private OutDevicesForSimMapper outDevicesForSimMapper;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result findByOrderId(Long orderId){
        List<OutDevicesForSimRes> list = outDevicesForSimMapper.findByOrderId(orderId);
        return Result.suc(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result bind(List<BindSimRequest> list){
        //获取其中一个出库设备
        BindSimRequest bindSimRequest = list.get(0);
        OutDevicesForSim outDevicesForSim = getById(bindSimRequest.getId());
        //校验规则
        Orders order = ordersService.getById(outDevicesForSim.getOrderId());
        if (!Integer.valueOf(order.getOrderState()).equals(OrderEnum.OUTDEVICES.getCode())) {
            return Result.fail();
        }
        //绑定sim卡
        list.forEach(request->{
            LambdaUpdateWrapper<OutDevicesForSim> wrapper = new UpdateWrapper<OutDevicesForSim>().lambda()
                    .set(OutDevicesForSim::getSim, request.getSim())
                    .eq(OutDevicesForSim::getId, request.getId());
            update(wrapper);
        });
        order.setOrderState(OrderEnum.ENDED.getCode().toString());
        order.setUpdateTime(LocalDateTime.now());
        ordersService.updateById(order);
        //恢复安装人员状态为未分配
        User installer = userMapper.selectById(order.getInstallerId());
        installer.setState(InstallerStateEnum.UNASSIGNED.getCode());
        userMapper.updateById(installer);
        return Result.suc();
    }

}
