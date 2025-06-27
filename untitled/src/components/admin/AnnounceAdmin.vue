<script setup>


import {reactive,onMounted} from "vue";
import {getAnnounce} from "@/net/index.js";

const announce=reactive({
  data:[]
})

function announceList(){
  getAnnounce((data)=>{
    announce.data=data
  })
}



onMounted(()=>{
  announceList()
})

</script>

<template>
  <div>
    <div>
      <div class="card-header">
        <span>通知管理</span>
        <el-button type="primary" @click="handleAdd" style="margin-right: 200px">添加通知</el-button>
      </div>
    </div>
    <el-divider />
    <el-table :data="announce.data" style="width: 100%" height="420">
      <el-table-column prop="id" label="编号" width="80"/>
      <el-table-column label="通知内容" width="380">
        <template #default="{row}">
          <div>
            <div>
              {{row.content}}
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="创建时间">
        <template #default="{row}">
          {{new Date(row.creatTime).toLocaleString()}}
        </template>
      </el-table-column>
      <el-table-column label="编辑">
        <template #default="{row}">
          <el-button type="danger" size="small">删除通知</el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="{row}">
          <el-button type="success" size="small">修改通知</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

</style>