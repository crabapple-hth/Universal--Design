import {createRouter, createWebHistory} from "vue-router";
import {isUnauthorized} from "@/net/index.js";

const router=createRouter({
    history:createWebHistory(),
    routes:[
        {
            path:'/',
            name:'index',
            component:()=>import('@/components/Index.vue'),
            children:[
                {
                    path: "",
                    name: "recommend"
                },
                {
                    path: "follow",
                    name:"follow"
                },
                {
                    path:"hot",
                    name:"hot"
                }
            ]
        },
        {
            path:'/login',
            name:'login',
            component:()=>import('@/components/AuthorizePage/LoginPage.vue')
        },
        {
            path:'/account/info',
            name:'userinfo',
            component:()=>import('@/components/UserPage/UserInfo.vue'),
            children:[
                {
                    path:"collect",
                    name:'userCollect',
                    component:()=>import('@/components/Topic/collectTopic.vue')
                },
                {
                    path:"myTopic",
                    name:"myTopic"
                },
                {
                    path:"like",
                    name:"myLike"
                }
            ]
        },
        {
            path:'/account/setting',
            name:'userSetting',
            component:()=>import('@/components/UserPage/Setting.vue')
        },
        {
            path:"/topicDetails",
            name:"topicDetails",
            component:()=>import('@/components/Topic/topicDetail.vue')
        }
    ]
})

// router.beforeEach((to, from, next) => {
//     const unauthorized = isUnauthorized()
//     if(to.name.startsWith('/') && !unauthorized) {
//         next('/login')
//     }else if(to.fullPath.startsWith('/index') && unauthorized) {
//         next('/')
//     } else {
//         next()
//     }
// })

export default router