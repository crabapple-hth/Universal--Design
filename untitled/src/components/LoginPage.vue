<script setup>
import {View} from '@element-plus/icons-vue'
import {reactive} from "vue";

const form=reactive({
  username:'',
  password:''
})

const login=()=>{
  console.log(form)
}

</script>

<template>
  <div class="login_page">
    <div class="Login_title">
      论坛
    </div>
    <div class="login_form">
      <div style="text-align: center;font-size: 20px">登录</div>
      <el-menu mode="horizontal" style="background-color: rgba(241, 234, 234, 0.76)">
        <el-menu-item>密码登录</el-menu-item>
      </el-menu>
      <el-form>
        <el-input class="input" v-model="form.username" placeholder="请输入用户名或邮箱"/>
        <el-input class="input" v-model="form.password" placeholder="请输入密码" type="password" :suffix-icon="View"/>
        <div style="text-align: right;margin-right: 130px">忘记密码</div>
        <el-button @click="login" type="primary" style="margin-left: 100px;width: 300px;margin-top: 20px" size="default">登录</el-button>
      </el-form>
      <el-divider style="" content-position="center">其他登录方式</el-divider>
      <div style="text-align: center">QQ,WX,WB</div>
    </div>
  </div>
</template>

<style scoped>
html,body{
  height: 100%;
  margin: 0;
  padding: 0;
}
.login_page{
  background: url("../assets/background.jpg") center center no-repeat;
  width: 100%;
  height: 100vh;
  background-size: cover;
  position: relative;
}
.Login_title{
  font-size: 50px;
  align-items: center;
  text-align: center;
  padding-top: 6%;
}

.login_form{
  border: 1px solid ghostwhite;
  width: 500px;
  height: 500px;
  background-color: rgba(241, 234, 234, 0.76);
  position: absolute;
  margin-top: 10px;
  top: 50%;  /* 将 login_form 垂直居中对齐  */
  left: 50%; /* 将 login_form 水平居中对齐 */
  transform: translate(-50%, -50%); /* 调整 login_form 的位置 */
  border-radius: 10px; /* 增加圆角 */
}

.input{
  height:40px;
  width: 300px;
  margin-left: 100px;
  border: none;
  margin-top: 20px
}

</style>