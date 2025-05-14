<script setup>

import {useRoute} from "vue-router";
import {reactive,onMounted,watch,ref} from "vue";
import {searchList} from "@/net/index.js";
import Topic from "@/components/Topic/topic.vue";
import {ElMessage} from "element-plus";

const route=useRoute()
const keyword=ref()

const searchTopicList=reactive({
  list:[],
  total:0
})

function search(){
  searchList(keyword.value,null,(data)=>{
      searchTopicList.list=data.content
      searchTopicList.total=data.total
  })
}

watch(
    () => route.query.keyword,
    (newKeyword, oldKeyword) => {
      if (newKeyword !== oldKeyword) {
        keyword.value=route.query.keyword
        search();
      }
    }
);


onMounted(
()=>{
  keyword.value=route.query.keyword
  search()
}
)

</script>

<template>
  <div class="topics" v-for="item in searchTopicList.list" :key="item.topicId">
    <topic :topic="item"/>
    <el-divider/>
  </div>
  <div v-if="!searchTopicList.total">没有相关帖子,试试其他关键词吧</div>
</template>

<style scoped>

</style>