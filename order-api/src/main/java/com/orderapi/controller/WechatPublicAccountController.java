package com.orderapi.controller;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.orderapi.common.CommonConst;
import com.orderapi.common.RedisUtil;
import com.orderapi.entity.InMessage;
import com.orderapi.entity.OutMessage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: xueyalin
 * @description:
 * @date: 2023/04/01
 */
@RestController
@RequestMapping("/wechat/publicAccount/")
public class WechatPublicAccountController {
    // 微信页面填写的token，必须保密
    private static final String TOKEN = "xyl";
    private static final String APPID = "wx8149ae1d0b67c56f";
    private static final String APPSECRET = "0e4d4155f4166c7d14b4fa5d0234e100";
//    private static final String TOKEN = "xyl";
//    private static final String APPID = "wx90114c78f334f9c2";
//    private static final String APPSECRET = "ea6e687e5280534d831011317e0849ee";

//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("validate")
    public String validate(String signature,String timestamp,String nonce,String echostr){
        // 1. 将token、timestamp、nonce三个参数进行字典序排序
        String[] arr = {timestamp, nonce, TOKEN};
        Arrays.sort(arr);
        // 2. 将三个参数字符串拼接成一个字符串进行sha1加密
        StringBuilder sb = new StringBuilder();
        for (String temp : arr) {
            sb.append(temp);
        }
        // 这里利用了hutool的加密工具类
        String sha1 = SecureUtil.sha1(sb.toString());
        // 3. 加密后的字符串与signature对比，如果相同则该请求来源于微信，原样返回echostr
        if (sha1.equals(signature)){
            return echostr;
        }
        // 接入失败
        return null;
    }

    @PostMapping(value = "validate", produces = "application/xml;charset=UTF-8")
    public Object handleMessage(@RequestBody InMessage inMessage){
        // 创建响应消息实体对象
        OutMessage outMessage = new OutMessage();
        // 把原来的接收方设置为发送方
        outMessage.setFromUserName(inMessage.getToUserName());
        // 把原来的发送方设置为接收方
        outMessage.setToUserName(inMessage.getFromUserName());
        // 设置消息类型
        outMessage.setMsgType(inMessage.getMsgType());
        // 设置消息时间
        outMessage.setCreateTime(System.currentTimeMillis() / 1000);
        // 根据接收到消息类型，响应对应的消息内容
        if ("text".equals(inMessage.getMsgType())){
            // 根据不同的关键字回复消息
            String inContent = inMessage.getContent();
            if (inContent.contains("你好")){
                outMessage.setContent("你好，欢迎关注");
            }else if (inContent.contains("动漫")){
                outMessage.setContent("进击的巨人");
            }else if (inContent.contains("注册")){
                outMessage.setContent("https://pxxwglwt.asse.devtunnels.ms:8080/RegisterView?openId="+inMessage.getFromUserName());
            }else if (inContent.contains("进入系统")){
                outMessage.setContent("https://pxxwglwt.asse.devtunnels.ms:8080/HomeView?openId="+inMessage.getFromUserName());
            }else {
                outMessage.setContent(inContent);
            }
        }else if ("image".equals(inMessage.getMsgType())){
            // 图片
            outMessage.setMediaId(new String[]{inMessage.getMediaId()});
        } else if ("event".equals(inMessage.getMsgType())) {
            // 获取推送的事件类型
            String event = inMessage.getEvent();
            //如果是关注事件
            if ( "subscribe".equals(event)){
                //回复
                outMessage.setMsgType( "text" );
                outMessage.setContent("欢迎关注定位设备销售订单系统！\n"+
                        "温馨提示：\n" +
                        "若您是新用户，请按以下步骤进行操作:\n" +
                        "1.点击注册按钮，到注册页面注册一个账号\n"+
                        "2.自动登录进系统后，请您先在我的页面完善个人信息后，再下订单哦");
            }
            if("click".equals(event.toLowerCase())){
                System.out.println(inMessage);
                if(inMessage.getEventKey().equals("Register")){
                    outMessage.setMsgType( "text" );
                    outMessage.setContent("https://pxxwglwt.asse.devtunnels.ms:8080/RegisterView?openId="+inMessage.getFromUserName());
                    return outMessage;
                }else if(inMessage.getEventKey().equals("System")){
                    outMessage.setMsgType( "text" );
                    outMessage.setContent("https://pxxwglwt.asse.devtunnels.ms:8080/HomeView?openId="+inMessage.getFromUserName());
                    return outMessage;
                }
            }

        }
        return outMessage;
    }

    @GetMapping("getAccessToken")
    public String getAccessToken(){
        String access_token = (String) redisUtil.get("access_token");
        if(!StringUtils.isEmpty(access_token)){
            return access_token;
        }
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + APPID +
                "&secret=" + APPSECRET;
        // 利用hutool的http工具类请求获取access_token
        String result = HttpUtil.get(url);
        // 将结果解析为json
        JSONObject jsonObject = JSONUtil.parseObj(result);
        // 获取access_token
        String accessToken = jsonObject.getStr("access_token");
        if (!StringUtils.isEmpty(accessToken)){
            // 将access_token存入redis
            redisUtil.setex("access_token", accessToken,60*60);
        }
        return accessToken;
    }

    @GetMapping("createMenu")
    public String createMenu(){
        // 从redis中取出access_token
        String accessToken = (String) redisUtil.get("access_token");
        String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + accessToken;
        // 创建菜单的请求体
//        String body = "{\n" +
//                "    \"button\":[\n" +
//                "        {\n" +
//                "            \"type\":\"click\",\n" +
//                "            \"name\":\"注册\",\n" +
//                "            \"key\":\"Register\"\n" +
//                "        },\n" +
//                "        {\t\n" +
//                "               \"type\":\"click\",\n" +
//                "               \"name\":\"进入系统\",\n" +
//                "               \"key\":\"System\"\n" +
//                "        }\n" +
//                "    ]\n" +
//                "}";
        String body = "{\n" +
                "    \"button\":[\n" +
                "        {\n" +
                "            \"type\":\"view\",\n" +
                "            \"name\":\"注册\",\n" +
                "            \"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx8149ae1d0b67c56f&redirect_uri=https://pxxwglwt.asse.devtunnels.ms:8080/RegisterView&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect\"\n" +
                "        },\n" +
                "        {\t\n" +
                "               \"type\":\"view\",\n" +
                "               \"name\":\"进入系统\",\n" +
                "               \"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx8149ae1d0b67c56f&redirect_uri=https://pxxwglwt.asse.devtunnels.ms:8080/HomeView&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
       return HttpUtil.post(url, body);
    }

    @GetMapping("getOpenId")
    public String getOpenID(String code){
//        String appid= CommonConst.WXAppId;
//        String app_secret=CommonConst.WXAppSecret;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appid", APPID);
        map.put("grant_type","authorization_code");
        map.put("secret", APPSECRET);
        map.put("code", code);
        String url = " https://api.weixin.qq.com/sns/oauth2/access_token";//获取微信公众号ACCESS_TOKEN
        String ret = HttpUtil.post(url, map);
        //hutool
        JSONObject retJson=new JSONObject(ret);
        String access_token=retJson.getStr("access_token");
        String openid = retJson.getStr("openid");
        return openid;
    }

    @GetMapping("getAccessTokenUserInfo")
    public String getAccessTokenUserInfo(@RequestParam("code") String code){
        System.out.println(code);
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+APPID+"&secret="+APPSECRET+"&code="+code+"&grant_type=authorization_code";
        // 利用hutool的http工具类请求获取access_token
        String result = HttpUtil.get(url);
        // 将结果解析为json
        JSONObject jsonObject = JSONUtil.parseObj(result);
        // 获取access_token
        String accessToken = jsonObject.getStr("access_token");
        String openid = jsonObject.getStr("openid");

        String url1 = "https://api.weixin.qq.com/sns/userinfo?access_token="+accessToken+"&openid="+openid+"&lang=zh_CN";
        String result1 = HttpUtil.get(url1);
        JSONObject jsonObject1 = JSONUtil.parseObj(result1);
        String headimgurl = jsonObject1.getStr("headimgurl");

        System.out.println(jsonObject);
        return openid+headimgurl;
    }
}

