import {defineStore} from "pinia";
import axios from "axios";

export const useStore=defineStore('general',{
    state:()=>{
        return {
            user:{
                userid:1,
                username:'',
                email:'',
                role:'',
                avatar:null,
                creatTime:''
            },
            forum:{
                types:[]
            }
        }
    },getters:{
        avatarUrl(){
            if (this.user.avatar)
                return `${axios.defaults.baseURL}/images/${this.user.avatar}`
            else
                return 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
        }
    },actions:{
        avatarUserUrl(avatar) {
            if(avatar)
                return `${axios.defaults.baseURL}/images/${avatar}`
            else
                return 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
        },
        forumType(typeId){
            if (typeId)
                return this.forum.types.find(type=>type.id=typeId).name
            else return "未获取到信息"
        }
    }
})