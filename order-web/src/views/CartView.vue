<template>
  <div>
    <!-- vant空状态 -->
    <van-empty v-show="isShow" description="购物车目前还没有商品" image="https://img.yzcdn.cn/vant/custom-empty-image.png">
      <van-button round type="danger" class="bottom-button" @click="gotoHome">
        去购物
      </van-button>
    </van-empty>
    <!--购物车列表-->
    <div v-for="(item,index) in carts">
      <!-- vant复选框组 --> <!-- vant布局 --> <!-- vant滑动单元格 -->
      <!-- vant商品卡片 --> <!-- vant步进器 -->
      <van-checkbox-group v-model="result" @change="onChecked">
        <van-row style="background-color: white;">
          <van-col span="2" style="margin-top: 35px;">
            <van-checkbox :name="item"></van-checkbox>
          </van-col>
          <van-col span="22">
            <van-swipe-cell>
              <van-card
                  :price="item.deviceResponse.price"
                  :desc="item.deviceResponse.introduction"
                  :title="item.deviceResponse.deviceName"
                  :thumb="http +''+item.deviceResponse.urls[0]"
                  class="goods-card"
              >
                <template #num>
                  <van-stepper v-model="item.num" @change="onNum(item.id,item.num)"/>
                </template>
              </van-card>
              <template #right >
                <van-button @click="del(item,index)" square text="删除" type="danger" class="delete-button" />
              </template>
            </van-swipe-cell>
          </van-col>
        </van-row>
      </van-checkbox-group>
    </div>
    <!-- vant提交订单栏 -->
    <van-submit-bar is-link :price="total" button-text="提交订单" @submit="showPopup">
      <van-checkbox v-model="checkedAll" @click="onAll">全选</van-checkbox>
    </van-submit-bar>
    <!--vant选择安装人员的选择器-->
    <van-popup v-model="show" round position="bottom">
      <van-picker
          title="请选择安装人员"
          show-toolbar
          :columns="columns"
          @cancel="show = false"
          @confirm="onConfirm"
      />
    </van-popup>
    <!--支付页面-->
    <van-popup v-model="showToPay" round position="bottom" :style="{ height: '70%' }" >
      <div class="span-total">
        <span style="font-size: 40px;font-weight: bold">￥{{total/100}}</span>
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
  </div>
</template>

<script>
export default {
  data(){
    return{
      http:this.$httpUrl,
      carts:[], //添加到购物车的商品
      result:[], //所有复选框选中的商品
      checkedAll:false, //全选的状态
      total:0, //总价
      isShow:true,
      show: false,
      value: '',
      columns: [],
      installers:[],
      showToPay:false,
      orderId:'',
    }
  },
  created() {
    this.carts = JSON.parse(sessionStorage.getItem("CurCarts"))
    if(this.carts.length > 0){ //显示或隐藏空状态
      this.isShow = false
    }else{
      this.isShow = true
    }
  },
  methods:{
    onPaySuccess(){ //确认交易
      let data = {
        orderState:1,
        orderId:this.orderId,
      }
      this.axios.post(this.$httpUrl + '/order/updateStateById',data).then(res => {
            if(res.data.code == 200) {
              this.$toast('订单已支付成功！')
              this.showToPay=false
              this.total=0
            }
      })
    },
    onConfirm(value, index) { //选择器 确定安装人员后
      let data = {
        userId:JSON.parse(sessionStorage.getItem('CurUser')).id,
        installerId:this.installers[index].id,
        cartResponse:this.result,
        total:this.total/100,
      }
      this.axios.post(this.$httpUrl + '/order/createOrder',data).then(res=>{
        if(res.data.code == 200) {
          this.$toast.success(`您已成功创建订单！`);
          this.showToPay=true
          this.orderId= res.data.data
          //刷新购物车数据
          this.$axios.get(this.$httpUrl+'/cart/findCartByUserId?userId='+data.userId).then(res=>{
            this.carts=res.data
            sessionStorage.setItem("CurCarts",JSON.stringify(res.data))
            //关闭选择安装人员选择器
            this.show = false

            if(this.carts.length > 0){ //显示或隐藏空状态
              this.isShow = false
            }else{
              this.isShow = true
            }
          })
        }
      })
    },
    showPopup() { //点击提交订单后
      if(this.result.length == 0){
        this.$toast('请先选择您想要购买的设备')
        return;
      }
      this.axios.get(this.http + '/user/getInstallers').then(res=>{
        if(res.data.code == 200) {
          this.installers = res.data.data;
          this.columns=[]
          for(let installer of this.installers){
            this.columns.push(installer.name)
          }
        }
      })
      this.show = true
    },
    onNum(id,num){ //商品数量发生变化时触发
      let data={
        id:id,
        num:num
      }
      this.axios.post(this.$httpUrl + '/cart/updateCarts',data).then(res=>{
        if(res.data.code == 200){
          sessionStorage.setItem("CurCarts",JSON.stringify(this.carts))
          this.onChecked()
        }
      })
    },
    onChecked(){ //点击复选框选中商品设备并计算总价
      this.total = 0
      this.result.map(item=>{
        this.total += item.deviceResponse.price*100*item.num
      })
      if(this.result.length == this.carts.length){
        this.checkedAll = true
      }else{
        this.checkedAll = false
      }
    },
    onAll(){ //点击全选按钮
      if(this.checkedAll){ //如果为true的话全部选中，否则全不选
        this.result = this.carts
      }else{
        this.result = []
      }
    },
    del(item,index){ //删除
      if(this.result.includes(item)){
        this.$notify({ type: 'danger', message: '删除前请取消选中' });
      }else{
        this.axios.post(this.$httpUrl + '/cart/delCarts', {id:item.id}).then(res=> {
          if (res.data.code == 200) {
            this.carts.splice(index,1)
            sessionStorage.setItem("CurCarts", JSON.stringify(this.carts))
          }
        })
      }
      if(this.carts.length > 0){ //显示或隐藏空状态
        this.isShow = false
      }else{
        this.isShow = true
        this.checkedAll = false
      }
    },
    gotoHome(){ //跳转到Home页
      this.$router.push({
        path:'/HomeView'
      })
    }
  }
}
</script>

<style scoped="scoped">
.goods-card {
  margin: 0;
  background-color: white;
}
.delete-button {
  height: 100%;
}
.bottom-button {
  width: 180px;
  height: 40px;
}
.van-row{
  margin-left: 20px;
  width: 100%;
}
.span-total{
  margin:20px;
  text-align: center;
}
</style>
