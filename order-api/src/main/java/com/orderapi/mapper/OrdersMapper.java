package com.orderapi.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.orderapi.entity.Device;
import com.orderapi.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xyl
 * @since 2023-05-08
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

    IPage pageCC(IPage<Orders> page, @Param(Constants.WRAPPER) Wrapper wrapper);

}
