<script setup>
import Topic from "@/components/Topic/topic.vue";
import {getMyTopics} from "@/net/index.js";
import {reactive,onMounted} from "vue";
import router from "@/router/index.js";

const topics=reactive({
  topicList:[]
})

const init=()=>{
  getMyTopics((data)=>{
    topics.topicList=data
  })
}



onMounted(()=>{
  init()
})
</script>

<template>
  <div class="topics" v-for="item in topics.topicList" :key="item.topicId">
    <topic :topic="item"/>
    <el-divider/>
  </div>
</template>

<style scoped>

</style>