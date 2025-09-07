package com.orderapi.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.orderapi.common.QueryPageParam;
import com.orderapi.common.Result;
import com.orderapi.entity.Device;
import com.baomidou.mybatisplus.extension.service.IService;
import com.orderapi.vo.request.DeviceRequest;
import com.orderapi.vo.response.DeviceResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xyl
 * @since 2023-05-02
 */
public interface DeviceService extends IService<Device> {

    IPage pageCC(IPage<Device> page, Wrapper wrapper);
    public Result upload(MultipartFile file);
    public Result save(DeviceRequest deviceRequest);
    public Result update(DeviceRequest deviceRequest) throws InterruptedException;
    public Result del(Long id);
    public Result listPage(QueryPageParam query);
    public List<DeviceResponse> findList();
    public DeviceResponse findById(Long id);

}
