package com.orderapi.service;

import com.orderapi.common.Result;
import com.orderapi.entity.Cart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.orderapi.vo.response.CartResponse;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xyl
 * @since 2023-05-07
 */
public interface CartService extends IService<Cart> {
    public List<CartResponse> findCartByUserId(Long userId);
    public Result addCart(Cart cart);
    public Result updateCarts(Cart cart);
    public Result delCarts(Cart cart);

}
