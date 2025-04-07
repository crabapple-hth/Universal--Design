<script setup>
import {ref, computed, reactive, onMounted} from 'vue';
import EmojiPicker from 'vue3-emoji-picker'
import 'vue3-emoji-picker/css'
import {creatCommend, getComments} from "@/net/index.js";
import {ElMessage} from "element-plus";
import axios from "axios";
import {useStore} from "@/store/index.js";
import Comment_box from "@/components/Topic/comment_cmp/comment_box.vue";


const store=useStore()
const comments=reactive({
  list:[]
})

const showWriter=ref(false)

const props=defineProps({
  item:{
    type:Object,
    default:{}
  }
})

const comment=reactive({
  tid:props.tid,
  content:"",
  quote:Number,
})

onMounted(()=>{
  // getComments(props.tid,(data)=>{
  //   comments.list=data
  // })
  console.log(props)
})
</script>

<template>
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
  </div>
</template>

<style scoped>

</style>