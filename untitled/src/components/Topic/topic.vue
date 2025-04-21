<script setup>
import {ref,onMounted} from "vue";
import {getTopicLikeCollect,changeLike,changeCollect} from "@/net/index.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import {Delta} from "@vueup/vue-quill";

const isLike=ref(false)
const isCollect=ref(false)

const unlikeImg=ref("src/assets/点赞.png")
const likeImg=ref("src/assets/已点赞.png")
const unCollectImg=ref("src/assets/收藏.png")
const collectImg=ref("src/assets/已收藏.png")


const props=defineProps({
  topic:{
    type:Object,
    default:{}
  }
})

const delta=new Delta(JSON.parse(props.topic.text))

let text = '';
delta.ops.forEach(op => {
  if (typeof op.insert === 'string') {
    text += op.insert;
  }
});

const images = delta.ops
    .filter(op => op.insert && typeof op.insert === 'object' && op.insert.image)
    .map(op => op.insert.image);

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
      <div class="topic-profile">
        <div  v-if="images.length>=1">
          <el-image :src=images[0] style="width: 300px;height: 150px;margin-right: 15px"></el-image>
        </div>
        <div class="text">{{text}}</div>
      </div>
    </div>
    <div class="topic_operate">
      <el-button text @click="like">
        <img  :src="isLike ? likeImg : unlikeImg" class="topic_operate_img" style="height: 20px"  alt="">
        <div style="font-size: 13px;margin-left: 7px">{{props.topic.likeCount}}</div>
      </el-button>
      <el-button text @click="collect">
        <img :src="isCollect ? collectImg:unCollectImg" class="topic_operate_img" style="height: 20px" alt="">
        <div style="font-size: 13px;margin-left: 7px">{{props.topic.collectCount}}</div>
      </el-button>
      <el-button text>
        <img  src="../../assets/评论.png" class="topic_operate_img"
              style="font-size: 13px;margin-left: 7px;height: 20px" alt="">评论
      </el-button>
    </div>
  </div>
</template>

<style scoped>
.title {
  font-size: 20px;
  font-weight: bold;
}

.topic-profile{
  display: flex;
}

.text {
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  -webkit-line-clamp: 4;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  line-height: 1.5; /* 增加可读性 */
  max-height: 6em;
}

.topic_operate{
  margin-top: 10px;
}
</style>