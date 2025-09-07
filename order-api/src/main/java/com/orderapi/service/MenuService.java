package com.orderapi.service;

import com.orderapi.common.Result;
import com.orderapi.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xyl
 * @since 2023-04-28
 */
public interface MenuService extends IService<Menu> {

    public Result list(String role);
}
