<script setup>
import {ref, computed, reactive, onMounted} from 'vue';
import EmojiPicker from 'vue3-emoji-picker'
import 'vue3-emoji-picker/css'
import {creatCommend, getComments} from "@/net/index.js";
import {ElMessage} from "element-plus";
import axios from "axios";
import {useStore} from "@/store/index.js";
import Comment_box from "@/components/Topic/comment_cmp/comment_box.vue";
import Comment_show from "@/components/Topic/comment_cmp/comment_show.vue";

const store=useStore()
const showWriter=ref(false)
const comments=reactive({
  list:[]
})

const optionsName = {
  "smileys_people": "微笑与人物",
  "animals_nature": "动物与自然",
  "food_drink": "食物与饮料",
  "activities": "活动",
  "travel_places": "旅行与地点",
  "objects": "物体",
  "symbols": "符号",
  "flags": "旗帜",
  "recent":"最近使用"
}

const props=defineProps({
  tid:{
    type:Number
  }
})

const comment=reactive({
  tid:props.tid,
  content:"",
  quote:Number,
})



function onSelectEmoji(emoji) {
  console.log(emoji)
  /*
    // result
    {
        i: "😚",
        n: ["kissing face"],
        r: "1f61a", // with skin tone
        t: "neutral", // skin tone
        u: "1f61a" // without tone
    }
    */
}

onMounted(()=>{
  getComments(props.tid,(data)=>{
    comments.list=data
    console.log(comments)
  })

})
</script>

<template>
  <div>
    <comment_box />
    <div class="show_comment" v-for="item in comments.list">
      <div style="margin-top: 20px">
        <div style="display: flex;align-items: center;flex-wrap: wrap">
          <el-avatar :src="axios.defaults.baseURL+'/images/'+item.avatar"/>
          <div style="margin-left: 10px">{{item.username}}</div>
        </div>
        <div style="margin-left: 50px">{{item.content}}</div>
        <div style="">
          <div style="margin-left: 50px;margin-top: 15px;display: flex;align-items: center">
            <div style="color: grey;font-size: 13px;width: 25%">{{new Date(item.time).toLocaleString()}}</div>
            <el-button text style="" size="small" @click="()=>{showWriter=!showWriter;comment.quote=item.cid}">
              回复</el-button>
          </div>
          <div class="son_comment" v-if="showWriter">
          </div>
        </div>
        <div v-for="reply in item.replies" style="margin-left: 50px;margin-top: 20px">
          <div style="display: flex;align-items: center;flex-wrap: wrap">
            <el-avatar style="margin-top: 20px" :src="axios.defaults.baseURL+'/images/'+reply.avatar"/>
            <div style="margin-left: 10px">{{reply.username}}</div>
          </div>
          <div style="margin-left: 50px">{{reply.content}}</div>
          <div style="">
            <div style="margin-left: 50px;margin-top: 15px;display: flex;align-items: center">
              <div style="color: grey;font-size: 13px;width: 25%">{{new Date(reply.time).toLocaleString()}}</div>
              <el-button text style="" size="small" @click="()=>{showWriter=!showWriter;comment.quote=item.cid}">
                回复</el-button>
            </div>
          </div>
        </div>
        <div class="son_comment" v-if="showWriter">
          <comment_box />
        </div>
      </div>
      <el-divider/>
    </div>
  </div>
</template>

<style scoped>

/* @import "tailwindcss"; */
.v3-emoji-picker .v3-footer{
  display: none;
}

.comment_creat{
  display: flex;
  margin-top: 15px;
  width: 100%;
}

.show_comment{
  margin-top: 20px;
  border-radius: 5px;
  width: 100%;
}

.son_comment{
  margin-top: 10px; /* 添加一些上边距，使评论框与回复按钮之间有间距 */
  width: 100%; /* 确保评论框宽度与父元素一致 */
  margin-left: 30px;
  display: flex;
}
</style>