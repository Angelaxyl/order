<template>
  <div style="height: 100%">
    <el-menu

        background-color="#545c64"
        text-color="#fff"
        active-text-color= "#409eff"
        style="height: 100%"
        :default-active="active"
        :collapse="isCollapse"
        :collapse-transition="false"
        router
    >
      <!--    active-text-color="#ffd04b"-->
      <el-menu-item :index="'/'+item.menuClick" v-for="(item,i) in menu" :key="i">
        <div>
          <i :class="item.menuIcon"></i>
          <span slot="title" >{{ item.menuName }}</span>
          <el-tag v-if="item.menuClick=='Order' && $store.state.point1>0" style="margin-left: 10px" type="danger" effect="dark">待处理</el-tag>
          <el-tag v-if="item.menuClick=='Bill' && $store.state.point2>0" style="margin-left: 10px" type="danger" effect="dark">待核验</el-tag>
        </div>

      </el-menu-item>
    </el-menu>
  </div>

</template>

<script>
export default {
  name: "Aside",
  data(){
    return{
    }
  },
  computed:{
    "menu":{
      get(){
        return this.$store.state.menu
      }
    },
    active(){
      return this.$route.fullPath;
    }
  },
  props:{
    isCollapse:Boolean
  },
  mounted() {
    this.$axios.get(this.$httpUrl+'/order/showPoint1').then(res => {
      this.$store.commit('setPoint1',res.data.data)
    })
    this.$axios.get(this.$httpUrl+'/order/showPoint2').then(res => {
      this.$store.commit('setPoint2',res.data.data)
    })
  }
}
</script>

<style scoped>
.item {
  margin-top: 10px;
  margin-right: 40px;
}
</style>