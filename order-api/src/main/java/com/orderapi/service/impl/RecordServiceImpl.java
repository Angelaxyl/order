package com.orderapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.orderapi.common.QueryPageParam;
import com.orderapi.common.RedisUtil;
import com.orderapi.common.Result;
import com.orderapi.common.SnowFlakeIdWorker;
import com.orderapi.entity.Device;
import com.orderapi.entity.Record;
import com.orderapi.mapper.RecordMapper;
import com.orderapi.service.DeviceService;
import com.orderapi.service.RecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xyl
 * @since 2023-05-04
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {
    @Resource
    private RecordMapper recordMapper;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public IPage pageCC(IPage<Record> page, Wrapper wrapper) {
        return recordMapper.pageCC(page,wrapper);
    }

    @Override
    public Result listPage( QueryPageParam query){
        HashMap param = query.getParam();
        //设备的名字
        String deviceName = (String)param.get("deviceName");
        String role =  (String)param.get("role");
        String userId =  (String)param.get("userId");

        Page<Record> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        QueryWrapper<Record> queryWrapper = new QueryWrapper();
        queryWrapper.apply(" a.device_id=b.id ");

        if(StringUtils.isNotBlank(deviceName) && !"null".equals(deviceName)){
            queryWrapper.like("b.device_name",deviceName);
        }
        if(StringUtils.isNotBlank(role) && "1".equals(role)){
            queryWrapper.apply(" a.user_id="+userId);
        }

        IPage result = pageCC(page,queryWrapper);
        return Result.suc(result.getRecords(),result.getTotal());
    }
    //出入库
    @Override
    public Result saveRecord(Record record){
        Device device = deviceService.getById(record.getDeviceId());
        int n = record.getCount();
        // 出库
        if("2".equals(record.getAction())){
            n = -n;
            record.setCount(n);
        }
        int num = device.getStock()+n;
        device.setStock(num);
        deviceService.updateById(device);
        redisUtil.del(device.getId().toString());
        record.setId(SnowFlakeIdWorker.nextId());
        return save(record)?Result.suc():Result.fail();
    }
}
