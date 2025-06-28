<script setup>

import {ArrowLeft, ChatLineRound, Picture} from "@element-plus/icons-vue";
import {
  authDelete,
  changeCollect,
  changeLike,
  creatCommend,
  getComments,
  getTopicDetails,
  getTopicLikeCollect,
  takeAccessToken
} from "@/net/index.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {ref,onMounted,reactive} from "vue";
import router from "@/router/index.js";
import {useRoute} from "vue-router";
import {Delta,Quill, QuillEditor} from "@vueup/vue-quill";
import '@vueup/vue-quill/dist/vue-quill.bubble.css'
import ImageReSize from "quill-image-resize-vue"
import {ImageExtend,QuillWatch} from  "quill-image-super-solution-module"
import recommend from "./commentBox.vue"
import axios from "axios";
import {useStore} from "@/store/index.js";
import Recommend from "@/components/Topic/commentBox.vue";
import CommentBox from "@/components/Topic/commentBox.vue";
import TopicEditor from "@/components/Topic/TopicEditor.vue";

const route=useRoute()
const store=useStore()
const boxShow=ref(false)
const show = ref(false);

const isInput=ref(false)

const isLike=ref(false)
const isCollect=ref(false)
const topicId=route.query.tid


const topic=reactive({
  title:"",
  content:"",
  type:"",
  userId:"",
  creatTime:"",
  updateTime:"",
  username:"",
  avatar:"",
  text:"",
  likeCount:0,
  collectCount:0
})

const unlikeImg=ref("src/assets/点赞.png")
const likeImg=ref("src/assets/已点赞.png")
const unCollectImg=ref("src/assets/收藏.png")
const collectImg=ref("src/assets/已收藏.png")

const input=()=>{
  isInput.value=true
}

const unInput=()=>{
  isInput.value=false
}

const onBack=()=>{
  router.back()
}

const like=()=>{
  changeLike(topicId,Number(isLike.value),()=>{
    ElMessage.success("点赞")
    isLike.value=!isLike.value;
  })
}

const collect=()=>{
  changeCollect(topicId,Number(isCollect.value),()=>{
    ElMessage.success("收藏")
    isCollect.value=!isCollect.value
  })
}

const init=()=>{
  getTopicDetails(topicId,(data)=>{
    topic.type=data.type
    topic.title=data.title
    topic.content=new Delta(JSON.parse(data.text))
    topic.text=data.text
    topic.userId=data.userId
    topic.creatTime=data.creatTime
    topic.updateTime=data.updateTime
    topic.username=data.username
    topic.avatar=data.avatar
    topic.likeCount=data.likeCount
    topic.collectCount=data.collectCount
    console.log(data)
  },()=>{
    console.log("error")
  })
}

function deltaToHtml(delta) {
  if (!delta?.ops) return '' // 添加防御性判断
  let html = '';
  delta.ops.forEach(op => {
    if (typeof op.insert === 'string') {
      // 处理换行和空格
      const text = op.insert
          .replace(/\n/g, '<br>')
          .replace(/  /g, '&nbsp;&nbsp;')
      html += text
    } else if (op.insert?.image) {
      // 添加 lazy loading 和备用 class
      html += `<img
        src="${op.insert.image}"
        alt="用户上传图片"
        class="rich-image"
        loading="lazy"
        style="max-width: 100%; margin: 10px 0; border-radius: 4px;">`
    }
  })
  return html
}

function ToDelete(){
  authDelete(topicId,()=>{
    ElMessage.success("删除成功")
    router.push("/index")
  })
  dialogVisible.value=false
}

const dialogVisible = ref(false)


function editorTopic(){
  show.value=true
}


onMounted(()=>{
  init()
  getTopicLikeCollect(topicId,(data)=>{
    isLike.value=data[0]
    isCollect.value=data[1]
  })
})

</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-header class="header">
        <el-page-header @back="onBack" :icon="ArrowLeft" style="margin-top: 20px">
          <template #content>
            <span class="text-large font-600 mr-3"> 帖子详情 </span>
          </template>
        </el-page-header>
      </el-header>
      <el-main class="main">
        <div>
          <div class="title">{{topic.title}}</div>
        </div>
        <div style="margin-top: 20px">
          <div class="writer">
            <el-avatar class="avatar"
                       :src="axios.defaults.baseURL+'/images/'+topic.avatar"
            />
          </div>
          <div style="height: 40%;font-weight: bold">{{topic.username}}</div>
          <div style="height: 20px;font-size: 13px">{{new Date(topic.creatTime).toLocaleString()}}</div>
        </div>
        <div
            class="content-area"
            v-html="deltaToHtml(topic.content)"
            style="clear: left;letter-spacing: 2px;line-height: 2em;margin-top: 20px; ">
        </div>
        <div style="text-align: center;font-size: 13px;color: gray">
          上次修改时间{{new Date(topic.updateTime).toLocaleString()}}
        </div>
        <el-divider/>
        <div class="topic_operate">
          <el-button text @click="like">
            <img  :src="isLike?likeImg:unlikeImg" class="topic_operate_img" style="height: 20px"  alt="">
            <div style="font-size: 13px;margin-left: 7px">{{topic.likeCount}}</div>
          </el-button>
          <el-button text @click="collect">
            <img :src="isCollect?collectImg:unCollectImg" class="topic_operate_img" style="height: 20px" alt="">
            <div style="font-size: 13px;margin-left: 7px">{{topic.collectCount}}</div>
          </el-button>
          <el-button @click="boxShow=!boxShow" text>
            <img src="../../assets/评论.png" class="topic_operate_img" style="height: 20px" alt="">评论
          </el-button>
          <div class="auth_operate" v-show="topic.userId===store.user.userid">
            <el-button text  @click="editorTopic">编辑帖子</el-button>
            <el-button text  @click="dialogVisible=true">删除帖子</el-button>
            <el-dialog
                v-model="dialogVisible"
                title="提示"
                width="500"
            >
              <span>确定要删除该帖子吗</span>
              <template #footer>
                <div class="dialog-footer">
                  <el-button @click="dialogVisible = false">取消</el-button>
                  <el-button type="primary" @click="ToDelete">
                    确定
                  </el-button>
                </div>
              </template>
            </el-dialog>
          </div>
        </div>
        <div>
          <comment-box :tid="topicId" :show="boxShow"/>
        </div>
      </el-main>
    </el-container>
    <topic-editor :show="show" @success="show = false" @close="show = false" v-if="topic"
                  :default-title="topic.title" :default-text="topic.text" :topic-id="topicId"
                  :default-type="topic.type"  submit-button="更新帖子内容"   />
  </div>
</template>

<style scoped>
.header {
  width: 100%;
  position: fixed;
  border: 1px solid gainsboro;
  background: #FFFFFF;
}

.main {
  margin-left: 20%;
  margin-top: 60px;
  border: 1px solid gainsboro;
  width: 60%;
}

.title{
  font-size: 25px;
}

.content-area {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
  line-height: 1.6;
}

.content-area img.rich-image {
  cursor: zoom-in;
  transition: transform 0.3s;
}

.content-area img.rich-image:hover {
  transform: scale(1.02);
}

.writer{
  width: 50px;
  float: left;
}

.topic_operate{
  display: flex;
  margin-top: 10px;

  .auth_operate{
    margin-left: 45%;
  }
}


</style>