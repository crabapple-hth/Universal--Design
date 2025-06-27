<script setup>

import {View} from "@element-plus/icons-vue";
import {ref,reactive} from "vue";
import {getCode, register} from "@/net/index.js";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";



const registerFormRef=ref()
const coldTime=ref(0)

const emits=defineEmits(["update:register"])

const showLogin = () => {
  emits("update:register",false);
};

const ruleForm=reactive({
  email:"",
  username:"",
  password:"",
  repeatPassword:"",
  code:""
})


const validatePass=(rule,value,callback)=>{
  if (value===""){
    callback(new Error("密码不能为空"))
  }else {
    callback()
  }
}

const validatePass2=(rule,value,callback)=>{
  if (value === ""){
    callback(new Error("请再次输入密码"))
  } else if (value!==ruleForm.password){
    callback(new Error("两次输入的密码不一致"))
  }else {
    callback()
  }
}

const validateEmail=(rule,value,callback)=>{
  const patter=/^\w+((-\w+)|(\.\w+))*@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/
  if (!patter.test(ruleForm.email)){
    callback(new Error("请输入合法的邮箱地址"))
  }else {
    callback()
  }
}

const rules=reactive({
  email:[
    {validator:validateEmail,trigger: "blur"},
    {required:true,message:"请输入邮箱",trigger:"blur"}
  ],
  username:[
    {required:true,message:"用户名不能为空",trigger:"blur"},
    {min:1,max:20,message: "用户名长度不能超过20个字符",trigger: "blur"}
  ],
  password:[
    {validator:validatePass,trigger:"blur"},
    {min:6,max:20,message:"密码长度应在6-20之间",trigger: "blur"}
  ],
  repeatPassword:[
    {validator:validatePass2,trigger:"blur"}
  ],
  code:[{min:6,max:6,message:"请输入正确的验证码",trigger:"blur"}]
})


const submitRegisterForm = async (formEl) =>{
  if (!formEl) return
  await formEl.validate((valid, filed) => {
    if (valid) {
      register({
        email:ruleForm.email,
        username:ruleForm.username,
        password:ruleForm.password,
        code:ruleForm.code
      },()=>{
        ElMessage.success("注册成功")
        showLogin()
      })
    } else {
      console.log("Error submit", filed);
    }
  });
};

const askCode = () => {
  registerFormRef.value.validateField('email', (valid) => {
    if (valid) {
      getCode(ruleForm.email,1, coldTime, () => {
        ElMessage.success("请求验证码成功");
      }, (message) => {
        ElMessage.warning(message);
      });
    } else {
      ElMessage.warning("请输入合法的邮箱地址");
    }
  });
};
</script>

<template>
  <div class="register_form">
    <div style="text-align: center;font-size: 20px;margin-bottom: 20px;margin-left: 90px">欢迎注册</div>
    <el-form
        ref="registerFormRef"
        :model="ruleForm"
        :rules="rules">
      <el-form-item prop="email">
        <el-input class="input" v-model="ruleForm.email" placeholder="请输入邮箱"/>
      </el-form-item>
      <el-form-item prop="username">
        <el-input class="input" v-model="ruleForm.username"  placeholder="请输入用户名"/>
      </el-form-item>
      <el-form-item prop="password">
        <el-input class="input" v-model="ruleForm.password"  placeholder="请输入密码" type="password" :suffix-icon="View"/>
      </el-form-item>
      <el-form-item prop="repeatPassword">
        <el-input class="input" v-model="ruleForm.repeatPassword" placeholder="请再次输入密码" type="password" :suffix-icon="View"/>
      </el-form-item>
      <el-form-item prop="code">
        <el-input class="code" v-model="ruleForm.code" placeholder="请输入验证码"/>
        <el-button @click="askCode" :disabled="coldTime>0||!ruleForm.email">{{coldTime>0 ? '请稍后'+coldTime+'秒':'获取验证码'}}</el-button>
      </el-form-item>
      <el-form-item>
        <el-button @click="submitRegisterForm(registerFormRef)" type="primary" style="margin-left: 100px;width: 300px;margin-top: 20px" size="default">注册</el-button>
      </el-form-item>
      <el-link style="margin-left: 175px" href="#" @click="router.push('/login')" >已有账号？点击登录</el-link>
    </el-form>
  </div>
</template>

<style scoped>
.register_form{
  margin-top: 100px;
  margin-left: 20px;
}

.input{
  height:40px;
  width: 300px;
  margin-left: 100px;
  border: none;
  margin-top: 10px
}

.code{
  width: 200px;
  margin-left: 100px;
}

:deep(.el-message el-message__warning){
  position: absolute;
}
</style>