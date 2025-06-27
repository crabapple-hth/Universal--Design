<script setup>

</script>

<template>
  <div class="login_page">
    <div class="login_main">
      <div class="introduce">
        <img style=" width:100%;height: 100%" src="../../assets/loginImg.jpg">
      </div>
      <router-view/>
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
  background: white;
  width: 100%;
  height: 100vh;
  background-size: cover;
  position: relative;
}

.login_main{
  display: flex;
  height: 100vh;

  .introduce{
    border-right:1px solid gainsboro;
    width: 70%;
  }
}


.login_form{
  width: 500px;
  height: 500px;
  margin-top: 10px;
  margin-left: 10px;
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
  color: hotpink;
  cursor: default;
}

.reset_password:hover{
  color: blue;
  cursor: default;
}


</style>