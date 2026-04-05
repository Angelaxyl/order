/*
 * @Descripttion: 
 * @Version: 1.0
 * @Author: pj
 * @Date: 2023-04-22 10:48:18
 * @LastEditors: pj
 * @LastEditTime: 2023-04-22 10:49:05
 */
import service from "./service.js";
//封装相关数据请求

let link = (url, method = "GET", data, params) => {
    return new Promise((resolve, reject) => {
        service.request({
            url,
            method,
            data,
            params
        }).then((ok) => {
            resolve(ok)
        }).catch((err) => {
            reject(err)
        })
    })
}

export default link