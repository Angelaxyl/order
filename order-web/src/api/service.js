/*
 * @Descripttion: 
 * @Version: 1.0
 * @Author: pj
 * @Date: 2023-04-22 10:47:44
 * @LastEditors: pj
 * @LastEditTime: 2023-04-22 10:47:44
 */
import axios from "axios";

//创建axios实例
const service = axios.create();
// 添加请求拦截器
service.interceptors.request.use(function (config) {
    config.headers["Content-Type"] = "application/json;charset=UTF-8";
    // 在发送请求之前做些什么
    if (localStorage.getItem("token") !== "") {
        config.headers["token"] = localStorage.getItem("token")
    }
    return config;
}, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
});

// 添加响应拦截器
service.interceptors.response.use(function (response) {
    // 对响应数据做点什么
    return response;
}, function (error) {
    // 对响应错误做点什么
    return Promise.reject(error);
});

export default service