<script setup>
import {RouterView} from "vue-router";
import {ref,provide,onMounted} from "vue";
import {getUserInfo} from "@/net/api/user.js";
import {isUnauthorized} from "@/net/index.js";
import zhCn from "element-plus/es/locale/lang/zh-cn";

const loading=ref(false)
provide('userLoading',loading)

onMounted(
    ()=>{
      if (!isUnauthorized()){
        getUserInfo(loading)
      }
    }
)

</script>

<template >
  <el-config-provider :locale="zhCn">
    <router-view/>
  </el-config-provider>
</template>


<style scoped>

</style>
