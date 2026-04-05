<template>
<div>
  <!-- 导航栏 -->
  <van-nav-bar
      title="我的个人信息"
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"
  />
  <van-cell is-link @click="showPopup">修改个人信息</van-cell>
    <van-popup v-model="show" round position="bottom" :style="{ height: '40%' ,width : '100%' }">
      <van-cell-group>
        <van-field v-model="MyName" label="我的名字" placeholder="请输入名字" />
        <van-field v-model="MyAge" label="我的年龄" placeholder="请输入年龄" />
        <van-field name="MyGender" label="我的性别">
          <template #input>
            <van-radio-group v-model="MyGender" direction="horizontal">
              <van-radio name="1">男</van-radio>
              <van-radio name="0">女</van-radio>
            </van-radio-group>
          </template>
        </van-field>
        <!-- 输入手机号，调起手机号键盘 -->
        <van-field v-model="MyPhone" type="tel" label="我的手机号" placeholder="请输入手机号" />
      </van-cell-group>
      <div class="span-total">
        <van-button round type="info" @click="onUpdate()">确定</van-button>
      </div>
    </van-popup>
  <van-cell is-link @click="showPswPopup">修改我的密码</van-cell>
  <van-popup v-model="showPsw" round position="bottom" :style="{ height: '40%' ,width : '100%' }">
    <van-form @submit="onSubmit">
      <van-field
          v-model="originPass"
          type="password"
          name="原密码"
          label="原密码"
          placeholder="原密码"
          :rules="[{ required: true, message: '请填写原密码' }]"
      />
      <van-field
          v-model="pass"
          type="password"
          name="新密码"
          label="新密码"
          placeholder="新密码"
          :rules="[{ required: true, message: '请填写新密码' }]"
      />
      <van-field
          v-model="checkPass"
          type="password"
          name="确认密码"
          label="确认密码"
          placeholder="确认密码"
          :rules="[{ required: true, message: '请填写确认密码' }]"
      />
      <div style="margin: 16px;">
        <van-button round block type="info" native-type="submit">提交</van-button>
      </div>
    </van-form>
  </van-popup>
  <van-cell :value="user.name">
    <!-- 使用 title 插槽来自定义标题 -->
    <template #title >
      <span class="custom-title">我的名字</span>
      <van-tag type="primary">可修改</van-tag>
    </template>
  </van-cell>
  <van-cell :value="user.age">
    <!-- 使用 title 插槽来自定义标题 -->
    <template #title>
      <span class="custom-title">我的年龄</span>
      <van-tag type="primary">可修改</van-tag>
    </template>
  </van-cell>
  <van-cell :value="user.gender=='1'?'男':'女'">
    <!-- 使用 title 插槽来自定义标题 -->
    <template #title>
      <span class="custom-title">我的性别</span>
      <van-tag type="primary">可修改</van-tag>
    </template>
  </van-cell>
  <van-cell :value="user.phone">
    <!-- 使用 title 插槽来自定义标题 -->
    <template #title>
      <span class="custom-title">我的手机号</span>
      <van-tag type="primary">可修改</van-tag>
    </template>
  </van-cell>
</div>
</template>

<script>
export default {
  name: "MyInfoView",
  data() {
    return {
      user:JSON.parse(sessionStorage.getItem('CurUser')),
      MyName:'',
      MyAge:'',
      MyGender:'1',
      MyPhone:'',
      MyPassword:'',
      show: false,
      originPass:'',
      pass:'',
      checkPass:'',
      showPsw:false,
    }
  },
  methods: {
    resetForm1() {
      this.originPass='',
      this.pass='',
      this.checkPass=''
    },
    onSubmit() {
      let element={
        userId:this.user.id,
        originPass:this.originPass,
        pass:this.pass,
        checkPass:this.checkPass,
      }
      this.$axios.post(this.$httpUrl+'/user/updatePassword',element).then(res=>res.data).then(res=>{
        if(res.code==200) {
          this.$toast(res.msg)
          this.showPsw= false
          sessionStorage.setItem('CurUser',JSON.stringify(res.data))
          this.resetForm1()
        }else{
          this.$toast(res.msg)
        }
      })
    },
    onUpdate(){
      if(this.MyName==''||this.MyAge==''||this.MyGender==''||this.MyPhone==''||this.MyPassword==''){
        this.$toast('提交内容不允许为空！')
        return
      }
      let element={
        id:this.user.id,
        userName:this.user.userName,
        name:this.MyName,
        password:this.MyPassword,
        age:this.MyAge,
        gender:this.MyGender,
        phone:this.MyPhone,
        role:this.user.role,
        openId:this.user.openId,
      }
      this.axios.post(this.$httpUrl+'/user/update',element).then(res=> {
        if(res.data.code==200){
          this.user.name = this.MyName
          this.user.age = this.MyAge
          this.user.gender = this.MyGender
          this.user.phone = this.MyPhone
          this.user.password = this.MyPassword
          sessionStorage.setItem('CurUser',JSON.stringify(this.user))
          this.$toast('修改成功！')
          this.show = false
        }
      })
    },
    onClickLeft() {
      this.$toast('返回')
      this.$router.back()
    },
    showPopup() {
      this.MyName = this.user.name
      this.MyAge = this.user.age
      this.MyGender = this.user.gender+''
      this.MyPhone = this.user.phone
      this.MyPassword = this.user.password
      this.show = true
    },
    showPswPopup(){
      this.originPass='',
      this.pass='',
      this.checkPass='',
      this.showPsw = true
    }
    },
}
</script>

<style scoped>
.van-submit-bar {
  bottom: 40px;
}
.van-submit-bar__bar{
  justify-content: center;
}
.span-total{
  margin:20px;
  text-align: center;
}
.custom-title {
  margin-right: 4px;
  vertical-align: middle;
}
</style>