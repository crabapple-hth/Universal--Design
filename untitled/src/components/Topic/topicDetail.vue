<script setup>

import {ArrowLeft} from "@element-plus/icons-vue";
import {changeCollect, changeLike, getTopicDetails} from "@/net/index.js";
import {ElMessage} from "element-plus";
import {ref,onMounted,reactive} from "vue";
import router from "@/router/index.js";
import {useRoute} from "vue-router";

const route=useRoute()

const isLike=ref(false)
const isCollect=ref(false)
const topicId=route.query.tid

const topic=reactive({
  title:"",
  content:"",
  userId:"",
  creatTime:"",
  updateTime:""
})

const unlikeImg=ref("../../assets/点赞.png")
const likeImg=ref("../../assets/点赞-1.png")

const like=()=>{
  changeLike(props.topic.topicId,Number(isLike.value),()=>{
    ElMessage.success("点赞")
    isLike.value=!isLike.value;
  })
}

const collect=()=>{
  changeCollect(props.topic.topicId,Number(isCollect.value),()=>{
    ElMessage.success("收藏")
    isCollect.value=!isCollect.value
  })
}

const init=()=>{
  getTopicDetails(topicId,(data)=>{
    console.log(data)
    topic.title=data.title
    topic.content=data.text
    topic.userId=data.userId
    topic.creatTime=data.creatTime
    topic.updateTime=data.updateTime
  },()=>{
    console.log("error")
  })
}

onMounted(()=>{
  init()
})

</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-header class="header">
        <el-page-header :icon="ArrowLeft" style="margin-top: 20px">
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
                       src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
            />
          </div>
          <div style="height: 40%;font-weight: bold">用户名</div>
          <div style="height: 20px;font-size: 13px">个人简介</div>
        </div>
        <div style="clear: left;letter-spacing: 2px;line-height: 2em;margin-top: 20px">
          {{topic.content}}
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

.writer{
  width: 50px;
  float: left;
}

.topic_operate{
  margin-top: 10px;
}
</style>