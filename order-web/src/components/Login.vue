<template>
    <div class="back">
        <div class="bg-blur"></div>
        <h1 class="text-title1">定位设备销售订单系统</h1>
        <div class="login">
            <el-form :model="loginForm"  :rules="rules" ref="loginForm" label-width="80px"
                label-paddind="0px 10px 0px 0px" class="demo-ruleForm">
                <div class="form-head">
                    <h2 class="text-title">登  录</h2>
                </div>
                <div class="form-centre">
                    <div class="input-group">
                        <div class="input">
                            <el-form-item label="账号：" prop="userName" class="input-right-box">
                                <el-input type="text" v-model="loginForm.userName" autocomplete="off"
                                          suffix-icon="iconfont icon-user-fill" placeholder="请输入账号"></el-input>
                            </el-form-item>
                        </div>
                    </div>
                    <div class="input-group">
                        <div class="input">
                            <el-form-item label="密码：" prop="password" class="input-right-box">
                                <el-input type="password" v-model="loginForm.password" autocomplete="off" show-password
                                          suffix-icon="iconfont icon-password" placeholder="请输入密码"  @keyup.enter.native="confirm"></el-input>
                            </el-form-item>
                        </div>
                    </div>
                </div>
                <div class="btn-box">
                    <el-button round class="login-btn hvr-float-shadow" type="primary"
                               @click="confirm" :disabled="confirm_disabled">登录</el-button>
                </div>
            </el-form>
        </div>
    </div>
</template>

<script>
    export default {
        name: "login",
        data () {
            var validateUserName = (rule, value, callback) => {
                if (value === "") {
                    callback(new Error("请输入账号"));
                } else {
                    callback();
                }
            };
            var validatePassword = (rule, value, callback) => {
                if (value === "") {
                    callback(new Error("请输入密码"));
                } else {
                    callback();
                }
            };
            return {
              confirm_disabled:false,
              loginForm: {  //初始化
                    userName: "",
                    password: "",
                },
                rules: {  //验证
                    userName: [
                        { validator: validateUserName, trigger: "blur" },
                        { min: 3, max: 8, message: '长度在 3 到 8 个字符', trigger: 'blur' }
                    ],
                    password: [
                        { validator: validatePassword, trigger: "blur" },
                        { min: 6, max: 18, message: '长度在 6 到 18 个字符', trigger: 'blur' }
                    ],
                },
            };
        },
        methods: {
          confirm(){
            this.confirm_disabled=true;
            this.$refs.loginForm.validate((valid) => {
              if (valid) { //valid成功为true，失败为false
                //去后台验证账号密码
                this.$axios.post(this.$httpUrl+'/user/login',this.loginForm).then(res=>res.data).then(res=>{
                  if(res.code==200){
                    //存储
                    sessionStorage.setItem("CurUser",JSON.stringify(res.data.user))
                    // localStorage.setItem("token",res.data.token)
                    this.$store.commit("setMenu",res.data.menu)
                    //跳转到主页
                    this.$router.replace('/Home');
                  }else{
                    this.confirm_disabled=false;
                    alert(res.msg);
                    return false;
                  }
                });
              } else {
                this.confirm_disabled=false;
                console.log('校验失败');
                return false;
              }
            });
          },
        },
    };
</script>

<style scoped>
.text-title1{
  padding-top: 120px;
  text-align: center;
  font-size: 50px;
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
        font-family: serif;
        font-style: normal;
        margin-top: 0%;
        padding-bottom: 20px;
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
        background: url("../assets/loginpage.png");
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

    .form-bottom {
        display: flex;
        justify-content: space-between;
    }

    .input-group {
        display: flex;
    }


    .input-right-box {
        text-align: center;
        padding: 0%;
    }

    .el-form-item__content {
        margin-left: 0%;
    }


    a {
        color: rgba(16, 16, 16, 0.481);
        text-decoration: none;
    }

    /* 鼠标移动到链接上 */
    a:hover {
        color: rgba(0, 0, 0, 0.903);
    }

    /* 选定的链接 */
    a:active {
        color: rgb(46, 55, 183);
    }

    /* Float Shadow */
.hvr-float-shadow {
  display: inline-block;
  vertical-align: middle;
  -webkit-transform: perspective(1px) translateZ(0);
  transform: perspective(1px) translateZ(0);
  box-shadow: 0 0 1px rgba(0, 0, 0, 0);
  position: relative;
  -webkit-transition-duration: 0.3s;
  transition-duration: 0.3s;
  -webkit-transition-property: transform;
  transition-property: transform;
}

.hvr-float-shadow:before {
  pointer-events: none;
  position: absolute;
  z-index: -1;
  content: '';
  top: 100%;
  left: 5%;
  height: 10px;
  width: 90%;
  opacity: 0;
  background: -webkit-radial-gradient(center, ellipse, rgba(0, 0, 0, 0.35) 0%, rgba(0, 0, 0, 0) 80%);
  background: radial-gradient(ellipse at center, rgba(0, 0, 0, 0.35) 0%, rgba(0, 0, 0, 0) 80%);
  /* W3C */
  -webkit-transition-duration: 0.3s;
  transition-duration: 0.3s;
  -webkit-transition-property: transform, opacity;
  transition-property: transform, opacity;
}
.hvr-float-shadow:hover, .hvr-float-shadow:focus, .hvr-float-shadow:active {
  -webkit-transform: translateY(-5px);
  transform: translateY(-5px);
  /* move the element up by 5px */
}
.hvr-float-shadow:hover:before, .hvr-float-shadow:focus:before, .hvr-float-shadow:active:before {
  opacity: 1;
  -webkit-transform: translateY(5px);
  transform: translateY(5px);
  /* move the element down by 5px (it will stay in place because it's attached to the element that also moves up 5px) */
}
</style>