<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { getTopics, logout } from '../net/index.js'
import router from "@/router/index.js";
import { ElMessage } from "element-plus";
import Topic from "@/components/Topic/topic.vue";

const page = ref(1)
const loading = ref(false);
const noMoreData = ref(false);


const activeIndex = ref('1')
const avatar_form = ref(false) // 用于控制下拉菜单的显示
const topics = reactive({
  total: "",
  topicList: []
})

const handleSelect = (key, keyPath) => {
  console.log(key, keyPath)
}

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

const out = () => {
  logout(() => {
    ElMessage.success("退出登录成功")
  })
  router.push('/login');
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

const toggleAvatarForm = () => {
  avatar_form.value = !avatar_form.value;
};

const navigateTo = (path) => {
  avatar_form.value = false; // 关闭下拉菜单
  router.push(path);
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
  <div class="common-layout">
    <el-container style="width: 100%;padding: 0;margin: 0;height: 100%">
      <el-header class="header">
        <el-menu
            :default-active="$route.path"
            class="el-menu-demo"
            mode="horizontal"
            :ellipsis="false"
            @select="handleSelect"
            router
        >
          <el-menu-item index="0" style="font-size: 25px;margin-right: 10px">校园论坛</el-menu-item>
          <el-menu-item index="/index" >首页</el-menu-item>
          <el-menu-item index="/login">第一组</el-menu-item>
          <el-menu-item index="/">第二组</el-menu-item>
          <el-menu-item index="/">第三组</el-menu-item>
          <div class="search-input">
            <label style="width: 200px">
              <input class="input_index" placeholder="搜索感兴趣的帖子">
              <img src="../assets/search.png" class="btn_pic" alt="搜索">
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
      <el-container style="margin-top: 50px; ">
        <el-main class="main">
          <div>
            <el-menu mode="horizontal" default-active="0">
              <el-menu-item index="0" style="width: 100px;margin-right: 25px">推荐</el-menu-item>
              <el-menu-item index="1">关注</el-menu-item>
            </el-menu>
          </div>
          <div>
            <router-view>
              <div class="topics" v-for="item in topics.topicList" :key="item.topicId">
                <topic :topic="item"/>
                <el-divider/>
              </div>
              <div v-if="loading">加载中...</div>
              <div v-else-if="noMoreData">没有更多数据了</div>
            </router-view>
          </div>
        </el-main>
        <el-aside class="side" width="200px">这是边框</el-aside>
      </el-container>
    </el-container>
  </div>
</template>


<style scoped>
.common-layout {
  height: 100%;
  overflow-y: auto;
}

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
.main {
  margin-left: 200px;
  margin-right: 100px;
}

.topics {
  margin-top: 20px;

}



.side {
  border: 1px solid gainsboro;
  margin-right: 50px;
  width: 20%;
  height: 200px;
  margin-top: 20px;
}

.el-menu--horizontal > .el-menu-item:nth-child(1) {
  margin-right: auto;
}

:deep(.el-divider--horizontal){
  margin: 10px 0;
}
</style>