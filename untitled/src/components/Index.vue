<script setup>
import { ref, reactive, onMounted, onUnmounted, shallowRef, watch,computed ,inject} from 'vue';
import {
  apiForumWeather,
  apiNotificationDelete, apiNotificationDeleteAll,
  apiNotificationList,
  getAccount,
  getTopics,
  getTypeList,
  logout
} from '../net/index.js';
import router from "@/router/index.js";
import { ElMessage } from "element-plus";
import recommendTopic from "@/components/Topic/recommendTopic.vue";
import { useRoute } from "vue-router";
import follow from "@/components/Topic/follow.vue";
import hotTopic from "@/components/Topic/hotTopic.vue";
import TopicEditor from "@/components/Topic/TopicEditor.vue";
import { useStore } from "@/store/index.js";
import {Bell, Calendar, Check, CollectionTag} from "@element-plus/icons-vue";
import UserInfo from "@/components/Topic/userInfo.vue";
import Weather from "@/components/Weather.vue";

const weather=reactive({
  location:{},
  now:{},
  hourly:[],
  success:false
})

const typeId=ref(0)
const avatar_form = ref(false);
const route = useRoute();
const show = ref(false);
const store = useStore();
const loading=inject("userLoading")

getTypeList((data)=>{store.forum.types=data})

const comp=shallowRef(recommendTopic);
const creatTopic = () => {
  show.value = true;
};

const handleSelect = (key, keyPath) => {
  console.log(key, keyPath);
};

const handleTypeSelect=(id)=>{
  typeId.value = Number(id);
};


const out = () => {
  logout(() => {
    ElMessage.success("退出登录成功");
  });
  router.push('/login');
};


const toggleAvatarForm = () => {
  avatar_form.value = !avatar_form.value;
};

const navigateTo = (path) => {
  avatar_form.value = false;
  router.push(path);
};

navigator.geolocation.getCurrentPosition(position => {
  const longitude = position.coords.longitude
  const latitude = position.coords.latitude
  apiForumWeather(longitude, latitude, data => {
    Object.assign(weather, data)
    weather.success = true
  })
}, error => {
  console.info(error)
  ElMessage.warning('位置信息获取超时，请检测网络设置')
  apiForumWeather(116.40529, 39.90499, data => {
    Object.assign(weather, data)
    weather.success = true
  })
}, {
  timeout: 3000,
  enableHighAccuracy: true
})



const notification = ref([])

const loadNotification =
    () => apiNotificationList((data) => notification.value = data)
loadNotification()

function confirmNotification(id, url) {
  apiNotificationDelete(id, () => {
    loadNotification()
    window.open(url)
  })
}

function deleteAllNotification() {
  apiNotificationDeleteAll(loadNotification)
}


watch(
    () => route,
    (newValue, oldValue) => {
      if (newValue.path === "/") comp.value = recommendTopic;
      if (newValue.path === "/follow") comp.value = follow;
      if (newValue.path === "/hot") comp.value = hotTopic;
    },
    { deep: true, immediate: true }
);

const activeMenu = computed(() => route.path);

onMounted(() => { });
</script>

<template>
  <div class="common-layout" v-loading="loading" element-loading-text="正在进入，请稍候....">
    <el-container style="width: 100%; padding: 0; margin: 0; height: 100%">
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
          <el-menu-item index="/actives">校园动态</el-menu-item>
          <div class="search-input">
            <input type="text" placeholder="搜索..." style="border: none; outline: none; flex-grow: 1; padding: 5px;">
            <button style="background: none; border: none; cursor: pointer; padding: 5px;">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <circle cx="11" cy="11" r="8"></circle>
                <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
              </svg>
            </button>
          </div>
          <div>
            <el-button style="margin-top: 15px; margin-left: 60px" type="primary" @click="creatTopic">发表帖子</el-button>
          </div>
          <user-info style="margin-left:7%" >
            <el-popover placement="bottom" :width="350" trigger="click">
              <template #reference>
                <el-badge is-dot :hidden="!notification.length">
                  <div class="notification">
                    <el-icon><Bell/></el-icon>
                    <div style="font-size: 10px">消息</div>
                  </div>
                </el-badge>
              </template>
              <el-empty :image-size="80" description="暂时没有未读消息哦~" v-if="!notification.length"/>
              <el-scrollbar :max-height="500" v-else>
                <div v-for="item in notification" class="notification-item"
                            @click="confirmNotification(item.id, item.url)">
                  <div>
                    <el-tag size="small" :type="item.type">消息</el-tag>&nbsp;
                    <span style="font-weight: bold">{{item.title}}</span>
                  </div>
                  <el-divider style="margin: 7px 0 3px 0"/>
                  <div style="font-size: 13px;color: grey">
                    {{item.content}}
                  </div>
                </div>
              </el-scrollbar>
              <div style="margin-top: 10px">
                <el-button size="small" type="info" :icon="Check" @click="deleteAllNotification"
                           style="width: 100%" plain>清除全部未读消息</el-button>
              </div>
            </el-popover>
          </user-info>
        </el-menu>
      </el-header>
      <el-container style="margin-top: 50px">
        <el-main class="main">
          <router-view v-slot="{Component}">
            <keep-alive>
              <component :is="Component" />
            </keep-alive>
          </router-view>
        </el-main>
        <el-aside class="side" width="200px">
          <div class="message">
            <div style="width: 100%;display: flex" >
              <el-icon size="25" style="width: 20px;height: 30px;margin-top: 13px">
                <CollectionTag />
              </el-icon>
              <p style="width: 50px">公告</p>
            </div>
            <el-divider style="margin-top: 0"/>
            <div style="font-size: 15px;color: grey;margin: 8px">此论坛是计算属性允许我们声明性地计算衍生值。然而在有些情
              况下，我们需要在状态变化时执行一些“副作用”：例如更改 DOM，或是根据异步操作的结果去修改另一处的状态。</div>
          </div>
          <div style="margin-top:20px ">
            <el-icon><Calendar/></el-icon>
            天气信息
            <el-divider style="margin: 10px 0"/>
            <weather :data="weather"/>
          </div>
        </el-aside>
      </el-container>
      <topic-editor :show="show" @success="show = false" @close="show = false" />
    </el-container>
  </div>
</template>


<style scoped>
.notification-item {
  transition: .3s;
  &:hover {
    cursor: pointer;
    opacity: 0.7;
  }
}

.notification {
  font-size: 22px;
  line-height: 14px;
  text-align: center;
  transition: color .3s;

  &:hover {
    color: grey;
    cursor: pointer;
  }
}

.common-layout {
  height: 100%;
  overflow-y: auto;
}

.header {
  width: 100vw;
  position: fixed;
  z-index: 1000;
}

.el-menu-demo{
  width: 100%;
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
  width: 280px;
  height: 25px;
  outline: none;
  border: none;
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


.side {
  margin-right: 50px;
  width: 20%;
  margin-top: 20px;
}

.el-menu--horizontal > .el-menu-item:nth-child(1) {
  margin-right: auto;
}

:deep(.el-divider--horizontal){
  margin: 10px 0;
}

.message{
  border-radius: 5px;
}
</style>