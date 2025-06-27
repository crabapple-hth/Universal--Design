<script setup>
import {reactive,ref} from "vue";
import router from "@/router/index.js";
import {getCode, login, resetPassword} from "@/net/index.js";
import {getUserInfo} from "@/net/api/user.js";
import {ElMessage} from "element-plus";

const ResetFormRef=ref()
const resetForm=reactive({
  email:"",
  password:"",
  repeat_password:"",
  code:""
})
const coldTime=ref(0)

const submitReset=async (formEl)=>{
  if(!formEl) return
  await formEl.validate((valid,filed)=>{
    if(valid){
      resetPassword({
        email:resetForm.email,
        password:resetForm.password,
        repeatPassword:resetForm.repeat_password,
        code:resetForm.code
      },()=>{
        ElMessage.success("密码重置成功")
        router.push("/login")
        console.info("重置成功")
      })
    }else {
      console.log("error submit",filed)
    }
  })
}

const validateEmail=(rule,value,callback)=>{
  const patter=/^\w+((-\w+)|(\.\w+))*@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/
  if (!patter.test(resetForm.email)){
    callback(new Error("请输入合法的邮箱地址"))
  }else {
    callback()
  }
}

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
  } else if (value!==resetForm.password){
    callback(new Error("两次输入的密码不一致"))
  }else {
    callback()
  }
}

const rules=reactive({
  email:[
    {validator:validateEmail,trigger: "blur"},
    {required:true,message:"请输入邮箱",trigger:"blur"}
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

const askCode = () => {
  ResetFormRef.value.validateField('email', (valid) => {
    if (valid) {
      getCode(resetForm.email, 2,coldTime, () => {
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
  <div>
    <div class="reset_block">
      <div style="margin-bottom: 20px;font-size: 30px;text-align: center">
        重置密码
        <div style="font-size: 13px;color: #bababa;text-align: center">
          使用邮箱重置密码
        </div>
      </div>
      <el-form  ref="ResetFormRef"
                :model="resetForm" :rules="rules">
        <el-form-item>
          <el-input class="input" v-model="resetForm.email" placeholder="输入注册邮箱"/>
        </el-form-item>
        <el-form-item>
          <el-input class="input" v-model="resetForm.password" placeholder="输入新密码"/>
        </el-form-item>
        <el-form-item>
          <el-input class="input" v-model="resetForm.repeat_password" placeholder="再次输入新密码"/>
        </el-form-item>
        <el-form-item>
          <el-input v-model="resetForm.code" style="width: 150px" placeholder="验证码"/>
          <el-button @click="askCode" style="margin-left: 50px"
                     :disabled="coldTime>0||!resetForm.email">{{coldTime>0 ? '请稍后'+coldTime+'秒':'获取验证码'}}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 300px" type="primary"  @click="submitReset(ResetFormRef)">重置密码</el-button>
        </el-form-item>
      </el-form>
      <el-link @click="router.push('/login')" style="margin-left: 130px">返回登录</el-link>
    </div>
  </div>
</template>

<style scoped>
  .reset_block{
    width: 100%;
    margin-top: 200px;
    margin-left: 20%;
  }

  .input{
    height:30px;
    width: 300px;
    border: none;
  }
</style>