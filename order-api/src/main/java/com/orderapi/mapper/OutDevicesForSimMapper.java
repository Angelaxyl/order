package com.orderapi.mapper;

import com.orderapi.entity.OutDevicesForSim;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.orderapi.entity.OutDevicesForSimRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xyl
 * @since 2023-05-09
 */
@Mapper
public interface OutDevicesForSimMapper extends BaseMapper<OutDevicesForSim> {


    public List<OutDevicesForSimRes> findByOrderId(@Param("orderId") Long orderId);

}
