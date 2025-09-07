package com.orderapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.orderapi.common.*;
import com.orderapi.entity.*;
import com.orderapi.enums.BillEnum;
import com.orderapi.enums.InstallerStateEnum;
import com.orderapi.enums.OrderEnum;
import com.orderapi.mapper.*;
import com.orderapi.vo.request.CreateInsuranceRequest;
import com.orderapi.vo.request.CreateOrderRequest;
import com.orderapi.vo.request.UpdateStateRequest;
import com.orderapi.vo.request.UploadUrlRequest;
import com.orderapi.vo.response.CartResponse;
import com.orderapi.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xyl
 * @since 2023-05-08
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private OrdersMapper orderMapper;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    @Lazy
    private BillService billService;

    @Autowired

    private OrderDetailsMapper orderDetailsMapper;

    @Autowired
    private OrderDetailsService orderDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OutDevicesForSimMapper outDevicesForSimMapper;

    @Autowired
    @Lazy
    private OutDevicesForSimService outDevicesForSimService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${file.uploadFolder}")
    private String uploadFolder;

    //逻辑路径
    @Value("${file.accessPath}")
    private String accessPath;

    private  static final String FONT_PATH="C:\\Windows\\Fonts\\simsun.ttc,0";

    @Override
    public IPage pageCC(IPage<Orders> page, Wrapper wrapper) {
        return orderMapper.pageCC(page,wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result createOrder(CreateOrderRequest request) {
        Orders order = new Orders();
        Long orderId = SnowFlakeIdWorker.nextId();
        Long billId = SnowFlakeIdWorker.nextId();
        //保存主订单数据
        order.setId(orderId);
        order.setUserId(request.getUserId());
        order.setInstallerId(request.getInstallerId());
        order.setBillId(billId);
        order.setTotal(request.getTotal());
        order.setOrderState(OrderEnum.NOT_PAID.getCode().toString());
        save(order);

        //保存订单详情表数据
        List<CartResponse> carts = request.getCartResponse();
        List<Long> ids = new ArrayList<>();
        carts.forEach(cart -> {
            ids.add(cart.getId());
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setId(SnowFlakeIdWorker.nextId());
            orderDetails.setDeviceId(cart.getDeviceResponse().getId());
            orderDetails.setOrderId(orderId);
            orderDetails.setNum(cart.getNum());
            orderDetailsService.save(orderDetails);
        });

        //保存账单数据
        Bill bill = new Bill();
        bill.setId(billId);
        bill.setOrderId(orderId);
        bill.setTotal(request.getTotal());
        bill.setUserId(request.getUserId());
        bill.setBillState(BillEnum.NOT_PAID.getCode().toString());
        billMapper.insert(bill);
        //修改安装人员状态为已分配
        User installer = userService.getById(request.getInstallerId());
        installer.setState(InstallerStateEnum.ASSIGNED.getCode());
        userService.updateById(installer);

        //删除购物车数据
        cartService.removeByIds(ids);

        return Result.suc(orderId.toString());
    }

    //根据订单的id修改订单的状态
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateStateById(UpdateStateRequest request) {
        Orders order = getById(request.getOrderId());
        if (Integer.valueOf(order.getOrderState()) >= request.getOrderState()) {
            return Result.fail();
        }
        order.setOrderState(request.getOrderState().toString());
        order.setUpdateTime(LocalDateTime.now());
        updateById(order);

        Bill bill = billMapper.selectById(order.getBillId());
        if (Integer.valueOf(bill.getBillState()) >= request.getOrderState()) {
            return Result.fail();
        }
        bill.setBillState(request.getOrderState().toString());
        bill.setUpdateTime(LocalDateTime.now());
        billService.updateById(bill);
        return  Result.suc();
    }

    @Override
    public Result findList(Long userId){
        LambdaQueryWrapper<Orders> wrapper = new QueryWrapper<Orders>().lambda()
                .eq(Orders::getUserId, userId).orderByDesc(Orders::getCreateTime);
        List<Orders> orders = orderMapper.selectList(wrapper);
        return Result.suc(orders);
    }

    //上传订单附件和电子保单
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result uploadUrl(UploadUrlRequest request) {
        Orders order = getById(request.getOrderId());
        if (Integer.valueOf(order.getOrderState()) >= request.getOrderState()) {
            return Result.fail();
        }
        order.setOrderState(request.getOrderState().toString());
        order.setInsuranceUrl(request.getInsuranceUrl());
        order.setUpdateTime(LocalDateTime.now());
        updateById(order);

        Bill bill = billMapper.selectById(order.getBillId());
        bill.setBillUrl(request.getBillUrl());
        bill.setUpdateTime(LocalDateTime.now());
        billService.updateById(bill);
        return  Result.suc();
    }

    //生成电子保单
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result createInsurance(CreateInsuranceRequest request) throws IOException {
        String uuid = UuidUtil.getUuid();
        PdfDocument pdfDocument = new PdfDocument(new PdfReader(uploadFolder+"电子保单.pdf"),
                new PdfWriter(uploadFolder+uuid+".pdf"));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDocument, false);
        PdfFont font = PdfFontFactory.createFont(FONT_PATH);
        DeviceRgb color = new DeviceRgb(0, 0, 0);
        //文本填充
        HashMap<String, String> map = new HashMap<>();
        map.put("userName",request.getUserName());
        String date = new SimpleDateFormat("yyyy年MM月dd日").format(new Date());
        map.put("startDate",date);
        map.put("type","身份证");
        map.put("number",request.getCardNumber());
        map.put("price",request.getPrice()+"元");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            form.getFormFields().get(entry.getKey()).setValue(entry.getValue()).setColor(color).setFont(font);
        }
        //清除表单域
        form.flattenFields();
        pdfDocument.close();
        return Result.suc(accessPath+uuid+".pdf");
    }


    @Override
    public Result listPage(QueryPageParam query){
        HashMap param = query.getParam();

        String orderId = (String)param.get("orderId");
        String billId = (String)param.get("billId");
        String userId = (String)param.get("userId");
        String installerId = (String)param.get("installerId");
        String orderState = (String)param.get("orderState");

        Page<Orders> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        QueryWrapper<Orders> queryWrapper = new QueryWrapper();

        if(StringUtils.isNotBlank(orderId) && !"null".equals(orderId)){
            queryWrapper.eq("orders.id",orderId);
        }
        if(StringUtils.isNotBlank(billId) && !"null".equals(billId)){
            queryWrapper.eq("orders.bill_id",billId);
        }
        if(StringUtils.isNotBlank(userId) && !"null".equals(userId)){
            queryWrapper.eq("orders.user_id",userId);
        }
        if(StringUtils.isNotBlank(installerId) && !"null".equals(installerId)){
            queryWrapper.eq("orders.installer_id",installerId);
        }
        if(StringUtils.isNotBlank(orderState) && !"null".equals(orderState)){
            queryWrapper.eq("orders.order_state",orderState);
        }

        IPage result = pageCC(page,queryWrapper);
        return Result.suc(result.getRecords(),result.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result outDevices(Long orderId, Long adminId) {
        Orders order = orderMapper.selectById(orderId);
        Bill bill = billService.getById(order.getBillId());
        if (!Integer.valueOf(order.getOrderState()).equals(OrderEnum.CHECKED.getCode())) {
            return Result.fail();
        }
        if (!Integer.valueOf(bill.getBillState()).equals(BillEnum.CHECKED.getCode())) {
            return Result.fail();
        }
        order.setOrderState(OrderEnum.OUTDEVICES.getCode().toString());
        order.setUpdateTime(LocalDateTime.now());

        LambdaQueryWrapper<OrderDetails> wrapper = new QueryWrapper<OrderDetails>().lambda()
                .eq(OrderDetails::getOrderId, orderId);
        List<OrderDetails> orderDetails = orderDetailsMapper.selectList(wrapper);
        orderDetails.forEach(details->{
            Integer num = details.getNum();
            //创建出库设备信息
            for(int i = 0;i<num;i++){
                OutDevicesForSim outDevicesForSim = new OutDevicesForSim();
                outDevicesForSim.setId(SnowFlakeIdWorker.nextId());
                outDevicesForSim.setDeviceId(details.getDeviceId());
                outDevicesForSim.setOrderId(orderId);
                outDevicesForSimService.save(outDevicesForSim);
            }
            //扣减库存
            Device device = deviceService.getById(details.getDeviceId());
            device.setStock(device.getStock()-num);

            deviceService.updateById(device);
            redisUtil.del(device.getId().toString());
            //添加记录
            Record record = new Record();
            record.setId(SnowFlakeIdWorker.nextId());
            record.setDeviceId(details.getDeviceId());
            record.setUserId(order.getUserId());
            record.setCount(-num);
            record.setAdminId(adminId);
            recordService.save(record);
        });
        updateById(order);
        return Result.suc();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result cancelOrder(Long orderId) {
        Orders order = orderMapper.selectById(orderId);
        //如果>=已出库状态则修改设备库存和删除出库设备
        if(Integer.valueOf(order.getOrderState()) >= OrderEnum.OUTDEVICES.getCode()){
            LambdaQueryWrapper<OrderDetails> wrapper = new QueryWrapper<OrderDetails>().lambda().eq(OrderDetails::getOrderId, orderId);
            List<OrderDetails> orderDetails = orderDetailsMapper.selectList(wrapper);
            orderDetails.forEach(detail -> {
                Integer num = detail.getNum();
                Device device = deviceService.getById(detail.getDeviceId());
                if (num > 0) {
                    device.setStock(device.getStock() + num);
                }
                deviceService.updateById(device);
                //添加入库记录
                Record record = new Record();
                record.setId(SnowFlakeIdWorker.nextId());
                record.setCount(num);
                record.setUserId(order.getUserId());
                record.setAdminId(order.getUserId());
                record.setDeviceId(detail.getDeviceId());
                recordService.save(record);
            });
            //删除出库设备
            LambdaQueryWrapper<OutDevicesForSim> wrapper3 = new QueryWrapper<OutDevicesForSim>().lambda().eq(OutDevicesForSim::getOrderId, orderId);
            outDevicesForSimMapper.delete(wrapper3);
        }
        //删除订单详情
        LambdaQueryWrapper<OrderDetails> wrapper1 = new QueryWrapper<OrderDetails>().lambda().eq(OrderDetails::getOrderId, orderId);
        orderDetailsMapper.delete(wrapper1);
        //删除订单
        removeById(orderId);
        //删除账单
        LambdaQueryWrapper<Bill> wrapper2 = new QueryWrapper<Bill>().lambda().eq(Bill::getOrderId, orderId);
        billMapper.delete(wrapper2);

        return Result.suc();
    }

    @Override
    public Result showPoint1() {
        LambdaQueryWrapper<Orders> wrapper = new QueryWrapper<Orders>().lambda().eq(Orders::getOrderState, OrderEnum.CHECKED.getCode());
        List<Orders> orders = orderMapper.selectList(wrapper);
        return Result.suc(orders.size());
    }
    @Override
    public Result showPoint2() {
        LambdaQueryWrapper<Orders> wrapper = new QueryWrapper<Orders>().lambda().eq(Orders::getOrderState, OrderEnum.UPLOADED.getCode());
        List<Orders> orders = orderMapper.selectList(wrapper);
        return Result.suc(orders.size());
    }

}
