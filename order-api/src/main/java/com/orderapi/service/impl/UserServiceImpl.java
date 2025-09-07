package com.orderapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.orderapi.common.*;
import com.orderapi.entity.Menu;
import com.orderapi.entity.Orders;
import com.orderapi.entity.OutDevicesForSim;
import com.orderapi.entity.User;
import com.orderapi.enums.InstallerStateEnum;
import com.orderapi.enums.OrderEnum;
import com.orderapi.enums.RoleEnum;
import com.orderapi.exception.GlobalException;
import com.orderapi.mapper.OrdersMapper;
import com.orderapi.mapper.OutDevicesForSimMapper;
import com.orderapi.mapper.UserMapper;
import com.orderapi.vo.request.ShowInstallDeviceNumRequest;
import com.orderapi.vo.request.UpdatePasswordRequest;
import com.orderapi.vo.response.ShowInstallDeviceNumResponse;
import com.orderapi.service.MenuService;
import com.orderapi.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.orderapi.enums.GlobalExceptionEnum.*;
import static com.orderapi.enums.GlobalExceptionEnum.PASSWORD_ERROR;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xyl
 * @since 2023-04-24
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;
    @Autowired
    private MenuService menuService;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OutDevicesForSimMapper outDevicesForSimMapper;

    @Override
    public IPage pageC(IPage<User> page) {
        return userMapper.pageC(page);
    }

    @Override
    public IPage pageCC(IPage<User> page, Wrapper wrapper) {
        return userMapper.pageCC(page,wrapper);
    }

    @Override
    public List<User> list(){
        return list();
    }

    @Override
    public Result findByNo(String userName){
        List list = lambdaQuery().eq(User::getUserName, userName).list();
        return list.size()>0?Result.suc(list):Result.fail();
    }
    //新增
    @Override
    public Result saveUser(User user){
        if(Objects.nonNull(user.getOpenId())){
            List<User> list = lambdaQuery().eq(User::getOpenId, user.getOpenId()).list();
            if(list.size()>0){
                throw new GlobalException(WX_OPENID_REPEAT.getCode(),WX_OPENID_REPEAT.getMsg());
            }
        }
        List list = lambdaQuery().eq(User::getUserName, user.getUserName()).list();
        if(list.size()>0){
            throw new GlobalException(USERNAME_REPEAT.getCode(),USERNAME_REPEAT.getMsg());
        }
        user.setId(SnowFlakeIdWorker.nextId());
        user.setSalt(UuidUtil.getUuid());
        user.setPassword(Md5Util.getMD5Pwd(user.getPassword(),user.getSalt()));
        if(user.getRole().equals(RoleEnum.INSTALLER.getCode())){
            user.setState(InstallerStateEnum.UNASSIGNED.getCode());
        }
        return save(user)?Result.suc():Result.fail();
    }
    //更新
    @Override
    public Result update(User user){
        User user1 = getById(user.getId());
        user.setPassword(user1.getPassword());
        return updateById(user)?Result.suc(JwtUtil.createJWT(user)):Result.fail();
    }
    //删除
    @Override
    public Result del(Long id){
        return removeById(id)?Result.suc():Result.fail();
    }

    //修改密码
    @Override
    public Result updatePassword(UpdatePasswordRequest request){
        if(!request.getPass().equals(request.getCheckPass())){
            throw new GlobalException(PASSWORD_ERR.getCode(),PASSWORD_ERR.getMsg());
        }
        User user = getById(request.getUserId());
        if(!user.getPassword().equals(Md5Util.getMD5Pwd(request.getOriginPass(), user.getSalt()))){
            throw new GlobalException(PASSWORD_ERROR.getCode(),PASSWORD_ERROR.getMsg());
        }
        user.setPassword(Md5Util.getMD5Pwd(request.getPass(), user.getSalt()));
        updateById(user);
        return Result.suc(user);
    }

    //登录电脑端
    @Override
    public Result login(User user){
        List<User> list = lambdaQuery()
                .eq(User::getUserName,user.getUserName()).list();
        //正确登录
        if(list.size() == 0){
            throw new GlobalException(USER_EMPTY.getCode(),USER_EMPTY.getMsg());
        }
        User user1 = list.get(0);
        if(user1.getPassword().equals(Md5Util.getMD5Pwd(user.getPassword(),user1.getSalt()))){
            List menuList = menuService.lambdaQuery()
                    .like(Menu::getMenuRight, user1.getRole()).orderByAsc(Menu::getId).list();
            HashMap res = new HashMap();
            res.put("user",user1);
            res.put("menu",menuList);
            return Result.suc(res);
        }else{
            throw new GlobalException(PASSWORD_ERROR.getCode(),PASSWORD_ERROR.getMsg());
        }
    }

    //登录移动端
    @Override
    public Result loginWx(User user){
        List list = lambdaQuery()
                .eq(User::getOpenId,user.getOpenId()).list();
        //正确登录
        if(list.size()>0){
            return Result.suc((User)list.get(0));
        }
        return Result.fail();
    }

    //修改
    @Override
    public boolean mod(@RequestBody User user){
        return updateById(user);
    }
    //新增或修改
    @Override
    public boolean saveOrMod(@RequestBody User user){
        return saveOrUpdate(user);
    }
    //删除
    @Override
    public boolean delete(Integer id){
        return removeById(id);
    }
    //查询（模糊、匹配）
    @Override
    public List<User> listP(@RequestBody User user){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(User::getName,user.getName());
        return list(lambdaQueryWrapper);
//        return Result.suc(list(lambdaQueryWrapper));
    }

    //分页（自带的方法）
    @Override
    public List<User> listPage(QueryPageParam query){
        HashMap param = query.getParam();
        String name = (String)param.get("name");

        Page<User> page = new Page();
        //当前第几页
        page.setCurrent(query.getPageNum());
        //每页多少条记录
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(User::getName,name);
        IPage result = page(page,lambdaQueryWrapper);
        System.out.println("total=="+result.getTotal());

        return result.getRecords();
    }
    //自定义的分页
    @Override
//    public List<User> listPageC(@RequestBody HashMap map){
    public List<User> listPageC(QueryPageParam query){


        HashMap param = query.getParam();
        String name = (String)param.get("name");

        Page<User> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(User::getName,name);

//        IPage result = pageC(page);
        IPage result = pageCC(page,lambdaQueryWrapper);
        System.out.println("total=="+result.getTotal());
        return result.getRecords();
    }

    //测试后端返回给前端数据的封装
    @Override
    public Result listPageC1(QueryPageParam query){
        HashMap param = query.getParam();
        String name = (String)param.get("name");
        String gender = (String)param.get("gender");
        String role = (String)param.get("role");
        String state = (String)param.get("state");

        Page<User> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            lambdaQueryWrapper.like(User::getName,name);
        }
        if(StringUtils.isNotBlank(gender)){
            lambdaQueryWrapper.eq(User::getGender,gender);
        }
        if(StringUtils.isNotBlank(role)){
            lambdaQueryWrapper.eq(User::getRole,role);
        }
        if(StringUtils.isNotBlank(state)){
            lambdaQueryWrapper.eq(User::getState,state);
        }

        IPage result = pageCC(page,lambdaQueryWrapper);
        System.out.println("total=="+result.getTotal());

        return Result.suc(result.getRecords(),result.getTotal());
    }
    //获取未分配安装人员
    @Override
    public Result getInstallers(){
        LambdaQueryWrapper<User> wrapper = new QueryWrapper<User>().lambda()
                .eq(User::getRole, RoleEnum.INSTALLER.getCode())
                .eq(User::getState, InstallerStateEnum.UNASSIGNED.getCode());
        List<User> list = list(wrapper);
        return Result.suc(list);
    }
    //获取所有安装人员
    @Override
    public Result getAllInstallers(){
        LambdaQueryWrapper<User> wrapper = new QueryWrapper<User>().lambda().eq(User::getRole, RoleEnum.INSTALLER.getCode());
        List<User> list = list(wrapper);
        return Result.suc(list);
    }

    //获取使用人员
    @Override
    public Result getUsers(){
        LambdaQueryWrapper<User> wrapper = new QueryWrapper<User>().lambda().eq(User::getRole, RoleEnum.USER.getCode());
        List<User> list = list(wrapper);
        return Result.suc(list);
    }

    @Override
    public Result getInstallNum(ShowInstallDeviceNumRequest request) {
        List<ShowInstallDeviceNumResponse> responses = Lists.newArrayList();
        List<User> installers = userMapper.selectList(new QueryWrapper<User>().lambda()
                .eq(User::getRole, RoleEnum.INSTALLER.getCode()));
        installers.forEach(installer->{
            ShowInstallDeviceNumResponse response = new ShowInstallDeviceNumResponse();

            LambdaQueryWrapper<Orders> wrapper = new QueryWrapper<Orders>().lambda()
                    .eq(Orders::getInstallerId, installer.getId())
                    .eq(Orders::getOrderState, OrderEnum.ENDED.getCode());
            if(Objects.nonNull(request.getStartDate())&&Objects.nonNull(request.getEndDate())){
                wrapper.between(Orders::getUpdateTime,request.getStartDate(),request.getEndDate());
            }
            List<Orders> orders = ordersMapper.selectList(wrapper);
            if(CollectionUtils.isEmpty(orders)){
                response.setUserName(installer.getUserName());
                response.setName(installer.getName());
                response.setNum(0);
                responses.add(response);
                return;
            }
            List<Long> orderIds = orders.stream().map(Orders::getId).collect(Collectors.toList());
            LambdaQueryWrapper<OutDevicesForSim> in = new QueryWrapper<OutDevicesForSim>().lambda()
                    .in(OutDevicesForSim::getOrderId, orderIds);
            Integer num = outDevicesForSimMapper.selectCount(in);
            response.setUserName(installer.getUserName());
            response.setName(installer.getName());
            response.setNum(num);
            responses.add(response);
        });

        return Result.suc(responses);
    }

}
