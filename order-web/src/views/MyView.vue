<template>
  <div>
    <div>
      <div class="headimg" style="text-align: center;">
        <van-image
            round
            width="3rem"
            height="3rem"
            style="margin-top: 70px"
            :src="headimgurl"
        />
      </div>
      <van-cell title="我的个人信息" is-link to="MyInfoView" />
      <van-cell title="我的订单" is-link to="MyOrderView" />
      <van-cell title="注销我的账号" is-link @click="toCancelUser()" />
    </div>
  </div>
</template>

<script>
export default {
  name: "MyView",
  data(){
    return{
      headimgurl:sessionStorage.getItem("headimgurl")
    }
  },
  methods:{
    toCancelUser(){
      this.$dialog.confirm({
        title: '温馨提示',
        message: '您确定要注销账号吗？',
      }).then(() => {
            // on confirm
            let user = JSON.parse(sessionStorage.getItem('CurUser'))
            this.axios.get(this.$httpUrl+'/user/del?id='+user.id).then((res) => {
              if(res.data.code == 200){
                sessionStorage.clear()
                localStorage.clear()
                this.$toast.success('账号已被注销！');
                window.location.href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx44d52ee9514d55ce&redirect_uri=http://xueyalin.gz2vip.91tunnel.com/RegisterView&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect"

              }
            })
          })
          .catch(() => {
            // on cancel
          });
    }
  }
}
</script>

<style scoped>
.headimg{
  background-image:linear-gradient(to right, #74ebd5,#acb6e5);
  width: 100%;
  height: 500px;
}
</style>