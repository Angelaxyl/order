package com.orderapi.service.impl;

import com.orderapi.common.Result;
import com.orderapi.entity.Menu;
import com.orderapi.mapper.MenuMapper;
import com.orderapi.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xyl
 * @since 2023-04-28
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public Result list(String role) {
        List list = lambdaQuery().like(Menu::getMenuRight, role).list();
        return Result.suc(list);
    }
}
