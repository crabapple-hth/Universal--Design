import {createRouter, createWebHistory} from "vue-router";
import {isRoleAdmin, isUnauthorized} from "@/net/index.js";

const router=createRouter({
    history:createWebHistory(),
    routes:[
        {
            path:'/',
            name:'index',
            component:()=>import('@/components/Index.vue'),
            children:[
                {
                    path: "index",
                    name: "recommend",
                    component:()=>import('@/components/Topic/recommendTopic.vue')
                },
                {
                    path: "actives",
                    name: "actives",
                    component:()=>import('@/components/ActivesView.vue')
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
            component:()=>import('@/components/UserPage/personalPage.vue'),
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
        },
        {
            path:"/admin",
            name:'admin',
            component:()=>import('@/components/AdminView.vue'),
            children:[
                {
                    path: '',
                    name:'admin-welcome',
                    component:()=>import('@/components/admin/welcomeAdmin.vue')
                },
                {
                    path: 'user',
                    name:'admin-user',
                    component:()=>import('@/components/admin/UserAdmin.vue')
                },
                {
                    path: 'forum',
                    name:'admin-forum',
                    component:()=>import('@/components/admin/ForumAdmin.vue')
                }
            ]
        }
    ]
})


router.beforeEach((to, from, next) => {
    const admin=isRoleAdmin()
    if (to.fullPath !== "/login" && isUnauthorized() ) {
        next('login');
    }else if(to.fullPath.startsWith('/admin') && !admin){
        next('index')
    }
    else {
        next();
    }
});

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