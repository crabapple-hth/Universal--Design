<script setup>

import {User} from "@element-plus/icons-vue";
import {apiDelTopic, apiSetTop, apiTopicList, getTypeList} from "@/net/index.js";
import {reactive,ref,onMounted} from "vue";
import {useStore} from "@/store/index.js";
import {ElMessage} from "element-plus";

const store=useStore()

const page=ref(1)
const size=ref(5)

const topicList=reactive({
  total:0,
  data:[]
})

function getList(){
  apiTopicList(page.value,size.value,(data)=>{
    topicList.total=data.total
    topicList.data=data.list
  })
}

const handleCurrentChange = (val) => {
  page.value=val
  getList()
}

function delTopic(tid){
  apiDelTopic(tid,()=>{
    getList()
    ElMessage.success("操作成功")
  })
}

function setTop(tid){
  apiSetTop(tid,()=>{
    ElMessage.success("置顶成功")
  })
}

onMounted(()=> {
  getTypeList((data)=>{store.forum.types=data})
  getList()
})
</script>

<template>
  <div class="topic-admin">
    <div class="title">
      <el-icon><User/></el-icon>
      论坛帖子总览
    </div>
    <div class="desc">
      在这里管理论坛帖子，包括删除、置顶等
    </div>
    <el-table :data="topicList.data" style="width: 100%;height: 420px">
      <el-table-column prop="tid" label="编号" width="80"/>
      <el-table-column label="用户" width="180">
        <template #default="{row}">
          <div class="table-username">
            <el-avatar :size="30" :src="store.avatarUserUrl(row.avatar)" />
            <div>
              {{row.username}}
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="title" label="标题" width="180" />
      <el-table-column prop="" label="类型">
        <template #default="{row}">
          <div>
            {{ store.forumType(row.type)}}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="发表时间" width="180">
        <template #default="{row}">
          <div>
            {{ new Date(row.creatTime).toLocaleString()}}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="置顶">
        <template #default="{row}">
          <el-switch v-model="row.top" @change="setTop(row.tid)" />
        </template>
      </el-table-column>
      <el-table-column label="编辑">
        <template #default="{row}">
          <el-button type="danger" size="small"
                     @click="delTopic(row.tid)">删除帖子</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination">
      <el-pagination
          :total="topicList.total"
          v-model:current-page="page"
          v-model:page-size="size"
          @current-change="handleCurrentChange"
          layout="total,sizes,prev,pager,next,jumper"/>
    </div>
  </div>
</template>

<style scoped>
.table-username{
  height: 30px;
  display: flex;
  align-items: center;
  gap: 15px;
}

.pagination{
  margin-top: 20px;
  display: flex;
  justify-content: right;
}
</style>