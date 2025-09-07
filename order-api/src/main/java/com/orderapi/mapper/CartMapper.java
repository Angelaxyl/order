package com.orderapi.mapper;

import com.orderapi.entity.Cart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xyl
 * @since 2023-05-07
 */
@Mapper
public interface CartMapper extends BaseMapper<Cart> {

}
