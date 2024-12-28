import {createRouter, createWebHistory} from "vue-router";

const router=createRouter({
    history:createWebHistory(),
    routes:[
        {
            path:'/index',
            name:'index',
            component:()=>import('@/components/Index.vue')
        },
        {
            path:'/',
            name:'login',
            component:()=>import('@/components/LoginPage.vue')
        }
    ]
})

export default router