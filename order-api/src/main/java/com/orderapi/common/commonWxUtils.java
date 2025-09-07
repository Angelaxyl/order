package com.orderapi.common;

/**
 * @author: xueyalin
 * @description:
 * @date: 2023/05/10
 */

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

public class commonWxUtils {

    //微信工具服务类
    public static WxMpService  wxService=new WxMpServiceImpl();
    //注入token的配置参数
    /**
     * 生产环境 建议将WxMpInMemoryConfigStorage持久化
     */
    public static WxMpInMemoryConfigStorage wxConfigProvider=new WxMpInMemoryConfigStorage();

    public static String  access_token="";

    @PostConstruct
    public void init() {
        //微信公众号的appid
        wxConfigProvider.setAppId(CommonConst.WXAppId_test);
        //微信公众号的密钥
        wxConfigProvider.setSecret(CommonConst.WXAppSecret_test);

        //注入token值
        wxConfigProvider.setToken(CommonConst.WXToken);
        wxService.setWxMpConfigStorage(wxConfigProvider);
       /*
        try {
            access_token=wxService.getAccessToken();
       } catch (WxErrorException e) {
           e.printStackTrace();
       }
       */
        String appid= CommonConst.WXAppId_test;
        String app_secret=CommonConst.WXAppSecret_test;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appid", appid);
        map.put("grant_type","client_credential");
        map.put("secret", app_secret);
        String url = "https://api.weixin.qq.com/cgi-bin/token";//获取微信公众号ACCESS_TOKEN
        String ret = HttpUtil.post(url, map);
        //hutool
        JSONObject retJson=new JSONObject(ret);
        access_token=retJson.getStr("access_token");
        //weixinBusinessController.createMenu();
        System.out.println("initializing weixin provider access_token is :"+access_token);
    }


}
