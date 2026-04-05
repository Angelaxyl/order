<template>
<div class="container">
  <div class="header">
    <!-- 导航栏 -->
    <van-nav-bar
        v-if="active=='/HomeView'"
        :title="title"
        @click-left="onClickLeft"
    />
    <van-nav-bar
        v-else
        :title="title"
        left-text="返回"
        left-arrow
        @click-left="onClickLeft"
    />
  </div>
  <div  class="main">
    <router-view/>
  </div>
  <!-- 标签栏 -->
  <div class="footer">
    <van-tabbar v-model="active" >
      <van-tabbar-item name="/HomeView" icon="home-o">首页</van-tabbar-item>
      <van-tabbar-item name="/CartView"  icon="shopping-cart-o" :badge="carts.length==0?'':carts.length">购物车</van-tabbar-item>
      <van-tabbar-item name="/MyView" icon="friends-o">我的</van-tabbar-item>
    </van-tabbar>
  </div>
</div>
</template>

<script>
export default {
  name: "IndexView",
  computed: {
    path(){
      return this.$route.path
    }
  },
  data() {
    return {
      carts:[],
      active: this.$route.path,
      title: '定位设备的销售订单系统',
    }
  },
  mounted() {
    //模拟登陆
    // let loginForm = {
    //   userName:'xxxy',
    //   password:'111111',
    //   role:'1'
    // }
    // this.$axios.post(this.$httpUrl+'/user/login',loginForm).then(res=>res.data).then(res=>{
    //   console.log(res)
    //   if(res.code==200){
    //     //存储
    //     sessionStorage.setItem("CurUser",JSON.stringify(res.data.user))
    //   }else{
    //     this.confirm_disabled=false;
    //     alert('校验失败，用户名或密码错误！');
    //     return false;
    //   }
    // });
    //真实登陆
    // if(sessionStorage.getItem('CurUser') ==''|| sessionStorage.getItem('CurUser')==undefined){
    //   let loginForm = {
    //     openId:this.openId
    //   }
    //   this.$axios.post(this.$httpUrl+'/user/loginWx',loginForm).then(res=>res.data).then(res=>{
    //     console.log(res)
    //     if(res.code==200){
    //       //存储
    //       sessionStorage.setItem("CurUser",JSON.stringify(res.data.user))
    //     }else{
    //       this.confirm_disabled=false;
    //       alert('校验失败，用户名或密码错误！');
    //       return false;
    //     }
    //   });
    // }

    let user = JSON.parse(sessionStorage.getItem('CurUser'))

    this.$axios.get(this.$httpUrl+'/cart/findCartByUserId?userId='+user.id).then(res=>{
      this.carts=res.data
      console.log(this.carts )
      sessionStorage.setItem("CurCarts",JSON.stringify(res.data))
    })
  },
  methods: {
    onClickLeft() {
      this.$toast('返回')
      this.$router.back()
    }
  },
  watch:{
    active(curVal,oldVal){
      this.$router.push(curVal);
      this.carts = JSON.parse(sessionStorage.getItem('CurCarts'))
      if(curVal == '/MyView'){
        this.title = '我的';
      }else  if(curVal == '/HomeView'){
        this.title = '定位设备的销售订单系统';
      }else  if(curVal == '/CartView'){
        this.title = '购物车';
      }else if(curVal == '/DetailsView'){
        this.title = '详情页面';
      }
    },
    path(curVal,oldVal){
      this.active = curVal
    }
  },
}
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
}

html, body {
  height: 100%;
}

.container {
  display: flex;
  height: 100%;
  flex-direction: column;
}

.header {
  height: auto;
}

.main {
  flex: 1;
  overflow: auto;
  margin-bottom: 100px;
}

.footer {
  height: auto;
}
</style>