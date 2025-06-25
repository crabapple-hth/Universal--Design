<script setup>

import Topic from "@/components/Topic/topic.vue";
import {getTopics, getTopicsByType} from "@/net/index.js";
import {ElMessage} from "element-plus";
import {onMounted, onUnmounted, reactive, ref,watch} from "vue";
import {useStore} from "@/store/index.js";

const loading = ref(false);
const noMoreData = ref(false);

const page = ref(1)
const store=useStore()
const typeId=ref(0)


const handleTypeSelect=(id)=>{
  page.value=1
  noMoreData.value=false
  typeId.value = Number(id);
};

const topics = reactive({
  total: "",
  topicList: []
})

const initIndex = () => {
  loading.value = true;
  const requestFunction = typeId.value===0 ? getTopics : getTopicsByType;
  const requestData = typeId.value===0 ? page.value : { page: page.value, type: typeId.value };

  requestFunction(requestData, (data) => {
    if (page.value === 1) {
      topics.topicList = data.topics;
      topics.total = data.total[0];
    } else {
      topics.topicList.push(...data.topics);
    }
    if (data.topics.length === 0) {
      noMoreData.value = true;
      ElMessage.warning("已经到底啦");
    }
    loading.value = false;
  }, () => {
    ElMessage.warning("出现了一些错误，请刷新页面重试");
    loading.value = false;
  });
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


watch(()=>typeId.value,async (newValue,OldValue)=>{
  console.log(newValue,OldValue)
  initIndex();
});
</script>

<template>
  <div>
    <el-menu mode="horizontal"
             default-active="0"
             @select="handleTypeSelect"
    >
      <el-menu-item index="0" style="width: 100px;margin-right: 15px">推荐</el-menu-item>
      <el-menu-item :index="item.id" v-for="item in store.forum.types"
                    style="width: 100px;margin-right: 15px">{{item.name}}</el-menu-item>
    </el-menu>
  </div>
  <div class="topics" v-for="item in topics.topicList" :key="item.topicId">
    <topic :topic="item"/>
    <el-divider/>
  </div>
  <div v-if="loading">加载中...</div>
  <div v-else-if="noMoreData">已经到底啦</div>
</template>

<style scoped>

</style>