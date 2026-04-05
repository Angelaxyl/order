<template>
    <div>
        <div class="detailss">
            <!-- vant轮播 -->
            <div style="background-color: #ffffff; border-radius: 0 0 15px 15px; margin-bottom: 15px">
                <van-swipe class="my-swipe" indicator-color="white" :autoplay="3000">
                    <van-swipe-item v-for="(item, index) in details.urls">
                        <img :src="http + item" width="100%" />
                    </van-swipe-item>
                </van-swipe>
                <div style="padding: 10px 10px">
                    <div style="display: flex; justify-content: space-between">
                        <span style="color: #f2270c; font-size: 15px">
                            ￥
                            <span style="font-size: 30px">{{ details.price }}</span>
                        </span>
                    </div>
                    <div>
                        <span style="font-size: 20px; font-weight: bold">设备名字：{{ details.deviceName }}</span>
                        <br />
                        <span style="font-size: 20px; font-weight: bold">设备型号：{{ details.deviceModel }}</span>
                    </div>
                </div>
            </div>
            <van-divider content-position="left">详情介绍</van-divider>
            <div>
                <span class="el-icon-message-solid" style="float: left">{{ details.introduction }}</span>
            </div>
            <div style="height: 50px"></div>
        </div>
        <!-- vant商品导航 -->
        <van-goods-action class="goods-action">
            <van-goods-action-icon icon="chat-o" text="客服" dot @click="contactService" />
            <van-goods-action-icon
                icon="cart-o"
                text="购物车"
                :badge="carts.length == 0 ? '' : carts.length"
                @click="toCarts" />
            <van-goods-action-icon icon="shop-o" text="店铺" />
            <van-goods-action-button @click="addCart" type="warning" text="加入购物车" />
            <van-goods-action-button type="danger" text="立即购买" />
        </van-goods-action>
    </div>
</template>

<script>
import Vue from 'vue'
import { Toast, Dialog } from 'vant'
Vue.use(Toast)
Vue.use(Dialog)
export default {
    data() {
        return {
            http: this.$httpUrl,
            details: {},
            carts: [] //购物车商品
        }
    },
    created() {
        this.carts = JSON.parse(sessionStorage.getItem('CurCarts'))
    },
    mounted() {
        let id = this.$route.params.id
        if (!id) {
            id = sessionStorage.getItem('CurDetailsId')
        }
        sessionStorage.setItem('CurDetailsId', id)
        this.$axios.get(this.$httpUrl + '/device/findById?id=' + id).then(res => {
            this.details = res.data
        })
    },
    methods: {
        toCarts() {
            //跳转购物车页面
            this.$router.push({
                path: '/CartView',
                active: '/CartView'
            })
        },
        addCart() {
            //添加到购物车
            let user = JSON.parse(sessionStorage.getItem('CurUser'))
            let element = {
                userId: user.id,
                deviceId: this.details.id,
                num: '1'
            }
            this.axios
                .post(this.$httpUrl + '/cart/addCart', element)
                .then(res => res.data)
                .then(res => {
                    if (res.code == 200) {
                        Toast.success('加入购物车成功')
                        this.$axios.get(this.$httpUrl + '/cart/findCartByUserId?userId=' + user.id).then(res => {
                            this.carts = res.data
                            sessionStorage.setItem('CurCarts', JSON.stringify(res.data))
                            this.$router.push({
                                path: '/CartView',
                                active: '/CartView'
                            })
                        })
                    }
                })
        },
        contactService() {
            //联系客服
            Dialog.alert({
                title: '联系客服',
                message: `
          <div style="text-align: left; padding: 10px;">
            <p style="margin: 10px 0;"><strong>客服电话：</strong><a href="tel:400-123-4567" style="color: #1989fa;">400-123-4567</a></p>
            <p style="margin: 10px 0;"><strong>客服QQ：</strong>123456789</p>
            <p style="margin: 10px 0;"><strong>客服微信：</strong>service123</p>
            <p style="margin: 10px 0; color: #969799; font-size: 12px;">工作时间：周一至周五 9:00-18:00</p>
          </div>
        `,
                allowHtml: true,
                confirmButtonText: '知道了'
            }).then(() => {
                // 用户点击确认后的回调
            })
        }
    }
}
</script>

<style>
.el-icon-message-solid {
    line-height: 60px;
    font-size: 30px;
}
.goods-action {
    margin-bottom: 90px;
}
.detailss {
    height: auto;
    overflow-y: auto;
    margin-bottom: 150px;
}
</style>
