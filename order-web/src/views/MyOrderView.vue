<template>
<div class="container">
  <!-- 导航栏 -->
  <van-nav-bar
      title="我的订单列表"
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"
  />
  <!--订单列表-->
  <div class="box" v-for="(item,index) in orders">
    <div class="box-1">
      <van-tag type="success" mark size="medium" style="float: left">{{ item.orderStateStr }}</van-tag>
      <van-tag type="danger" size="medium" style="float: right" @click="toCancelOrder(item.id)"><van-icon name="revoke"/> 撤销</van-tag>
    </div>
    <div class="box-2">
      <van-cell style="background-image:linear-gradient(to right, #e0eafc,#cfdef3);" center title="订单ID" value="订单详情" :label="item.id" is-link @click="toOrderDetails(item)"/>
    </div>
    <div class="box-3">
      <span style="font-weight: bold;float: left">{{ item.createTime }}</span>
      <span style="font-weight: bold;float: right;color: #F2270C">￥<span>{{item.total}}</span></span>
    </div>
  </div>
  <!--支付页面-->
  <van-popup v-model="showToPay" round position="bottom" :style="{ height: '70%' }" >
    <div class="span-total">
      <span style="font-size: 40px;font-weight: bold">￥{{total}}</span>
    </div>
    <van-image
        width="10rem"
        height="10rem"
        fit="contain"
        :src="http+'/f/pay.jpg'"
    />
    <div class="span-total">
      <van-button round type="info" @click="onPaySuccess">确认交易</van-button>
    </div>
  </van-popup>
  <!--上传图片页面-->
  <van-popup v-model="showToUpload" round position="bottom" :style="{ height: '80%' }" >
    <div class="span-total span-title">
      <span style="font-size: 30px;font-weight: bold">请上传订单附件</span>
    </div>
    <div class="span-1">
      <span style="font-size: 20px;font-weight: bold">（1）请填写保单信息：</span>
    </div>
    <div class="span-total">
      <van-cell-group>
        <van-field v-model="userName" label="投保人：" placeholder="请输入投保人姓名" />
        <van-field v-model="cardNumber" label="身份证号：" placeholder="请输入投保人身份证号" />
        <van-field v-model="price" label="保障金额：" placeholder="请输入保障金额"/>
      </van-cell-group>
    </div>
    <div class="span-total span-title">
      <van-button round type="info" @click="onCreateInsurance">生成电子保单</van-button>
    </div>
    <div class="span-1">
      <span style="font-size: 20px;font-weight: bold;">（2）请上传账单截图：</span>
    </div>
    <div class="span-total">
      <van-uploader v-model="file1" :max-count="1" :after-read="afterRead2" :after-delete="afterDelete2"/>
    </div>
    <div class="span-total">
      <van-button round type="info" @click="onUpload">确认上传</van-button>
    </div>
  </van-popup>
</div>
</template>

<script>
export default {
  name: "MyOrderView",
  data(){
    return{
      total:0,
      http:this.$httpUrl,
      orders:[],
      showToPay:false,
      showToUpload:false,
      user:JSON.parse(sessionStorage.getItem('CurUser')),
      orderId:'',
      file1: [],
      file2: [],
      orderUrl:'',
      billUrl:'',
      label:'',
      userName:'',
      cardNumber:'',
      price:'',
    }
  },
  mounted() {
    this.axios.get(this.$httpUrl+'/order/findList?userId='+this.user.id)
        .then(res =>{
      this.orders = res.data.data
      this.orders.forEach(order=>{
        order.orderStateStr = this.getOrderState(order.orderState)
      })
    })
  },
  methods:{

    onCreateInsurance() {
      let element = {
        userName: this.userName,
        cardNumber: this.cardNumber,
        price: this.price,
      }
      if (this.userName == '' || this.cardNumber == '' || this.price == '') {
        this.$toast('提交内容不允许为空！')
        return
      }
      this.axios.post(this.$httpUrl + '/order/createInsurance',element).then(res => {
        if (res.data.code == 200) {
          this.orderUrl=res.data.data
          this.$toast('成功生成电子保单！')
        }
      })
    },
    toCancelOrder(orderId){
      this.$dialog.confirm({
        title: '温馨提示',
        message: '您确定要撤销此订单吗？',
      })
          .then(() => {
            // on confirm
            this.axios.get(this.$httpUrl+'/order/cancelOrder?orderId='+orderId).then(res=>{
               if(res.data.code == 200) {
                 //刷新订单列表
                 this.axios.get(this.$httpUrl + '/order/findList?userId=' + this.user.id).then(res => {
                   this.orders = res.data.data
                   this.orders.forEach(order => {
                     order.orderStateStr = this.getOrderState(order.orderState)
                   })
                 })
               }
            })
          })
          .catch(() => {
            // on cancel
          });
    },
    onUpload(){
      let data={
        orderId:this.orderId,
        orderState: 2,
        insuranceUrl:this.orderUrl,
        billUrl:this.billUrl,
      }
      if(this.orderUrl=='' || this.billUrl==''){
        this.$toast.fail('请生成电子保单并上传账单图片！')
        return
      }
      this.axios.post(this.$httpUrl+'/order/uploadUrl',data).then(res=>{
        if(res.data.code == 200){
          //刷新订单列表
          this.axios.get(this.$httpUrl+'/order/findList?userId='+this.user.id).then(res =>{
            this.orders = res.data.data
            this.orders.forEach(order=>{
              order.orderStateStr = this.getOrderState(order.orderState)
            })
            //关掉弹窗
            this.showToUpload=false
          })
        }
      })
    },
    afterDelete1(){
      this.orderUrl=''
    },
    afterDelete2(){
      this.billUrl=''
    },
    afterRead1(file) {
      // 此时可以自行将文件上传至服务器
      const data = new FormData();
      data.append("file", file.file);
      this.axios.post(this.$httpUrl+'/device/upload',data).then(res=>{
        if(res.data.code == 200){
          this.orderUrl = res.data.data
          this.$toast("已上传图片")
        }
      })
    },
    afterRead2(file){
      // 此时可以自行将文件上传至服务器
      const data = new FormData();
      data.append("file", file.file);
      this.axios.post(this.$httpUrl+'/device/upload',data).then(res=>{
        if(res.data.code == 200){
          this.billUrl = res.data.data
          this.$toast("已上传图片")
        }
      })
    },
    onPaySuccess(){
      let data = {
        orderState:1,
        orderId:this.orderId,
      }
      this.axios.post(this.$httpUrl + '/order/updateStateById',data).then(res => {
        if(res.data.code == 200) {
          this.$toast('订单已支付成功！')
          this.showToPay=false
          this.total=0
          //刷新订单列表
          this.axios.get(this.$httpUrl+'/order/findList?userId='+this.user.id).then(res =>{
            this.orders = res.data.data
            this.orders.forEach(order=>{
              order.orderStateStr = this.getOrderState(order.orderState)
            })
          })
        }
      })
    },
    getOrderState(state){
      if(state == 0){
        return'未支付'
      }else if(state == 1){
        return'已支付'
      }else if (state == 2){
        return'已上传'
      }else if (state == 3){
        return'已审核'
      }else if(state == 4){
        return'已出库'
      }else if(state == 5){
        return'已结束'
      }
    },
    onClickLeft() {
      this.$toast('返回')
      this.$router.back()
    },
    toOrderDetails(item){
      this.orderId = item.id
      if(item.orderState == 0){//未支付
        this.total = item.total
        this.showToPay=true
      }else if(item.orderState == 1){//已支付
        this.showToUpload=true
      }else if(item.orderState == 4){//已出库
        this.$router.push({
          name:'BindSimView',
          params:{
            orderId:item.id
          }
        })
      }
    }
  }
}
</script>

<style scoped>
.box{
  margin: 10px 20px 20px 20px;
  border-radius:20px;
  box-shadow:0px 0px 5px #ccc;
  border: darkgray 1px solid;
  /*background-image:linear-gradient(to right, #43c6ac,#f8ffae);*/
  background-image:linear-gradient(to right, #e0eafc,#cfdef3);
}
.box-1{
  padding: 20px;
}
.box-2{
  padding: 10px 10px 0px 10px;
  font-weight: bold;
  font-size: 30px;
}
.box-3{
  margin-bottom: 30px;
  padding: 10px 30px 30px 30px;
  font-size: 30px;
}
.span-total{
  margin:20px;
  text-align: center;
}
.span-1{
  text-align: left;
  margin-left: 30px;
}
.span-title{
  margin: 30px 0px;
}
</style>