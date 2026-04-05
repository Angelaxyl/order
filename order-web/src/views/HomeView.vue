<template>
  <div>
    <!-- vant骨架屏 -->
    <van-skeleton v-for="i in 18" title :row="3"
                  :loading="list.length<=0" style="background-color: white;"/>
    <div>
      <!-- 商品卡片 -->
      <van-card
          v-for="(item,index) in list"
          :title="item.deviceName"
          :desc="item.introduction"
          :thumb="http +''+item.urls[0]"
          :price="item.price"
          @click="toDetails(item.id)">
        <template #num>
          <span>库存：{{item.stock}}</span>
        </template>
      </van-card>
    </div>
  </div>
</template>

<script>
export default{
  data(){
    return{
      // openId:this.$route.query.openId,
      openId:'oK6-L6CwrUJ-Vsv1Ecgzj15TguG8',
      code:this.$route.query.code,
      carts:[],
      http:this.$httpUrl,
      kw:'',
      list:[],
    }
  },
   mounted() {
     // 真实登录
     let user = JSON.parse(sessionStorage.getItem('CurUser'))
     if(!user){

       获取openId
       this.$axios.get(this.$httpUrl+'/wechat/publicAccount/getAccessTokenUserInfo?code='+this.code)
           .then(res=>{
         this.openId = res.data.substring(0,28)
         let headimgurl = res.data.substring(28)
         sessionStorage.setItem('headimgurl',headimgurl)
         let loginForm = {
           openId:this.openId
         }
         this.$axios.post(this.$httpUrl+'/user/loginWx',loginForm).then(res=>res.data).then(res=>{
           if(res.code==200){
             //存储
             sessionStorage.setItem("CurUser",JSON.stringify(res.data))
             let user1 = res.data
             this.$axios.get(this.$httpUrl+'/cart/findCartByUserId?userId='+user1.id).then(res=>{
               this.carts=res.data
               sessionStorage.setItem("CurCarts",JSON.stringify(res.data))
             })
             this.$axios.get(this.$httpUrl+'/device/findList').then(res=>{
               this.list = res.data
             })
           }else{
             this.confirm_disabled=false;
             alert('校验失败，用户名或密码错误！');
             return false;
           }
         });
       });

       // //模拟登录
       // let loginForm = {
       //   openId:this.openId
       // }
       // this.$axios.post(this.$httpUrl+'/user/loginWx',loginForm).then(res=>res.data).then(res=>{
       //   if(res.code==200){
       //     //存储
       //     // this.$axios.get(this.$httpUrl+'/wechat/publicAccount/getAccessToken').then(res=>res.data).then(res=>{
       //     //   console.log('access_token',res)
       //     //   this.$axios.get(
       //     //       'https://api.weixin.qq.com/sns/userinfo?access_token='+res+'N&openid='+this.openId+'&lang=zh_CN')
       //     //       .then(res1=>{
       //     //     console.log('res1',res1)
       //     //   })
       //     // })
       //     sessionStorage.setItem("CurUser",JSON.stringify(res.data))
       //     let user1 = res.data
       //     this.$axios.get(this.$httpUrl+'/cart/findCartByUserId?userId='+user1.id).then(res=>{
       //       this.carts=res.data
       //       sessionStorage.setItem("CurCarts",JSON.stringify(res.data))
       //     })
       //     this.$axios.get(this.$httpUrl+'/device/findList').then(res=>{
       //       this.list = res.data
       //     })
       //     if(user1.name == ''){
       //       this.$dialog.alert({
       //         title: '温馨提示',
       //         message: '请您先在“我的”页面完善个人信息后，再下订单哦',
       //       }).then(() => {
       //         // on close
       //       });
       //     }
       //   }else{
       //     this.confirm_disabled=false;
       //     alert('校验失败，用户名或密码错误！');
       //     return false;
       //   }
       // });

     }else {
       this.$axios.get(this.$httpUrl+'/cart/findCartByUserId?userId='+user.id).then(res=>{
         this.carts=res.data
         console.log(this.carts )
         sessionStorage.setItem("CurCarts",JSON.stringify(res.data))
       })
       this.$axios.get(this.$httpUrl+'/device/findList').then(res=>{
         this.list = res.data
       })
     }

  },
  methods:{
    toDetails(id){ //跳转到详情页面
      this.$router.push({
        name:'DetailsView',
        params:{
          id
        }
      })
    },
  }
}
</script>

<style scoped>
.list-title{
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}
.list-nav{
  width: 100%;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 99;
}
.iconfont{
  font-size: 8px;
  margin: 0 3px;
}
.active{
  color: red;
}
.list-sort{
  position: fixed;
  top: 54px;
  left: 0;
  z-index: 99;
  width: 100%;
  background-color: #FFFFFF;
  display: flex;
  justify-content: space-around;
  align-items: center;
  margin-top: -1px;
  font-weight: 300;
}

</style>

