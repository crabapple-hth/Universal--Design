<script setup>
import {logout} from "@/net/index.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import {ref,watch,reactive,shallowRef} from "vue";
import collectTopic from "@/components/Topic/collectTopic.vue";
import myTopic from "@/components/Topic/myTopic.vue"
import likeTopic from "@/components/Topic/likeTopic.vue"
import {useRoute} from "vue-router";

const avatar_form = ref(false)
const activeIndex = ref('1')
const loading = ref(false);
const noMoreData = ref(false);
const comp=shallowRef(myTopic)
const route=useRoute()

const handleSelect = (key, keyPath) => {
  console.log(key, keyPath)
}


const toggleAvatarForm = () => {
  avatar_form.value = !avatar_form.value;
};

const navigateTo = (path) => {
  avatar_form.value = false; // 关闭下拉菜单
  router.push(path);
};

watch(()=>route,(newValue,oldValue)=>{
  if (newValue.path==="/account/info/like") comp.value=likeTopic
  if (newValue.path==="/account/info/collect") comp.value=collectTopic
  if (newValue.path==="/account/info/myTopic") comp.value=myTopic
},{deep:true,immediate:true})

const out = () => {
  logout(() => {
    ElMessage.success("退出登录成功")
  })
  router.push('/login');
}
</script>

<template>
  <div class="common-layout">
    <el-container>
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
              <img src="../../assets/search.png" class="btn_pic" alt="搜索">
            </label>
          </div>
          <button class="avatar"  @click="toggleAvatarForm">
            <el-avatar
                src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
            />
          </button>
          <!-- 头像下拉菜单 -->
          <div class="avatar_form" v-if="avatar_form">
            <a @click="navigateTo('/account/info')">我的主页</a>
            <a @click="navigateTo('/account/setting')">设置</a>
            <a @click="out">退出登录</a>
          </div>
        </el-menu>
      </el-header>
      <el-main class="main">
        <div class="Card">
          <div style="height: 50%">

          </div>
          <div style="height: 50%;display: flex">
            <el-avatar class="info_avatar" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
            <div style="margin-top: 20px;height: 20px;width: 80%">
              <div style="font-size: 20px;">username</div>
              <div style="height: 20px;font-size: 15px;margin-top: 20px;color: grey">这个人没有任何介绍</div>
            </div>
            <el-button style="margin-top: 50px">编辑个人资料</el-button>
          </div>
        </div>
        <div class="account_info">
          <el-menu mode="horizontal" :default-active="$route.path" router>
            <el-menu-item index="/account/info/collect">我的收藏</el-menu-item>
            <el-menu-item index="/account/info/myTopic">我发布的</el-menu-item>
            <el-menu-item index="/account/info/like">我的喜欢</el-menu-item>
          </el-menu>
          <div>
            <router-view>
              <Component :is="comp"/>
            </router-view>
            <div v-if="loading">加载中...</div>
            <div v-else-if="noMoreData">没有更多数据了</div>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<style scoped>
.header {
  width: 100%;
  position: fixed;
  z-index: 1000;
}

.search-input {
  margin-left: 25px;
  margin-top: 15px;
  border: 1px solid grey;
  border-radius: 15px;
  height: 30px;
}

.input_index {
  border-radius: 15px;
  width: 300px;
  height: 25px;
  outline: none;
  border: none;
  margin-left: 10px;
}

.btn_pic {
  height: 25px;
  width: 25px;
  float: right;
  margin-right: 15px;
}

.avatar {
  background-color: white;
  margin-left: 50px;
  border: none;
}

.avatar_form {
  z-index: 5;
  position: absolute;
  top: 50px;
  right: 70px;
  border: 1px solid grey;
  background-color: white;
  padding: 10px;
  display: flex;
  flex-direction: column;
}

.avatar_form a {
  padding: 5px 10px;
  text-decoration: none;
  color: black;
  cursor: pointer;
}

.avatar_form a:hover {
  background-color: gainsboro;
}

  .main{
    height: 100%;
    margin-top: 80px;
    width: 70%;
    margin-left: 13%;
  }

  .Card{
    border: 1px solid ghostwhite;
    background-color: ghostwhite;
    height: 180px;
  }

  .info_avatar{
    height: 100px;
    width: 100px;
  }

  .account_info{
    height: 400px;
  }

.topics {
  margin-top: 20px;

}

.title {
  font-size: 20px;
  font-weight: bold;
}

.text {
  overflow: hidden;
  text-overflow: ellipsis;
  -webkit-line-clamp: 2;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  font-weight: 20;
}

.topic_operate{
  margin-top: 10px;
}

</style>