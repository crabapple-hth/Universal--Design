<script setup>
import {ref, computed, reactive, onMounted} from 'vue';
import EmojiPicker from 'vue3-emoji-picker'
import 'vue3-emoji-picker/css'
import {creatCommend, getComments} from "@/net/index.js";
import {ElMessage} from "element-plus";
import axios from "axios";
import {useStore} from "@/store/index.js";

const store=useStore()
const showPicker = ref(false);
const commentText = ref();


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

const addEmoji = (emoji) => {
  console.log(emoji);
  commentText.value += emoji.i;
};

const creat=()=>{
  comment.content=commentText.value
  creatCommend(comment,()=>{
    ElMessage.success("发表成功")
    commentText.value=''
    comment.quote=-1
    getComments(props.tid,(data)=>{
      comments.list=data
    })
    showWriter.value=false
  },()=>{
    ElMessage.warning("出现了问题")
  })
}
</script>

<template>
  <div>
    <div class="comment_creat">
      <img :src="store.avatarUrl" style="width: 50px;height: 50px" alt="评论发表头像"/>
      <div style="width: 100%">
        <el-input style="margin-left: 15px;width: 90%" v-model="commentText" placeholder="友善发言，文明讨论"/>
        <el-button text @click="showPicker = !showPicker" style="margin-top: 15px">
          <img src="../../../assets/emoji_add.png" style="width: 25px;height: 25px" alt="">
        </el-button>
        <el-button type="primary" style="float: right;margin-right: 50px;margin-top: 15px"
                   size="default" @click="creat" plain>发表</el-button>
        <EmojiPicker :native="true" v-if="showPicker" @select="addEmoji" :group-names="optionsName" :display-recent="true"
                     :hide-search="true" :disable-skin-tones="true" :hide-group-icons="true"
                     theme="auto" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.comment_creat{
  display: flex;
  margin-top: 15px;
  width: 100%;
}
</style>