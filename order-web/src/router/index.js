import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/IndexView.vue'

Vue.use(VueRouter)

const routes = [
    //移动端
    /*{
        path: '/',
        redirect:'/HomeView'
    },*/
    {
        path:'/IndexView',
        name:'IndexView',
        component:()=>import('../views/IndexView.vue'),
        children:[
            {
                path: '/HomeView',  //首页
                name: 'HomeView',
                meta:{
                    title:'首页'
                },
                component: () => import('../views/HomeView.vue')
            },
            {
                path: '/CartView',  //购物车
                name: 'CartView',
                component: () => import('../views/CartView.vue')
            },

            {
                path: '/MyView',  //我的
                name: 'MyView',
                component: () => import('../views/MyView.vue')
            },
            {
                path: '/DetailsView',  //详情
                name: 'DetailsView',
                component: () => import('../views/DetailsView.vue')
            },


        ]
    },
    {
        path: '/MyInfoView',  //我的个人信息
        name: 'MyInfoView',
        component: () => import('../views/MyInfoView.vue')
    },
    {
        path: '/MyOrderView',  //我的订单
        name: 'MyOrderView',
        component: () => import('../views/MyOrderView.vue')
    },
    {
        path: '/BindSimView',  //绑定sim卡
        name: 'BindSimView',
        component: () => import('../views/BindSimView.vue')
    },
    {
        path: '/RegisterView',  //注册页面
        name: 'RegisterView',
        component: () => import('../views/RegisterView.vue')
    },




    //电脑端
    {
      path: '/',
      redirect:'/Login'
    },
    {
        path: '/Login',  //登录页
        name: 'login',
        component: () => import('../components/Login.vue')
    },
    {
        path:'/Index',
        name:'index',
        component:()=>import('../components/Index.vue'),
        children:[
            {
                path:'/Home',
                name:'home',
                meta:{
                    title:'首页',
                    icon:'iconfont icon-home-fill'
                },
                component:()=>import('../components/home/HomeManage.vue')
            }
        ]
    },

]

const router = new VueRouter({
    mode: 'history',
    // base: process.env.BASE_URL,
    routes
})

//重置
export function resetRouter() {
  router.matcher = new VueRouter({
      mode:'history',
      routes: []
  }).matcher
}
//路由错误解决
const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (to) {
  return VueRouterPush.call(this, to).catch(err => err)
}

export default router
