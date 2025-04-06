<script setup>

import {ArrowLeft, ChatLineRound, Picture} from "@element-plus/icons-vue";
import {changeCollect, changeLike, creatCommend, getComments, getTopicDetails, takeAccessToken} from "@/net/index.js";
import {ElMessage} from "element-plus";
import {ref,onMounted,reactive} from "vue";
import router from "@/router/index.js";
import {useRoute} from "vue-router";
import {Delta,Quill, QuillEditor} from "@vueup/vue-quill";
import '@vueup/vue-quill/dist/vue-quill.bubble.css'
import ImageReSize from "quill-image-resize-vue"
import {ImageExtend,QuillWatch} from  "quill-image-super-solution-module"
import recommend from "../Topic/recommend.vue"
import axios from "axios";
import {useStore} from "@/store/index.js";

const route=useRoute()
const store=useStore()

const isInput=ref(false)

const isLike=ref(false)
const isCollect=ref(false)
const topicId=route.query.tid

// const comment=reactive({
//   tid:topicId,
//   content:"",
//   quote:-1,
// })

const comments=reactive({
  list:[]
})

const topic=reactive({
  title:"",
  content:"",
  userId:"",
  creatTime:"",
  updateTime:"",
  username:"",
  avatar:""
})

const unlikeImg=ref("../../assets/点赞.png")
const likeImg=ref("../../assets/点赞-1.png")

// Quill.register('modules/imageReSize',ImageReSize)
// Quill.register('modules/imageExtend',ImageExtend)
//
// const quillOptions={
//   modules: {
//     toolbar: {
//       container: [
//         "bold", "italic", "underline", "strike", "clean",
//         {color: []}, {'background': []},
//         {size: ["small", false, "large", "huge"]},
//         {header: [1, 2, 3, 4, 5, 6, false]},
//         {list: "ordered"}, {list: "bullet"}, {align: []},
//         "blockquote", "code-block", "link", "image",
//         {indent: '-1'}, {indent: '+1'}
//       ],
//       handlers: {
//         'image':function (){
//           QuillWatch.emit(this.quill.id)
//         }
//       }
//     },
//     imageReSize:{
//       modules:[
//         'Resize','DisplaySize'
//       ]
//     },
//     imageExtend: {
//       action:  axios.defaults.baseURL + '/api/image/topicImage',
//       name: 'file',
//       size: 5,
//       loading: true,
//       accept: 'image/png, image/jpeg',
//       response: (resp) => {
//         if(resp.data) {
//           return axios.defaults.baseURL +"/images/" +resp.data
//         } else {
//           return null
//         }
//       },
//       methods: 'POST',
//       headers: xhr => {
//         xhr.setRequestHeader('Authorization', 'Bearer '+takeAccessToken());
//       },
//       start: () => topic.uploading = true,
//       success: () => {
//         ElMessage.success('图片上传成功!')
//         topic.uploading = false
//       },
//       error: () => {
//         ElMessage.warning('图片上传失败，请联系管理员!')
//         topic.uploading = false
//       }
//     }
//   }
// }

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

// const creat=()=>{
//   creatCommend(comment,()=>{
//     ElMessage.success("发表成功")
//   },()=>{
//     ElMessage.warning("出现了问题")
//   })
// }

const init=()=>{
  getTopicDetails(topicId,(data)=>{
    topic.title=data.title
    topic.content=new Delta(JSON.parse(data.text))
    topic.userId=data.userId
    topic.creatTime=data.creatTime
    topic.updateTime=data.updateTime
    topic.username=data.username
    topic.avatar=data.avatar
  },()=>{
    console.log("error")
  })
  getComments(topicId,(data)=>{
    comments.list=data
    console.log(store.user)
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
onMounted(()=>{
  init()
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
          <div style="height: 20px;font-size: 13px">{{topic.creatTime}}</div>
        </div>
        <div
            class="content-area"
            v-html="deltaToHtml(topic.content)"
            style="clear: left;letter-spacing: 2px;line-height: 2em;margin-top: 20px">
        </div>
        <el-divider/>
        <div class="topic_operate">
          <el-button text @click="like">
            <img  src="../../assets/点赞.png" class="topic_operate_img" style="height: 20px"  alt="">{{!isLike ? "点赞" : "已点赞"}}
          </el-button>
          <el-button text @click="collect">
            <img src="../../assets/收藏.png" class="topic_operate_img" style="height: 20px" alt="">{{!isCollect ? "收藏" : "已收藏"}}
          </el-button>
          <el-button text>
            <img src="../../assets/评论.png" class="topic_operate_img" style="height: 20px" alt="">评论
          </el-button>
        </div>
        <div>
          <div class="comment_creat">
            <img :src="store.avatarUrl"
                 style="width: 50px;height: 50px"/>
            <div style="width: 100%">
              <recommend :tid="topicId"/>
<!--              <el-input size="large"-->
<!--                        style="margin-left: 20px;width: 90%;height: 50px"-->
<!--                        autosize-->
<!--                        v-model="comment.content"-->
<!--                        @focus="input"-->
<!--                        @blur="unInput"-->
<!--                        placeholder="文明发言；理性互动"/>-->

<!--              <div style="margin-left: 20px;margin-top: 10px;display: flex;justify-content: space-between" v-show="true">-->
<!--                <el-button type="primary" style="float: right" size="default" @click="creat" plain>发表</el-button>-->
<!--              </div>-->
            </div>
          </div>
        </div>
        <div >
          <div class="show_comment" v-for="item in comments.list">
            <div style="margin-top: 20px">
              <div style="display: flex;align-items: center;flex-wrap: wrap">
                <el-avatar :src="axios.defaults.baseURL+'/images/'+item.avatar"/>
                <div style="margin-left: 10px">{{item.username}}</div>
              </div>
              <div style="margin-left: 50px">{{item.content}}</div>
              <div style="margin-left: 50px;display: flex;justify-content: space-between">
                <div style="color: grey">{{item.time}}</div>
                <el-icon><ChatLineRound /></el-icon>
                <el-icon><ChatLineRound /></el-icon>
              </div>
            </div>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<style scoped>
.header {
  width: 100%;
  position: fixed;
  z-index: 1000;
  border: 1px solid gainsboro;
}

.main {
  margin-left: 350px;
  margin-top: 60px;
  border: 1px solid gainsboro;
  width: 50%;
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
  margin-top: 10px;
}

.comment_creat{
  display: flex;
  margin-top: 15px;
  width: 100%;
}


.show_comment{
  margin-top: 20px;
  border: 1px solid gainsboro;
  border-radius: 5px;
}
</style>