<template>
  <div class="back">
    <div class="bg-blur"></div>
    <h1 class="text-title1">定位设备销售订单系统</h1>
    <div class="login">
      <el-form label-width="180px"
               label-paddind="0px 10px 0px 0px" class="demo-ruleForm" style="width: 250px">
        <div class="form-head">
          <h1 class="text-title">用户注册</h1>
        </div>
        <div class="box-1">
          <div class="box-2">
            <van-cell-group>
              <van-field v-model="userName" label="账号" placeholder="请输入账号" label-width="50px" />
            </van-cell-group>
          </div>
          <div class="box-2">
            <van-cell-group>
              <van-field v-model="password" type="password" label="密码" placeholder="请输入密码" label-width="50px" />
            </van-cell-group>
          </div>
        </div>
        <div class="btn-box">
          <el-button :disabled="this.openId == ''"  round class="login-btn" type="primary" @click="toRegister()">注册</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "RegisterView",
  data () {
    return {
      openId :'',
      code:this.$route.query.code,
      userName:'',
      password:'',
    };
  },
  methods: {
    toRegister(){

      let element={
        openId : this.openId,
        userName:this.userName,
        password:this.password,
        role:'1'
      }
      this.$axios.post(this.$httpUrl+'/user/save',element).then(res=>res.data).then(res=> {
        if (res.code == 200) {
          this.$toast('您已成功注册账号！')
          window.location.href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx44d52ee9514d55ce&redirect_uri=http://xueyalin.gz2vip.91tunnel.com/HomeView&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect"
        }else{
          this.$toast(res.msg)
        }
      })
    },
  },
mounted() {
    this.$axios.get(this.$httpUrl+'/wechat/publicAccount/getOpenId?code='+this.code).then(res=>res.data).then(res=>{
      this.openId = res
  });
}
};
</script>

<style scoped>
.text-title1{
  padding-top: 350px;
  text-align: center;
  font-size: 66px;
  font-family: serif;
  font-weight: bold;
  color: white;
}

.login {
  display: flex;
  justify-content: center;
  align-content: center;
  padding-top: 20px;
}

.demo-ruleForm {
  background-color: white;
  width: 300px;
  padding: 50px;
  /* margin-right: 500px; */
  border: 1px solid black;
  border-radius: 8%;
  /*设置透明度，0为完全透明，1为不透明*/
  opacity: 0.8;
}

.text-title {
  text-align: center;
  font-size:50px;
  font-family: serif;
  font-style: normal;
  margin-top: 0%;
  padding-bottom: 20px;
}

.box-1{
}

.back {
  height: 100%;
  width: 100%;
}

.bg-blur {
  height: 100%;
  width: 100%;
  /*渐变的背景色*/
  /*height: 100vh;
background-image: linear-gradient(to bottom right, #FC466B, #3F5EF8);
overflow: hidden;*/
  /*背景图*/
  background: url("../assets/register.jpg");
  position: fixed;
  background-size: 100% 100%;
  z-index: -1;

  /* filter: blur(1px); */
}

.login-btn {
  width: 50%;
  /* text-align: center; */
  /* border-radius: 10%; */
}

.btn-box {
  text-align: center;
  margin-bottom: 15px;
}
</style>