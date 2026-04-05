<template>
  <div class="container">
    <!-- 导航栏 -->
    <van-nav-bar
        title="绑定SIM卡"
        left-text="返回"
        left-arrow
        @click-left="onClickLeft"
    />
    <!--设备列表-->
    <div class="box" v-for="(item,index) in devices">
      <div class="box-1">
        设备ID:{{ item.id }}
      </div>
      <div class="box-1">
        设备名字:{{ item.deviceName }}
      </div>
      <div class="box-3">
        <!-- 允许输入数字，调起带符号的纯数字键盘 -->
        <van-field v-model="item.number" type="number" style="background-image:linear-gradient(to right, #e0eafc,#cfdef3);" label="SIM卡号：" />
      </div>
    </div>
    <div class="span-total">
      <van-button round type="info" @click="onBind">确认绑定</van-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "BindSimView",
  data(){
    return{
      devices:[],
      user:JSON.parse(sessionStorage.getItem('CurUser')),
      number:'',
      list:[],
    }
  },
  mounted() {
    let orderId = this.$route.params.orderId
    this.axios.get(this.$httpUrl+'/out-devices-for-sim/findByOrderId?orderId='+orderId)
        .then(res =>{
      if(res.data.code==200){
        this.devices = res.data.data
      }
    })
  },
  methods:{
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
    onBind(){
      this.list = []
      this.devices.forEach(device => {
        if(device.number !== '' && device.number != null){
          let element = {
            id:device.id,
            sim:device.number
          }
          this.list.push(element)
        }
      })
      if(this.devices.length != this.list.length){
        //提示
        this.$toast('请将所有设备绑定SIM卡号！')
        return
      }
      this.axios.post(this.$httpUrl+'/out-devices-for-sim/bind',this.list).then(res=>{
        if(res.data.code==200){
          this.$toast('绑定成功！')
          this.$router.back()
        }
      })
    },
  }
}
</script>

<style scoped>
.box{
  margin: 10px 20px 20px 20px;
  border-radius:20px;
  /*box-shadow:0px 0px 5px #ccc;*/
  border: darkgray 1px solid;
  background-image:linear-gradient(to right, #e0eafc,#cfdef3);
}
.box-1{
  padding: 20px;
  font-size: 30px;
  font-weight: bold;
}
.box-3{
  font-size: 30px;
  /*box-shadow:0px 0px 5px #ccc;*/
  border: darkgray 0.5px solid;
  margin: 10px 10px 10px 10px;
  background: #e3f9fd;
}
.span-total{
  margin:20px;
  text-align: center;
}
</style>