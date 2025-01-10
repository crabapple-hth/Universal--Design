<script setup>

import Topic from "@/components/Topic/topic.vue";
import {getTopics} from "@/net/index.js";
import {ElMessage} from "element-plus";
import {onMounted, onUnmounted, reactive, ref} from "vue";

const loading = ref(false);
const noMoreData = ref(false);

const page = ref(1)

const topics = reactive({
  total: "",
  topicList: []
})

const initIndex = () => {
  loading.value = true;
  getTopics(page.value, (data) => {
    if (page.value === 1) {
      topics.topicList = data.topics;
      topics.total = data.total[0]
    } else {
      topics.topicList.push(...data.topics);
    }
    if (data.topics.length === 0) {
      noMoreData.value = true
      ElMessage.warning("已经没有更多数据了")
    }
    loading.value = false;
  }, () => {
    ElMessage.warning("出现了一些错误，请刷新页面重试")
    loading.value = false;
  })
}

const handleScroll = () => {
  if (loading.value || noMoreData.value) return;

  const scrollTop = document.documentElement.scrollTop;
  const clientHeight = document.documentElement.clientHeight;
  const scrollHeight = document.documentElement.scrollHeight;

  if (scrollTop + clientHeight >= scrollHeight - 10) {
    page.value++;
    initIndex();
  }
};

onMounted(() => {
  initIndex()
  window.addEventListener('scroll', handleScroll);
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
})
</script>

<template>
  <div class="topics" v-for="item in topics.topicList" :key="item.topicId">
    <topic :topic="item"/>
    <el-divider/>
  </div>
  <div v-if="loading">加载中...</div>
  <div v-else-if="noMoreData">没有更多数据了</div>
</template>

<style scoped>

</style>