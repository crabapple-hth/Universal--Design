import {createRouter, createWebHistory} from "vue-router";
import {isRoleAdmin, isUnauthorized} from "@/net/index.js";

const router=createRouter({
    history:createWebHistory(),
    routes:[
        {
            path:'/',
            name:'index',
            component:()=>import('@/components/Index.vue'),
            redirect: '/index',
            children:[
                {
                    path: "index",
                    name: "recommend",
                    component:()=>import('@/components/Topic/recommendTopic.vue')
                },
                {
                    path: "actives",
                    name: "actives",
                    component:()=>import('@/components/ActivesView.vue'),
                    children:[
                        {
                            path: "details",
                            name: "active",
                            component:()=>import('@/components/ActiveDetails.vue')
                        }
                    ]
                },
                {
                    path: "AiChat",
                    name: "AiChat",
                    component:()=>import('@/components/AiChat.vue')
                },
                {
                    path: "search",
                    name:"search",
                    component:()=>import('@/components/Topic/SearchList.vue')
                }
            ]
        },
        {
            path:'/welcome',
            name:'/welcome',
            component:()=>import('@/components/AuthorizePage/WelcomeView.vue'),
            children:[
                {
                    path:'/login',
                    name:'login',
                    component:()=>import('@/components/AuthorizePage/LoginPage.vue'),
                },
                {
                    path:'/reset',
                    name:'/reset',
                    component:()=>import('@/components/AuthorizePage/ResetPassword.vue')
                },
                {
                    path:'/register',
                    name:'/register',
                    component:()=>import('@/components/AuthorizePage/Register.vue')
                },
            ]
        }, {
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
                    name:"myTopic",
                    component:()=>import('@/components/Topic/myTopic.vue')
                },
                {
                    path:"like",
                    name:"myLike",
                    component:()=>import('@/components/Topic/likeTopic.vue')
                }
            ]
        },
        {
            path:'/account/setting',
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
                },
                {
                    path: 'active',
                    name:'admin-active',
                    component:()=>import('@/components/admin/ActiveAdmin.vue')
                },
                {
                    path: 'announce',
                    name:'admin-announce',
                    component:()=>import('@/components/admin/AnnounceAdmin.vue')
                }
            ]
        }
    ]
})

router.beforeEach((to, from, next) => {
    const admin=isRoleAdmin()
    if (to.fullPath === "/index" && isUnauthorized() ) {
        next('login');
    }else if(to.fullPath.startsWith('/admin') && !admin){
        next('index')
    }
    else {
        next()
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