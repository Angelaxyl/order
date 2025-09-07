package com.orderapi.controller;


import com.orderapi.common.Result;
import com.orderapi.entity.Cart;
import com.orderapi.vo.response.CartResponse;
import com.orderapi.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xyl
 * @since 2023-05-07
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/findCartByUserId")
    public List<CartResponse> findCartByUserId(@RequestParam("userId") Long userId) {
        return cartService.findCartByUserId(userId);
    }

    @PostMapping("/addCart")
    public Result addCart(@RequestBody Cart cart) {
        return cartService.addCart(cart);
    }

    @PostMapping("/updateCarts")
    public Result updateCarts(@RequestBody Cart cart) {
        return cartService.updateCarts(cart);
    }

    @PostMapping("/delCarts")
    public Result delCarts(@RequestBody Cart cart) {
        return cartService.delCarts(cart);
    }
}
