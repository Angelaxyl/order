import Vue from 'vue'
import App from './App.vue'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/styles/icon/iconfont.css'
import './assets/global.css'
import axios from 'axios'
import service from "./utils/service.js";
Vue.prototype.$axios=service;
// Vue.prototype.$axios=axios;
// Vue.prototype.$httpUrl='http://localhost:8081';
// Vue.prototype.$httpUrl='http://wz.free.idcfengye.com';
Vue.prototype.$httpUrl='https://pxw4d8jc.asse.devtunnels.ms:8081';
import VueAxios from 'vue-axios'
import VueRouter from 'vue-router'
import router from './router'
//引入ECharts
import ECharts from 'vue-echarts';
import 'echarts';

Vue.component('ECharts',ECharts);


//h5rem适配
import 'amfe-flexible/index.js'

import Vant from 'vant';
import 'vant/lib/index.css';

Vue.use(Vant)

Vue.use(VueAxios, axios)
Vue.use(ElementUI,{size:'small'})
Vue.use(VueRouter)
Vue.config.productionTip = false

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
