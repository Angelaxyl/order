package com.orderapi.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.orderapi.common.*;
import com.orderapi.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.orderapi.vo.request.ShowInstallDeviceNumRequest;
import com.orderapi.vo.request.UpdatePasswordRequest;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xyl
 * @since 2023-04-24
 */
public interface UserService extends IService<User> {

    IPage pageC(IPage<User> page);
    IPage pageCC(IPage<User> page, Wrapper wrapper);

    public List<User> list();
    public Result findByNo(String userName);
    //新增
    public Result saveUser(User user);
    //更新
    public Result update(User user);
    //删除
    public Result del(Long id);
    //修改密码
    public Result updatePassword(UpdatePasswordRequest request);
    //登录电脑端
    public Result login(User user);
    //登录移动端
    public Result loginWx(User user);
    //修改
    public boolean mod(User user);
    //新增或修改
    public boolean saveOrMod(User user);
    //删除
    public boolean delete(Integer id);
    //查询（模糊、匹配）
    public List<User> listP(User user);
    //分页（自带的方法）
    public List<User> listPage(QueryPageParam query);
    //自定义的分页
//    public List<User> listPageC(@RequestBody HashMap map){
    public List<User> listPageC(QueryPageParam query);
    //测试后端返回给前端数据的封装
    public Result listPageC1(QueryPageParam query);
    //获取未分配安装人员
    public Result getInstallers();
    //获取所有安装人员
    public Result getAllInstallers();
    //获取使用人员
    public Result getUsers();

    public Result getInstallNum(ShowInstallDeviceNumRequest request);
}
