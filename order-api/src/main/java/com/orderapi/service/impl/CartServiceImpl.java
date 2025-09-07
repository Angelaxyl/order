package com.orderapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.orderapi.common.Result;
import com.orderapi.common.SnowFlakeIdWorker;
import com.orderapi.entity.Cart;
import com.orderapi.entity.Device;
import com.orderapi.entity.DeviceImg;
import com.orderapi.mapper.CartMapper;
import com.orderapi.mapper.DeviceImgMapper;
import com.orderapi.mapper.DeviceMapper;
import com.orderapi.vo.response.CartResponse;
import com.orderapi.vo.response.DeviceResponse;
import com.orderapi.service.CartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xyl
 * @since 2023-05-07
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    private DeviceImgMapper deviceImgMapper;
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private CartMapper cartMapper;

    @Override
    public List<CartResponse> findCartByUserId(Long userId) {
        LambdaQueryWrapper<Cart> wrapper = new QueryWrapper<Cart>().lambda().
                eq(Cart::getUserId, userId);
        List<Cart> cart = cartMapper.selectList(wrapper);

        List<CartResponse> cartresponses = Lists.newArrayList();
        for (Cart cart1 : cart) {
            CartResponse cartResponse = new CartResponse();
            BeanUtils.copyProperties(cart1, cartResponse);
            DeviceResponse response = new DeviceResponse();
            Device device = deviceMapper.selectById(cart1.getDeviceId());
            BeanUtils.copyProperties(device, response);
            response.setUrls(new ArrayList<>());

            LambdaQueryWrapper<DeviceImg> wrapper1 = new QueryWrapper<DeviceImg>().lambda()
                    .eq(DeviceImg::getDeviceId, device.getId());
            List<DeviceImg> imgs = deviceImgMapper.selectList(wrapper1);
            imgs.forEach(img -> {
                response.getUrls().add(img.getImgPath());
            });
            cartResponse.setDeviceResponse(response);
            cartresponses.add(cartResponse);
        }
        return cartresponses;
    }

    @Override
    public Result addCart(Cart cart) {
        //如果购物车中已经有这个设备，那就在这条记录的基础上加一
        LambdaQueryWrapper<Cart> wrapper = new QueryWrapper<Cart>().lambda()
                .eq(Cart::getDeviceId, cart.getDeviceId()).eq(Cart::getUserId, cart.getUserId());
        Cart cart1 = cartMapper.selectOne(wrapper);
        if (cart1 != null) {
            cart1.setNum(cart1.getNum() + 1);
            cartMapper.updateById(cart1);
            return Result.suc();
        }
        //如果购物车中没有这个设备，那就新建一条跟这个设备一一对应的记录
        cart.setId(SnowFlakeIdWorker.nextId());
        save(cart);
        return Result.suc();
    }

    @Override
    public Result updateCarts(Cart cart) {
        Cart cart2 = getById(cart.getId());
        cart2.setNum(cart.getNum());
        cartMapper.updateById(cart2);
        return Result.suc();
    }

    @Override
    public Result delCarts(Cart cart) {
        removeById(cart.getId());
        return Result.suc();
    }

}
