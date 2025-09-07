package com.orderapi.controller;


import com.orderapi.common.*;
import com.orderapi.entity.User;
import com.orderapi.vo.request.ShowInstallDeviceNumRequest;
import com.orderapi.vo.request.UpdatePasswordRequest;
import com.orderapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xueyalin
 * @since 2023-04-24
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public List<User> list(){
        return userService.list();
    }

    @GetMapping("/findByUserName")
    public Result findByNo(@RequestParam String userName){
        return userService.findByNo(userName);
    }
    //新增
    @PostMapping("/save")
    public Result save(@RequestBody User user){
        return userService.saveUser(user);
    }
    //更新
    @PostMapping("/update")
    public Result update(@RequestBody User user){
        return userService.update(user);
    }
    //删除
    @GetMapping("/del")
    public Result del(@RequestParam Long id){
        return userService.del(id);
    }

    //修改密码
    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody UpdatePasswordRequest request){
        return userService.updatePassword(request);
    }

    //登录电脑端
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        return userService.login(user);
    }

    //登录移动端
    @PostMapping("/loginWx")
    public Result loginWx(@RequestBody User user){
        return userService.loginWx(user);
    }

    //修改
    @PostMapping("/mod")
    public boolean mod(@RequestBody User user){
        return userService.mod(user);
    }
    //新增或修改
    @PostMapping("/saveOrMod")
    public boolean saveOrMod(@RequestBody User user){
        return userService.saveOrMod(user);
    }
    //删除
    @GetMapping("/delete")
    public boolean delete(Integer id){
        return userService.delete(id);
    }
    //查询（模糊、匹配）
    @PostMapping("/listP")
    public List<User> listP(@RequestBody User user){
        return userService.listP(user);
    }

    //分页（自带的方法）
    @PostMapping("/listPage")
    public List<User> listPage(@RequestBody QueryPageParam query){
        return userService.listPage(query);
    }
    //自定义的分页
    @PostMapping("/listPageC")
//    public List<User> listPageC(@RequestBody HashMap map){
    public List<User> listPageC(@RequestBody QueryPageParam query){
        return userService.listPageC(query);
    }

    //测试后端返回给前端数据的封装
    @PostMapping("/listPageC1")
    public Result listPageC1(@RequestBody QueryPageParam query){
        return userService.listPageC1(query);
    }
    //获取未分配安装人员
    @GetMapping("/getInstallers")
    public Result getInstallers(){
        return userService.getInstallers();
    }
    //获取所有安装人员
    @GetMapping("/getAllInstallers")
    public Result getAllInstallers(){
        return userService.getAllInstallers();
    }

    //获取使用人员
    @GetMapping("/getUsers")
    public Result getUsers(){
        return userService.getUsers();
    }

    //获取安装人员安装设备量
    @PostMapping("/getInstallNum")
    public Result getInstallNum(@RequestBody ShowInstallDeviceNumRequest request){
        return userService.getInstallNum(request);
    }
}
