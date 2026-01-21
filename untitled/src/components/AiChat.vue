<script setup>
import {ref} from "vue";
import {sendChat} from "@/net/api/aichat.js";

const textareaRef = ref(null);

const sendMessage = () => {
    sendChat(textareaRef.value, () => {
      textareaRef.value="";
    });
  }

</script>

<template>
  <div class="ai-chat">
    <div class="ai-chat-history">
      历史记录
    </div>
    <div class="ai-chat-window">
      聊天框
      <div>
        <el-input
            type="textarea"
            :rows="1"
            style="width: 240px"
            v-model="textareaRef"
        />
        <el-button @click="sendMessage">发送</el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.ai-chat{
  display: flex;
  width: 100%;
  height: 88vh;

  .ai-chat-history{
    width: 20%;
    height: 100%;
    border-right: 1px solid #ccc;
  }

  .ai-chat-window{
    width: 80%;
    height: 100%;
  }
}
</style>