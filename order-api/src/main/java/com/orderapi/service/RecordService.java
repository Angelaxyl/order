package com.orderapi.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.orderapi.common.QueryPageParam;
import com.orderapi.common.Result;
import com.orderapi.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xyl
 * @since 2023-05-04
 */
public interface RecordService extends IService<Record> {
    IPage pageCC(IPage<Record> page, Wrapper wrapper);
    public Result listPage(QueryPageParam query);
    public Result saveRecord(Record record);
}
