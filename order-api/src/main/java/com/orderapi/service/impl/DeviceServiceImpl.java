package com.orderapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.orderapi.common.*;
import com.orderapi.entity.Device;
import com.orderapi.entity.DeviceImg;
import com.orderapi.mapper.DeviceImgMapper;
import com.orderapi.mapper.DeviceMapper;
import com.orderapi.vo.request.DeviceRequest;
import com.orderapi.vo.response.DeviceResponse;
import com.orderapi.service.DeviceImgService;
import com.orderapi.service.DeviceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xyl
 * @since 2023-05-02
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService {

    //物理路径
    @Value("${file.uploadFolder}")
    private String uploadFolder;

    //逻辑路径
    @Value("${file.accessPath}")
    private String accessPath;

    private static List<String> FILE_SUFFIX = new ArrayList<>();

    static {
        FILE_SUFFIX.add(".jpg");
        FILE_SUFFIX.add(".png");
        FILE_SUFFIX.add(".jpeg");
    }

    @Autowired
    private DeviceImgService deviceImgService;

    @Autowired
    private DeviceImgMapper deviceImgMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public IPage pageCC(IPage<Device> page, Wrapper wrapper) {
        return deviceMapper.pageCC(page,wrapper);
    }

    @Override
    public Result upload(MultipartFile file){
        String uuid = UuidUtil.getUuid();
        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf(".")).toLowerCase();
        if(!FILE_SUFFIX.contains(suffix)){
            return Result.fail();
        }
        File dest = new File(uploadFolder+uuid+filename.substring(filename.lastIndexOf(".")));
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.suc(accessPath+uuid+filename.substring(filename.lastIndexOf(".")));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result save(DeviceRequest deviceRequest){
        //保存设备表
        Long id = SnowFlakeIdWorker.nextId();
        Device device = new Device();
        BeanUtils.copyProperties(deviceRequest, device);
        device.setId(id);
        save(device);
        //图片操作
        List<String> urls = deviceRequest.getUrls();
        urls.forEach(url -> {
            DeviceImg img = new DeviceImg();
            img.setId(SnowFlakeIdWorker.nextId());
            img.setImgPath(url);
            img.setDeviceId(id);
            deviceImgService.save(img);
        });
        return Result.suc();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(DeviceRequest deviceRequest) throws InterruptedException {
        redisUtil.del(deviceRequest.getId().toString());
        //删除设备图片表数据
        LambdaQueryWrapper<DeviceImg> wrapper = new QueryWrapper<DeviceImg>().lambda()
                .eq(DeviceImg::getDeviceId, deviceRequest.getId());
        deviceImgMapper.delete(wrapper);

        //修改设备表
        Device device = new Device();
        BeanUtils.copyProperties(deviceRequest, device);
        updateById(device);
        Thread.sleep(1000);
        redisUtil.del(deviceRequest.getId().toString());
        //图片操作
        List<String> urls = deviceRequest.getUrls();

        urls.forEach(url -> {
            DeviceImg img = new DeviceImg();
            img.setId(SnowFlakeIdWorker.nextId());
            img.setImgPath(url);
            img.setDeviceId(device.getId());
            deviceImgService.save(img);
        });
        return Result.suc();
    }

    @Override
    public Result del(Long id){

        //删除设备图片表数据
        LambdaQueryWrapper<DeviceImg> wrapper = new QueryWrapper<DeviceImg>().lambda().eq(DeviceImg::getDeviceId, id);
        deviceImgMapper.delete(wrapper);
        redisUtil.del(id.toString());
        return removeById(id)?Result.suc():Result.fail();
    }

    @Override
    public Result listPage(QueryPageParam query){
        HashMap param = query.getParam();
        String deviceName = (String)param.get("deviceName");
        String deviceModel = (String)param.get("deviceModel");

        Page<Device> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Device> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(deviceName) && !"null".equals(deviceName)){
            lambdaQueryWrapper.like(Device::getDeviceName,deviceName);
        }
        if(StringUtils.isNotBlank(deviceModel) && !"null".equals(deviceModel)){
            lambdaQueryWrapper.like(Device::getDeviceModel,deviceModel);
        }

        IPage result = pageCC(page,lambdaQueryWrapper);
        List<Device> devices = result.getRecords();
        if(devices.size() == 0){
            return Result.suc(result.getRecords(),result.getTotal());
        }

        ArrayList<Long> ids = new ArrayList<>();
        Map<Long, DeviceResponse> maps = new HashMap<>();

        devices.forEach(device -> {
            ids.add(device.getId());
            DeviceResponse response = new DeviceResponse();
            BeanUtils.copyProperties(device,response);
            response.setUrls(new ArrayList<>());
            maps.put(device.getId(), response);
        });

        LambdaQueryWrapper<DeviceImg> wrapper = new QueryWrapper<DeviceImg>().lambda().in(DeviceImg::getDeviceId, ids);
        List<DeviceImg> imgs = deviceImgMapper.selectList(wrapper);

        imgs.forEach(img -> {
            DeviceResponse response = maps.get(img.getDeviceId());
            response.getUrls().add(img.getImgPath());
        });

        List<DeviceResponse> list = new ArrayList<>(maps.values());

        return Result.suc(list,result.getTotal());
    }

    @Override
    public List<DeviceResponse> findList(){
        List<Device> devices = list();
        ArrayList<Long> ids = new ArrayList<>();
        Map<Long, DeviceResponse> maps = new HashMap<>();

        devices.forEach(device -> {
            ids.add(device.getId());
            DeviceResponse response = new DeviceResponse();
            BeanUtils.copyProperties(device,response);
            response.setUrls(new ArrayList<>());
            maps.put(device.getId(), response);
        });

        LambdaQueryWrapper<DeviceImg> wrapper = new QueryWrapper<DeviceImg>().lambda().in(DeviceImg::getDeviceId, ids);
        List<DeviceImg> imgs = deviceImgMapper.selectList(wrapper);
        imgs.forEach(img -> {
            DeviceResponse response = maps.get(img.getDeviceId());
            response.getUrls().add(img.getImgPath());
        });

        List<DeviceResponse> list = new ArrayList<>(maps.values());
        return list;
    }

    @Override
    public DeviceResponse findById(Long id){
        DeviceResponse res = (DeviceResponse)redisUtil.get(id.toString());
        if(Objects.nonNull(res)){
            return res;
        }
        DeviceResponse response = new DeviceResponse();
        Device device = deviceMapper.selectById(id);
        BeanUtils.copyProperties(device,response);
        response.setUrls(new ArrayList<>());
        LambdaQueryWrapper<DeviceImg> wrapper = new QueryWrapper<DeviceImg>().lambda()
                .eq(DeviceImg::getDeviceId, device.getId());
        List<DeviceImg> imgs = deviceImgMapper.selectList(wrapper);
        imgs.forEach(img -> {
            response.getUrls().add(img.getImgPath());
        });
        redisUtil.setex(id.toString(),response,60*60);
        return response;
    }
}
