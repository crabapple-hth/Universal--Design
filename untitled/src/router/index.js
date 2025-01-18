import {createRouter, createWebHistory} from "vue-router";

const router=createRouter({
    history:createWebHistory(),
    routes:[
        {
            path:'/index',
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

export default router