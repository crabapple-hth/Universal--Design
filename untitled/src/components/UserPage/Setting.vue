<script setup>

import {ArrowLeft} from "@element-plus/icons-vue";
import {reactive,onMounted} from "vue";
import {getAccessToken, getInfo, takeAccessToken, updateInfo} from "@/net/index.js";
import {ElMessage} from "element-plus";
import axios from "axios";
import {useStore} from "@/store/index.js";
import router from "@/router/index.js";


const info=reactive({
  username:"",
  sex:"",
  birthday:"",
  introduction:""
})

const store=useStore()

const init=()=>{
  getInfo((data)=>{
    info.username=data.username
    info.sex=data.sex
    info.introduction=data.introduction
    info.birthday=data.birthday
  })
}

const update=(info)=>{
  updateInfo(info,()=>{
    ElMessage.success("修改成功")
  })
}

const onBack=()=>{
  router.back();
}

function beforeAvatarUpload(rawFile) {
  if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
    ElMessage.error('头像只能是 JPG/PNG 格式的')
    return false
  } else if(rawFile.size / 1024 > 100) {
    ElMessage.error('头像大小不能大于 100KB')
    return false
  }
  return true
}

function uploadSuccess(response){
  ElMessage.success('头像上传成功')
  store.user.avatar = response.data
}

onMounted(()=>{
  init()
})
</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <el-page-header :icon="ArrowLeft" @back="onBack" style="margin-top: 20px">
          <template #content>
            <span class="text-large font-600 mr-3"> 个人信息编辑 </span>
          </template>
        </el-page-header>
      </el-header>
      <el-main class="main">
        <div style="margin-top: 100px;float: left;height: 500px;margin-right: 50px">
          <el-image class="avatar" :src="store.avatarUrl"/>
          <el-upload
              :action="axios.defaults.baseURL + '/api/image/avatar'"
              :headers="getAccessToken()"
              :before-upload="beforeAvatarUpload"
              :on-success="uploadSuccess"
              :show-file-list="false">
            <el-button type="primary" style="width: 120px">修改头像</el-button>
          </el-upload>
        </div>
        <div>
          <el-form style="margin-top: 120px">
            <el-form-item style="margin-top: 50px" label="用户名:">
              <el-input v-model="info.username"/>
            </el-form-item>
            <el-form-item style="margin-top: 50px" label="性别:">
              <el-radio-group v-model="info.sex">
                <el-radio value="MALE">男</el-radio>
                <el-radio value="FEMALE">女</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item style="margin-top: 50px" label="生日">
              <el-col>
                <el-date-picker v-model="info.birthday"
                                type="date"
                                placeholder="输入日期"/>
              </el-col>
            </el-form-item>
            <el-form-item style="margin-top: 50px" label="个人简介">
              <el-input v-model="info.introduction" type="textarea" placeholder="介绍一下自己吧！"/>
            </el-form-item>
            <el-form-item style="margin-top: 50px">
              <el-button type="primary" @click="update(info)">提交修改</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<style scoped>
  .main{
    border: 1px solid gainsboro;
    width: 70%;
    margin: auto;
    border-radius: 5px;
  }

  .avatar{
    border:1px solid grey;
    height: 120px;
  }
</style>