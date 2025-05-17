<script setup>
import {logout} from "@/net/index.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import {ref, watch, reactive, shallowRef, computed} from "vue";
import collectTopic from "@/components/Topic/collectTopic.vue";
import myTopic from "@/components/Topic/myTopic.vue"
import likeTopic from "@/components/Topic/likeTopic.vue"
import {useRoute} from "vue-router";
import {useStore} from "@/store/index.js";
import UserInfo from "@/components/Topic/userInfo.vue";

const avatar_form = ref(false)
const loading = ref(false);
const noMoreData = ref(false);
const comp=shallowRef(myTopic)
const route=useRoute()

const handleSelect = (key, keyPath) => {
  console.log(key, keyPath)
}

const store=useStore()


const toggleAvatarForm = () => {
  avatar_form.value = !avatar_form.value;
};

const navigateTo = (path) => {
  avatar_form.value = false; // 关闭下拉菜单
  router.push(path);
};

const toSetting=()=>{
  router.push("/account/setting")
}

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

const activeMenu = computed(() => route.path);

</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-header class="header">
        <el-menu
            :default-active="activeMenu"
            class="el-menu-demo"
            mode="horizontal"
            :ellipsis="false"
            @select="handleSelect"
            router
        >
          <el-menu-item index="0" style="font-size: 25px; margin-right: 10px">校园论坛</el-menu-item>
          <el-menu-item index="/index">首页</el-menu-item>
          <el-menu-item index="/actives">校园活动</el-menu-item>
          <div class="search-input">
            <input type="text" placeholder="搜索..." style="border: none; outline: none; flex-grow: 1; padding: 5px;" v-model="searchWord">
            <button style="background: none; border: none; cursor: pointer; padding: 5px;" @click="search">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <circle cx="11" cy="11" r="8"></circle>
                <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
              </svg>
            </button>
          </div>
          <user-info/>
<!--          <button class="avatar"  @click="toggleAvatarForm">-->
<!--            <el-avatar-->
<!--                :src="store.avatarUrl"-->
<!--            />-->
<!--          </button>-->
          <!-- 头像下拉菜单 -->
<!--          <div class="avatar_form" v-if="avatar_form">-->
<!--            <a @click="navigateTo('/account/info')">我的主页</a>-->
<!--            <a @click="navigateTo('/account/setting')">设置</a>-->
<!--            <a @click="out">退出登录</a>-->
<!--          </div>-->
        </el-menu>
      </el-header>
      <el-main class="main">
        <div class="Card">
          <div style="height: 50%">

          </div>
          <div style="height: 50%;display: flex">
            <el-avatar class="info_avatar" :src="store.avatarUrl"></el-avatar>
            <div style="margin-top: 20px;height: 20px;width: 80%">
              <div style="font-size: 20px;">{{store.user.username}}</div>
              <div style="height: 20px;font-size: 15px;margin-top: 20px;color: grey">这个人没有任何介绍</div>
            </div>
            <el-button style="margin-top: 50px" @click="toSetting">编辑个人资料</el-button>
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
  display: flex;
  align-items: center;
  height: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  padding: 5px;
  margin-left: 15%;
  margin-top: 15px;
  width: 300px;
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