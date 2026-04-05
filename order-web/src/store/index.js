import Vue from 'vue'
import Vuex from 'vuex'
import router,{resetRouter} from "@/router"
import createPersistedState from 'vuex-persistedstate'

Vue.use(Vuex)

function addNewRoute(menuList) {
    let routes = router.options.routes
    routes.forEach(routeItem=>{
        if(routeItem.path=="/Index"){
            menuList.forEach(menu=>{
                let childRoute = {
                    path:'/'+menu.menuClick,
                    name:menu.menuName,
                    meta:{
                        icon:menu.menuIcon,
                        title:menu.menuName,
                    },
                    component:()=>import('../components/'+menu.menuComponent)
                }
                routeItem.children.push(childRoute)
            })
        }
    })
    resetRouter()
    router.addRoutes(routes)
}

export default new Vuex.Store({
    state:{
        menu:[],
        point1:0,
        point2:0,
    },
    mutations:{
        setMenu(state,menuList) {
            state.menu = menuList
            // 添加路由
            addNewRoute(menuList)
        },
        setRouter(state,menuList) {
            // 添加路由
            addNewRoute(menuList)
        },
        setPoint1(state,point1){
            state.point1 = point1
        },
        setPoint2(state,point2){
            state.point2 = point2
        },
    },
    getters:{
        getMenu(state){
            return state.menu
        }
    },
    plugins:[createPersistedState()]
})