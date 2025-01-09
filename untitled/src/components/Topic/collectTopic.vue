<script setup>
import Topic from "@/components/Topic/topic.vue";
import {getCollects} from "@/net/index.js";
import {reactive,onMounted} from "vue";

const topics=reactive({
  topicList:[]
})

const init=()=>{
  getCollects((data)=>{
    topics.topicList=data
    console.log(topics.topicList)
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