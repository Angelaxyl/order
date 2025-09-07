package com.orderapi.service.impl;

import com.orderapi.entity.OrderDetails;
import com.orderapi.mapper.OrderDetailsMapper;
import com.orderapi.service.OrderDetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xyl
 * @since 2023-05-08
 */
@Service
public class OrderDetailsServiceImpl extends ServiceImpl<OrderDetailsMapper, OrderDetails> implements OrderDetailsService {

}
