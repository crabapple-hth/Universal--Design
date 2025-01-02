<script setup>
import { ref } from 'vue'
import {logout} from '../net/index.js'
import router from "@/router/index.js";

const activeIndex = ref('1')
const avatar_form=ref(false)

const handleSelect = (key, keyPath) => {
  console.log(key, keyPath)
}

const out=()=>{
  logout(()=>{
    console.log("退出")
  })
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
          <button class="avatar"  @click="()=>{avatar_form=!avatar_form}">
            <el-avatar
                src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
            />
          </button>
          <div class="avatar_form" v-if="avatar_form">
            <a href="#">退出登录</a>
          </div>
        </el-menu>
      </el-header>
      <el-container >
        <el-main class="main">
          <div>
            <el-menu mode="horizontal" default-active="0">
              <el-menu-item index="0" style="width: 100px;margin-right: 25px">推荐</el-menu-item>
              <el-menu-item index="1">关注</el-menu-item>
            </el-menu>
          </div>
          <div>
            <div class="topics">
              <div class="title">这是标题</div>
              <div class="text">我想每个人到了一定的阶段，一定的时候，总是会发生一些改变的，这种改变可能就是突然悟了，
                突然明白了这个人生的意义和以后的发展就是这么回事，感觉整个人都升华了,所以到了年纪就是会明白的，就好像一个人长大了</div>
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

.avatar{
  background-color: white;
  margin-left: 50px;
  border: none;
}

.avatar_form{
  z-index: 5;
  position: absolute;
  top: 50px;
  right: 70px;
  border: 1px solid grey;
  background-color: grey;
}

.main{
  margin-left: 200px;
  margin-right: 100px;
}

.topics{
  margin-top: 20px;

}

.title{
  font-size: 20px;
  font-weight: bold;
}

.text{
  overflow: hidden;
  text-overflow: ellipsis;
  -webkit-line-clamp: 2;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  font-weight: 20;
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