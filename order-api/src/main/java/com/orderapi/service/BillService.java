package com.orderapi.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.orderapi.common.QueryPageParam;
import com.orderapi.common.Result;
import com.orderapi.entity.Bill;
import com.baomidou.mybatisplus.extension.service.IService;
import com.orderapi.entity.Device;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xyl
 * @since 2023-05-08
 */
public interface BillService extends IService<Bill> {
    IPage pageCC(IPage<Bill> page, Wrapper wrapper);
    public Result listPage(QueryPageParam query);
    public Result checked(Long billId);
}
