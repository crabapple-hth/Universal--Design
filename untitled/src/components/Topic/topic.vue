<script setup>
import {ref,onMounted} from "vue";
import {getTopicLikeCollect,changeLike,changeCollect} from "@/net/index.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const isLike=ref(false)
const isCollect=ref(false)

const unlikeImg=ref("../../assets/点赞.png")
const likeImg=ref("../../assets/点赞-1.png")


const props=defineProps({
  topic:{
    type:Object,
    default:{}
  }
})

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

const getDetail=()=>{
  const topicId=props.topic.topicId
  router.push({name:"topicDetails",query:{tid:topicId}})
}

onMounted(()=>{
  getTopicLikeCollect(props.topic.topicId,(data)=>{
    isLike.value=data[0]
    isCollect.value=data[1]
  })
})
</script>

<template>
  <div >
    <div @click="getDetail">
      <div class="title">{{ topic.title }}</div>
      <div class="text">{{ topic.text }}</div>
    </div>
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
  </div>
</template>

<style scoped>
.title {
  font-size: 20px;
  font-weight: bold;
}

.text {
  overflow: hidden;
  text-overflow: ellipsis;
  -webkit-line-clamp: 2;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  font-weight: 20;
}

.topic_operate{
  margin-top: 10px;
}
</style>