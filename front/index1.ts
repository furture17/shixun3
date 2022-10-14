import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import LayOut from '@/views/LayOut/LayOut.vue'
import store from "@/store";
import {ElMessage} from "element-plus";

const routes: Array<RouteRecordRaw> = [
    /* {
         path: '/',
         name: 'home',
         component: HomeView
     },*/
    {
        path: '/about',
        name: 'about',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
    },
    {
        path: '/main',
        name: 'main',
        component: () => import('../views/MainView.vue')
    },
    {
        path: '/authority',
        name: 'authority',
        component: () => import('../views/AuthorityView.vue')
    },

    {
        path: '/test',
        name: 'test',
        component: () => import('../views/TestView.vue')
    },

    {
        path: '/admin',
        name: 'admin',
        component: () => import('../views/AdminView.vue')
    },
    {
        path: "/",
        name: "layout",
        component: LayOut,
        redirect: "/index",
        // 子路由/嵌套路由
        children: [
            {
                path: "/index",
                name: "index",
                component: () => import("../views/pages/Welcome.vue")
            },
            {
                path: "/usercenter",
                name: "usercenter",
                component: () => import("../views/pages/UserCenterView.vue")
            },
            {
                path: "/model",
                name: "model",
                component: () => import("../views/pages/Model.vue")
            },
            {
                path: "/user",
                name: "user",
                component: () => import("../views/pages/UsersManage.vue")
            },
            {
                path: "/addModel",
                name: "addModel",
                component: () => import("../views/pages/AddModel.vue")
            },
            {
                path: '/userCenter',
                name: 'userCenter',
                component: () => import('../views/pages/UserCenterView.vue')
            },
            {
                path: '/dataset',
                name: 'dataset',
                component: () => import('../views/pages/Dataset.vue')
            },
            {
                path: '/history',
                name: 'history',
                component: () => import('../views/pages/History.vue')
            },
            {
                path: '/predict',
                name: 'predict',
                component: () => import('../views/pages/Predict.vue')
            },
        ]
    },
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

router.beforeEach((to, from, next) => {
    console.log(store.state.userId)
    next()})
    /*if (to.path == '/authority'||store.state.userId != '') {
        next()
    } else {
        next('/authority')
        ElMessage.error('请先登录')
    }
}) */

export default router