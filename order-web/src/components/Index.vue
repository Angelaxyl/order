<template>
  <el-container style="height: 100vh; border: 1px solid #eee">

    <el-aside :width="aside_width" style="height:100%; background-color: rgb(238, 241, 246) ;margin-left: -1px;">
      <Aside :isCollapse="isCollapse"></Aside>
    </el-aside>

    <el-container style="">
      <el-header style="text-align: right; background-color: #409eff; font-size: 12px;border-bottom: rgba(169,169,169,0.3) 1px solid;">
        <Header @doCollapse="doCollapse" :icon="icon"></Header>
      </el-header>

      <el-main style="">
<!--        <Main></Main>-->
        <Tabs></Tabs>
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Aside from "@/components/Aside";
import Header from "@/components/Header";
import Tabs from "@/components/Tabs";
export default {
  name: "Index",
  components: {Header, Aside,Tabs},
  data(){
    return{
      user : JSON.parse(sessionStorage.getItem('CurUser')),
      isCollapse:false,
      aside_width:'200px',
      icon:'el-icon-s-fold'
    }
  },
  methods:{
    doCollapse(){
      this.isCollapse = !this.isCollapse
      if(!this.isCollapse){// 展开
        this.aside_width='200px'
        this.icon='el-icon-s-fold'
      }else{// 收起
        this.aside_width='64px'
        this.icon='el-icon-s-unfold'
      }
    }
},
  watch:{
    '$store.state.menu':{
      handler(val,old){
        if(!old && this.user && this.user.userName){
          this.$store.commit("setRouter",val)
        }
      },
      immediate:true
    }
  }
}
</script>

<style scoped>
.el-header {
  /*background-color: #B3C0D1;*/
  color: #333;
  line-height: 60px;
}
.el-main{
  padding: 5px;
}
.el-aside {
  color: #333;
}
</style>