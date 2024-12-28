<script setup>
import { ref } from 'vue'

const activeIndex = ref('1')
const handleSelect = (key, keyPath) => {
  console.log(key, keyPath)
}
</script>

<template>
  <div class="common-layout">
    <el-container style="width: 100%;padding: 0;margin: 0;height: 100%">
      <el-header class="header">
        <el-menu
            :default-active="activeIndex"
            class="el-menu-demo"
            mode="horizontal"
            :ellipsis="false"
            @select="handleSelect"
        >
          <el-menu-item index="0" style="font-size: 25px;margin-right: 10px">校园论坛</el-menu-item>
          <el-menu-item index="1">首页</el-menu-item>
          <el-menu-item index="2">第一组</el-menu-item>
          <el-menu-item index="3">第二组</el-menu-item>
          <el-menu-item index="4">第三组</el-menu-item>
          <div class="search-input">
            <label style="width: 200px">
              <input class="input_index" placeholder="搜索感兴趣的帖子">
              <img src="../assets/search.png" class="btn_pic" alt="搜索">
            </label>
          </div>
        </el-menu>
      </el-header>
      <el-container >
        <el-main class="main">
          <div>
            <el-menu mode="horizontal" >
              <el-menu-item index="0" style="width: 100px;margin-right: 25px">推荐</el-menu-item>
              <el-menu-item index="1">关注</el-menu-item>
            </el-menu>
          </div>
          <div>
            <div class="topics">
              <div class="title">这是标题</div>
              <div class="text">这是内容</div>
              <div>备用</div>
              <el-divider/>
            </div>
          </div>
        </el-main>
        <el-aside class="side" width="200px">这是边框</el-aside>
      </el-container>
    </el-container>
  </div>
</template>


<style scoped>

.header{
  width: 100%;
}

.search-input{
  margin-left: 25px;
  margin-top: 15px;
  border: 1px solid grey;
  border-radius: 15px;
  height: 30px;
}

.input_index{
  border-radius: 15px;
  width: 300px;
  height: 25px;
  outline: none;
  border: none;
  margin-left: 10px;
}

.btn_pic{
  height: 25px;
  width: 25px;
  float: right;
  margin-right: 15px;
}

.main{
  margin-left: 200px;
  margin-right: 100px;
}

.topics{
  margin-top: 20px;

}

.side{
  border: 1px solid gainsboro;
  margin-right: 50px;
  margin-top: 20px;
  width: 20%;
}

.el-menu--horizontal > .el-menu-item:nth-child(1) {
  margin-right: auto;
}
</style>