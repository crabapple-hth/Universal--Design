<script setup>
import {View} from '@element-plus/icons-vue'
import {reactive,ref,inject} from "vue";
import {login} from "@/net/index.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import Register from "@/components/AuthorizePage/Register.vue";
import {getUserInfo} from "@/net/api/user.js";

const formRef=ref()
const register=ref(false)
const loading=inject("userLoading")

const ruleForm=reactive({
  username:"",
  password:""
})

const rules=reactive({
  username:[
    {required:true,message:"用户名不能为空",trigger:"blur"},
    {min:1,max:20,message: "用户名长度不能超过20个字符",trigger: "blur"}
  ],
  password:[
    {required:true,message:"密码不能为空",trigger:"blur"},
    {min:6,max:20,message: "用户名长度不能超过20个字符",trigger: "blur"}
  ]
})

const submitForm=async (formEl)=>{
  if(!formEl) return
  await formEl.validate((valid,filed)=>{
    if(valid){
      login(ruleForm.username,ruleForm.password,1,
          ()=>{
            getUserInfo(loading)
            router.push("/index")
          })
    }else {
      console.log("error submit",filed)
    }
  })
}

const showRegister = () => {
  register.value = !register.value;
}
const updateRegister=(val)=>{
  register.value=val;
}

</script>

<template>
  <div class="login_page">
    <div class="Login_title">
      论坛
    </div>
    <div class="login_form" v-if="!register">
      <el-menu mode="horizontal" style="background-color: rgba(241, 234, 234, 0)">
        <el-menu-item>密码登录</el-menu-item>
      </el-menu>
      <el-form
          ref="formRef"
          :model="ruleForm"
          :rules="rules">
        <el-form-item prop="username">
          <el-input class="input" v-model="ruleForm.username"  placeholder="请输入用户名或邮箱"/>
        </el-form-item>
        <el-form-item prop="password">
          <el-input class="input" v-model="ruleForm.password"  placeholder="请输入密码" type="password" :suffix-icon="View"/>
        </el-form-item>
        <div style="text-align: right;margin-right: 130px">忘记密码</div>
        <el-form-item>
          <el-button @click="submitForm(formRef)" type="primary" style="margin-left: 100px;width: 300px;margin-top: 20px" size="default">登录</el-button>
        </el-form-item>
      </el-form>
      <el-divider style="" content-position="center">其他登录方式</el-divider>
      <div style="text-align: center">QQ,WX,WB</div>
      <div style="display: flex;justify-content: center">
        <div class="register_link" @click="()=>{register=!register}">没有账号？立即注册</div>
      </div>
    </div>
    <Register v-if="register" @update:register="updateRegister" />
  </div>
</template>

<style scoped>
html,body{
  height: 100%;
  margin: 0;
  padding: 0;
}
.login_page{
  background: url("../../assets/loginImg.jpg") center center no-repeat;
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
  background-color: rgba(241, 234, 234,0.5);
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



:deep(.el-form-item__error){
  left: 100px;
  font-size: 15px;
}

:deep(.el-divider__text){
  background-color: rgba(241, 234, 234, 0);
}

:deep(.el-message el-message--warning){
  position: absolute !important;
  left: 50px;
}

.register_link{
  margin-top: 30px;
  width: 150px;
}

.register_link:hover{
  color: green;
  cursor: default;
}


</style>