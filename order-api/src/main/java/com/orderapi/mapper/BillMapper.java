package com.orderapi.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.orderapi.entity.Bill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.orderapi.entity.Device;
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
public interface BillMapper extends BaseMapper<Bill> {

    IPage pageCC(IPage<Bill> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
