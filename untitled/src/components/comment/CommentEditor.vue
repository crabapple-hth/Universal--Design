<script setup>

import EmojiPicker from "vue3-emoji-picker";
import {useStore} from "@/store/index.js";
import {reactive, ref} from "vue";
import {creatCommend, getComments} from "@/net/index.js";
import {ElMessage} from "element-plus";


const sensitiveWords = [
  '你妈',
  '傻逼',
  '他妈的',
];

const containsSensitiveWords = (text) => {
  if (!text) return false;
  return sensitiveWords.some(word => text.includes(word));
};


const props=defineProps({
  tid:{
    type:Number
  },
  commentInfo:{
    type:Object
  }
})

const emit=defineEmits(['close', 'success'])

const store = useStore()
const commentText = ref();
const showPicker = ref(false);
const comment = reactive({
  tid: props.tid,
  content: "",
  reply_cid: null,
  top_comment_id: null
})

const addEmoji = (emoji) => {
  commentText.value += emoji.i;
};

const optionsName = {
  "smileys_people": "微笑与人物",
  "animals_nature": "动物与自然",
  "food_drink": "食物与饮料",
  "activities": "活动",
  "travel_places": "旅行与地点",
  "objects": "物体",
  "symbols": "符号",
  "flags": "旗帜",
  "recent": "最近使用"
}

const creat = () => {
  comment.content = commentText.value

  // 敏感词检测
  if (containsSensitiveWords(commentText.value)) {
    ElMessage.warning("评论包含不合适内容，请修改后再发表");
    return;
  }
  if (!props.commentInfo){
    comment.reply_cid=-1
    comment.top_comment_id=-1
  }else {
    comment.reply_cid=props.commentInfo.reply_cid;
    comment.top_comment_id=props.commentInfo.top_comment_id
  }
  creatCommend(comment, () => {
    ElMessage.success("发表成功")
    commentText.value = ''
    emit('success')
    emit('close')
  }, () => {
    ElMessage.warning("出现了问题")
  })
 }
</script>

<template>
  <div class="comment_creat">
    <img :src="store.avatarUrl" style="width: 50px; height: 50px" alt="评论发表头像" />
    <div style="width: 100%">
      <el-input style="margin-left: 15px; width: 90%" v-model="commentText" placeholder="友善发言，文明讨论" />
      <el-button text @click="showPicker = !showPicker" style="margin-top: 15px">
        <img src="../../assets/emoji_add.png" style="width: 25px; height: 25px" alt="">
      </el-button>
      <el-button type="primary" style="float: right; margin-right: 50px; margin-top: 15px" size="default" @click="creat"
                 plain>发表</el-button>
      <EmojiPicker :native="true" v-if="showPicker" @select="addEmoji" :group-names="optionsName" :display-recent="true"
                   :hide-search="true" :disable-skin-tones="true" :hide-group-icons="true" theme="auto" />
    </div>
  </div>
</template>

<style scoped>

.comment_creat {
  display: flex;
  margin-top: 15px;
  width: 100%;
}
</style>