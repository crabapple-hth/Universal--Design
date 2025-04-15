<script setup>
import { ref, reactive, onMounted, onUnmounted, shallowRef, watch,computed ,inject} from 'vue';
import {getAccount, getTopics, getTypeList, logout} from '../net/index.js';
import router from "@/router/index.js";
import { ElMessage } from "element-plus";
import recommendTopic from "@/components/Topic/recommendTopic.vue";
import { useRoute } from "vue-router";
import follow from "@/components/Topic/follow.vue";
import hotTopic from "@/components/Topic/hotTopic.vue";
import TopicEditor from "@/components/Topic/TopicEditor.vue";
import { useStore } from "@/store/index.js";
import {CollectionTag} from "@element-plus/icons-vue";
import UserInfo from "@/components/Topic/userInfo.vue";



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
  typeId.value = id;
  console.log(typeId.value)
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
          <el-menu-item index="/login">校园动态</el-menu-item>
          <el-menu-item index="/market">跳蚤市场</el-menu-item>
          <el-menu-item index="/talk">趣事闲谈</el-menu-item>
          <el-menu-item index="/any">其他</el-menu-item>
          <div class="search-input">
            <label style="width: 200px">
              <input class="input_index" placeholder="搜索感兴趣的帖子" />
              <img src="../assets/search.png" class="btn_pic" alt="搜索" />
            </label>
          </div>
          <div>
            <el-button style="margin-top: 15px; margin-left: 20px" type="primary" @click="creatTopic">发表帖子</el-button>
          </div>
          <user-info />
        </el-menu>
      </el-header>
      <el-container style="margin-top: 50px">
        <el-main class="main">
          <div>
            <el-menu mode="horizontal"
                     default-active="0"
                     @select="handleTypeSelect"
                     >
              <el-menu-item index="0" style="width: 100px;margin-right: 15px">推荐</el-menu-item>
              <el-menu-item :index="item.id" v-for="item in store.forum"
                            style="width: 100px;margin-right: 15px">{{item.name}}</el-menu-item>
            </el-menu>
          </div>
          <div>
            <recommend-topic :is="comp"  :topic-type="typeId"/>
          </div>
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
            天气
          </div>
        </el-aside>
      </el-container>
      <topic-editor :show="show" @success="show = false" @close="show = false" />
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