<script setup>
import { ref, reactive, onMounted ,onUnmounted} from 'vue';
import EmojiPicker from 'vue3-emoji-picker'
import 'vue3-emoji-picker/css'
import {creatCommend, getComments, replyCommentList} from "@/net/index.js";
import { ElMessage } from "element-plus";
import axios from "axios";
import { useStore } from "@/store/index.js";

const store = useStore()
const showPicker = ref(false);
const commentText = ref();
const comments = reactive({
  list: []
})
const replies=reactive({
  show:false,
  total:0,
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
  "recent": "最近使用"
}

const props = defineProps({
  tid: {
    type: Number
  },
  show: {
    type: Boolean
  }
})

const comment = reactive({
  tid: props.tid,
  content: "",
  reply_cid: null,
  top_comment_id: null
})

const addEmoji = (emoji) => {
  commentText.value += emoji.i;
};

const creat = () => {
  comment.content = commentText.value
  creatCommend(comment, () => {
    ElMessage.success("发表成功")
    commentText.value = ''
    getComments(props.tid, (data) => {
      comments.list = data.map(item => ({ ...item, showReply: false }));
    })
  }, () => {
    ElMessage.warning("出现了问题")
  })
}

function toggleReply(item,event) {
  event.stopPropagation()
  item.showReply = !item.showReply;
  comment.top_comment_id = item.cid;
  comment.reply_cid = item.cid;
}


function handleGlobalClick(event) {
  comments.list.forEach(item => {
    if (item.showReply) {
      const replyBox = document.querySelector(`.son_comment[data-cid="${item.cid}"]`);
      if (replyBox && !replyBox.contains(event.target)) {
        item.showReply = false;
      }
    }
  });
}



function lookClick(item){
  replyCommentList(item.cid,1,5,(data)=>{
    replies.total=data.length
    replies.list=data
    replies.show=!replies.show
    console.info(data)
  })
}

onMounted(() => {
  getComments(props.tid, (data) => {
    comments.list = data.map(item => ({ ...item, showReply: false }));
  })

  // 添加全局点击事件监听器
  document.addEventListener('click', handleGlobalClick);
})

onUnmounted(() => {
  // 移除全局点击事件监听器
  document.removeEventListener('click', handleGlobalClick);
})
</script>

<template>
  <div>
    <div v-if="show" class="comment_creat">
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
    <div class="show_comment" v-for="item in comments.list" :key="item.cid">
      <div style="margin-top: 20px">
        <div style="display: flex; align-items: center; flex-wrap: wrap">
          <el-avatar :src="axios.defaults.baseURL + '/images/' + item.avatar" />
          <div style="margin-left: 10px">{{ item.username }}</div>
        </div>
        <div style="margin-left: 50px">{{ item.content }}</div>
        <div>
          <div style="margin-left: 50px; margin-top: 15px; display: flex; align-items: center">
            <div style="color: grey; font-size: 13px; width: 25%">{{ new Date(item.time).toLocaleString() }}</div>
            <el-button text style="" size="small" @click="toggleReply(item,$event)">
              回复
            </el-button>
          </div>
        </div>
        <div v-for="reply in item.replies" style="margin-left: 50px; margin-top: 20px">
          <div style="display: flex; align-items: center; flex-wrap: wrap">
            <el-avatar style="margin-top: 20px" :src="axios.defaults.baseURL + '/images/' + reply.avatar" />
            <div style="margin-left: 10px">{{ reply.username }}</div>
          </div>
          <div style="margin-left: 50px">{{ reply.content }}</div>
          <div>
            <div style="margin-left: 50px; margin-top: 15px; display: flex; align-items: center">
              <div style="color: grey; font-size: 13px; width: 25%">{{ new Date(reply.time).toLocaleString() }}</div>
              <el-button text style="" size="small" @click="toggleReply(item,$event); comment.reply_cid = reply.cid">
                回复
              </el-button>
            </div>
          </div>
        </div>
        <div class="clickable-text" @click="lookClick(item)" v-if="!replies.show">
          点击查看
        </div>
        <div v-else class="pagination">
          <el-pagination layout="prev, pager, next" :total=replies.total />
        </div>
        <div class="son_comment" :data-cid="item.cid" v-if="item.showReply">
          <div class="comment_creat">
            <img :src="store.avatarUrl" style="width: 50px; height: 50px" alt="评论发表头像" />
            <div style="width: 100%">
              <el-input style="margin-left: 15px; width: 90%" v-model="commentText" placeholder="友善发言，文明讨论" />
              <el-button text @click="showPicker = !showPicker" style="margin-top: 15px">
                <img src="../../assets/emoji_add.png" style="width: 25px; height: 25px" alt="">
              </el-button>
              <el-button type="primary" style="float: right; margin-right: 50px; margin-top: 15px" size="default"
                         @click="creat" plain>发表</el-button>
              <EmojiPicker :native="true" v-if="showPicker" @select="addEmoji" :group-names="optionsName"
                           :display-recent="true" :hide-search="true" :disable-skin-tones="true" :hide-group-icons="true"
                           theme="auto" />
            </div>
          </div>
        </div>
      </div>
      <el-divider />
    </div>
  </div>
</template>

<style scoped>
.v3-emoji-picker .v3-footer {
  display: none;
}

.comment_creat {
  display: flex;
  margin-top: 15px;
  width: 100%;
}

.show_comment {
  margin-top: 20px;
  border-radius: 5px;
  width: 100%;
}

.son_comment {
  margin-top: 10px;
  width: 100%;
  margin-left: 30px;
  display: flex;
}

.clickable-text {
  font-size: 13px;
  color: grey;
  margin-left: 30px;
  cursor: pointer;
  padding: 5px;
}

.clickable-text:hover {
  color: skyblue;
}

.pagination{
  margin-left: 30px;
}
</style>