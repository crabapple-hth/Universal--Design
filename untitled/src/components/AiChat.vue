<script setup>
import {ref, onMounted, nextTick, computed, reactive} from "vue";
import { sendChat } from "@/net/api/aichat.js";
import { ElMessage } from 'element-plus';

// 消息数据
const messages = ref([]);
const textInput = ref("");
const isLoading = ref(false);
const activeHistory = ref(null);
const chatHistories = ref([]);
const responseMessage = ref("");
// 添加AI回复
const aiMessage = reactive({
  id: Date.now() + 1,
  content: "正在生成中", // 实际应该使用API返回的数据
  sender: "ai",
  timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
  avatar: "🤖"
});

// 模拟历史记录数据
onMounted(() => {
  // 加载历史记录
  loadChatHistories();

  // 初始化一个欢迎消息
  messages.value = [
    {
      id: 1,
      content: "您好！我是AI助手，有什么可以帮您的吗？",
      sender: "ai",
      timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
      avatar: "🤖"
    }
  ];
});

// 加载聊天历史
const loadChatHistories = () => {
  // 这里可以替换为从API获取真实数据
  chatHistories.value = [
    { id: 1, title: "关于Vue3的问题", date: "2023-10-15", messageCount: 5 },
    { id: 2, title: "JavaScript学习", date: "2023-10-14", messageCount: 12 },
    { id: 3, title: "项目优化建议", date: "2023-10-13", messageCount: 8 },
    { id: 4, title: "API接口问题", date: "2023-10-12", messageCount: 6 },
    { id: 5, title: "代码调试帮助", date: "2023-10-11", messageCount: 10 },
  ];

  // 设置第一个历史记录为激活状态
  if (chatHistories.value.length > 0) {
    activeHistory.value = chatHistories.value[0].id;
  }
};

// 发送消息
const sendMessage = async () => {
  if (!textInput.value.trim()) {
    ElMessage.warning("请输入消息内容");
    return;
  }

  // 添加用户消息
  const userMessage = {
    id: Date.now(),
    content: textInput.value,
    sender: "user",
    timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
    avatar: "👤"
  };

  messages.value.push(userMessage);

  // 清空输入框
  const messageToSend = textInput.value;
  textInput.value = "";

  // 显示加载状态
  isLoading.value = true;

  // 滚动到底部
  scrollToBottom();

  try {

    // 调用API发送消息
    sendChat(messageToSend,(data)=>{
     responseMessage.value = data
      aiMessage.content=(data)
    })

    messages.value.push(aiMessage);

    console.log(messages)

    // 滚动到底部
    scrollToBottom();
  } catch (error) {
    console.error("发送消息失败:", error);
    ElMessage.error("发送消息失败，请稍后重试");

    // 添加错误提示消息
    const errorMessage = {
      id: Date.now() + 1,
      content: "抱歉，我暂时无法处理您的请求，请稍后再试。",
      sender: "ai",
      timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
      avatar: "🤖",
      isError: true
    };

    messages.value.push(errorMessage);
  } finally {
    isLoading.value = false;
  }
};

// 通过Enter键发送消息（Ctrl+Enter换行）
const handleKeyDown = (event) => {
  if (event.key === 'Enter' && !event.shiftKey && !event.ctrlKey) {
    event.preventDefault();
    sendMessage();
  }

  // Ctrl+Enter 换行
  if (event.key === 'Enter' && (event.ctrlKey || event.metaKey)) {
    event.preventDefault();
    const cursorPos = event.target.selectionStart;
    const textBefore = textInput.value.substring(0, cursorPos);
    const textAfter = textInput.value.substring(cursorPos);
    textInput.value = textBefore + "\n" + textAfter;

    // 设置光标位置
    nextTick(() => {
      event.target.selectionStart = event.target.selectionEnd = cursorPos + 1;
    });
  }
};

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    const chatWindow = document.querySelector('.message-container');
    if (chatWindow) {
      chatWindow.scrollTop = chatWindow.scrollHeight;
    }
  });
};

// 选择历史记录
const selectHistory = (historyId) => {
  activeHistory.value = historyId;
  // 这里可以加载选中的历史记录消息
  // 模拟加载历史消息
  messages.value = [
    {
      id: 1,
      content: "这是之前的历史对话记录，您之前问过关于Vue3的问题。",
      sender: "ai",
      timestamp: "10:30",
      avatar: "🤖"
    },
    {
      id: 2,
      content: "Vue3的Composition API如何使用？",
      sender: "user",
      timestamp: "10:31",
      avatar: "👤"
    },
    {
      id: 3,
      content: "Vue3的Composition API允许您将相关功能组织在一起，而不是按照选项（data、methods等）分离代码。您可以使用setup函数来使用它。",
      sender: "ai",
      timestamp: "10:32",
      avatar: "🤖"
    }
  ];

  ElMessage.success(`已加载历史对话: ${chatHistories.value.find(h => h.id === historyId)?.title}`);
};

// 新建聊天
const newChat = () => {
  activeHistory.value = null;
  messages.value = [
    {
      id: 1,
      content: "开始新的对话！我是AI助手，有什么可以帮您的吗？",
      sender: "ai",
      timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
      avatar: "🤖"
    }
  ];
  ElMessage.info("开始新的对话");
};

// 删除历史记录
const deleteHistory = (historyId, event) => {
  event.stopPropagation();
  chatHistories.value = chatHistories.value.filter(h => h.id !== historyId);

  if (activeHistory.value === historyId) {
    activeHistory.value = null;
    newChat();
  }

  ElMessage.success("已删除历史记录");
};

// 计算属性：是否有消息
const hasMessages = computed(() => messages.value.length > 0);
</script>

<template>
  <div class="ai-chat">
    <!-- 侧边栏 - 历史记录 -->
    <div class="ai-chat-history">
      <div class="history-header">
        <h3>聊天历史</h3>
        <el-button type="primary" size="small" @click="newChat" class="new-chat-btn">
          <i class="el-icon-chat-dot-round"></i> 新对话
        </el-button>
      </div>

      <div class="history-list">
        <div
            v-for="history in chatHistories"
            :key="history.id"
            class="history-item"
            :class="{ active: activeHistory === history.id }"
            @click="selectHistory(history.id)"
        >
          <div class="history-content">
            <div class="history-title">
              <i class="el-icon-chat-line-round"></i>
              <span>{{ history.title }}</span>
            </div>
            <div class="history-meta">
              <span class="date">{{ history.date }}</span>
              <span class="count">{{ history.messageCount }} 条消息</span>
            </div>
          </div>
          <el-button
              type="danger"
              size="small"
              circle
              @click="deleteHistory(history.id, $event)"
              class="delete-btn"
          >
            <i class="el-icon-delete"></i>
          </el-button>
        </div>

        <div v-if="chatHistories.length === 0" class="empty-history">
          <i class="el-icon-chat-line-round"></i>
          <p>暂无历史记录</p>
        </div>
      </div>
    </div>

    <!-- 主聊天区域 -->
    <div class="ai-chat-window">
      <!-- 聊天消息区域 -->
      <div class="message-container" v-if="hasMessages">
        <div
            v-for="message in messages"
            :key="message.id"
            class="message-wrapper"
            :class="[message.sender, { 'error-message': message.isError }]"
        >
          <div class="message-avatar">
            {{ message.avatar }}
          </div>
          <div class="message-content">
            <div class="message-text" v-html="message.content"></div>
            <div class="message-time">{{ message.timestamp }}</div>
          </div>
        </div>

        <!-- 加载指示器 -->
        <div v-if="isLoading" class="message-wrapper ai">
          <div class="message-avatar">
            🤖
          </div>
          <div class="message-content">
            <div class="message-text loading">
              <span class="dot"></span>
              <span class="dot"></span>
              <span class="dot"></span>
            </div>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="empty-chat">
        <div class="empty-icon">
          <i class="el-icon-chat-line-square"></i>
        </div>
        <h3>开始与AI对话</h3>
        <p>选择左侧的历史记录或开始新的对话</p>
      </div>

      <!-- 输入区域 -->
      <div class="input-area">
        <div class="input-wrapper">
          <el-input
              type="textarea"
              :rows="3"
              :maxlength="1000"
              v-model="textInput"
              placeholder="输入您的问题（Enter发送，Ctrl+Enter换行）"
              @keydown="handleKeyDown"
              :disabled="isLoading"
              resize="none"
              class="message-input"
          >
          </el-input>
          <div class="input-actions">
            <span class="char-count">{{ textInput.length }}/1000</span>
            <el-button
                type="primary"
                @click="sendMessage"
                :loading="isLoading"
                :disabled="!textInput.trim()"
                class="send-btn"
            >
              <template #loading>
                <span>发送中</span>
              </template>
              <template #default>
                <i class="el-icon-position"></i> 发送
              </template>
            </el-button>
          </div>
        </div>

        <div class="quick-actions">
          <el-button size="small" @click="textInput = '帮我解释一下这段代码的含义'">
            解释代码
          </el-button>
          <el-button size="small" @click="textInput = '帮我优化这段代码的性能'">
            优化代码
          </el-button>
          <el-button size="small" @click="textInput = '帮我生成一个响应式布局的CSS代码'">
            CSS生成
          </el-button>
          <el-button size="small" @click="textInput = '帮我检查这段代码的错误'">
            代码调试
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.ai-chat {
  display: flex;
  width: 100%;
  height: 88vh;
  background-color: #f5f7fa;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);

  .ai-chat-history {
    width: 260px;
    height: 100%;
    background-color: #fff;
    border-right: 1px solid #e4e7ed;
    display: flex;
    flex-direction: column;

    .history-header {
      padding: 20px;
      border-bottom: 1px solid #e4e7ed;
      display: flex;
      justify-content: space-between;
      align-items: center;

      h3 {
        margin: 0;
        font-size: 16px;
        font-weight: 600;
        color: #303133;
      }

      .new-chat-btn {
        padding: 5px 10px;
        font-size: 12px;
      }
    }

    .history-list {
      flex: 1;
      overflow-y: auto;
      padding: 10px;

      .history-item {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 12px;
        margin-bottom: 8px;
        border-radius: 8px;
        cursor: pointer;
        transition: all 0.2s;
        border: 1px solid transparent;

        &:hover {
          background-color: #f0f2f5;
          border-color: #dcdfe6;

          .delete-btn {
            opacity: 1;
          }
        }

        &.active {
          background-color: #ecf5ff;
          border-color: #409eff;

          .history-title {
            color: #409eff;
          }
        }

        .history-content {
          flex: 1;
          overflow: hidden;

          .history-title {
            display: flex;
            align-items: center;
            font-size: 14px;
            color: #303133;
            margin-bottom: 4px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;

            i {
              margin-right: 6px;
              font-size: 14px;
            }
          }

          .history-meta {
            display: flex;
            justify-content: space-between;
            font-size: 12px;
            color: #909399;

            .count {
              background-color: #f0f2f5;
              padding: 1px 6px;
              border-radius: 10px;
            }
          }
        }

        .delete-btn {
          opacity: 0;
          transition: opacity 0.2s;
          margin-left: 8px;
          width: 24px;
          height: 24px;
          display: flex;
          align-items: center;
          justify-content: center;

          i {
            font-size: 12px;
          }
        }
      }

      .empty-history {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        height: 200px;
        color: #c0c4cc;

        i {
          font-size: 48px;
          margin-bottom: 16px;
        }

        p {
          font-size: 14px;
          margin: 0;
        }
      }
    }
  }

  .ai-chat-window {
    flex: 1;
    height: 100%;
    display: flex;
    flex-direction: column;

    .message-container {
      flex: 1;
      overflow-y: auto;
      padding: 20px;
      display: flex;
      flex-direction: column;
      gap: 16px;

      .message-wrapper {
        display: flex;
        max-width: 80%;

        &.user {
          align-self: flex-end;
          flex-direction: row-reverse;

          .message-avatar {
            margin-left: 12px;
            margin-right: 0;
            background-color: #409eff;
          }

          .message-content {
            align-items: flex-end;

            .message-text {
              background-color: #409eff;
              color: white;
              border-radius: 18px 18px 4px 18px;
            }
          }
        }

        &.ai {
          align-self: flex-start;

          .message-avatar {
            background-color: #67c23a;
          }

          .message-content {
            align-items: flex-start;

            .message-text {
              background-color: white;
              color: #303133;
              border-radius: 18px 18px 18px 4px;
              box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
            }
          }
        }

        &.error-message {
          .message-avatar {
            background-color: #f56c6c;
          }

          .message-content .message-text {
            background-color: #fef0f0;
            color: #f56c6c;
            border: 1px solid #fbc4c4;
          }
        }

        .message-avatar {
          width: 40px;
          height: 40px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 20px;
          flex-shrink: 0;
          margin-right: 12px;
          color: white;
        }

        .message-content {
          display: flex;
          flex-direction: column;
          max-width: calc(100% - 52px);

          .message-text {
            padding: 12px 16px;
            font-size: 14px;
            line-height: 1.5;
            word-break: break-word;

            &.loading {
              display: flex;
              align-items: center;
              justify-content: center;
              background-color: white;
              width: 60px;
              height: 40px;
              padding: 0;

              .dot {
                width: 8px;
                height: 8px;
                border-radius: 50%;
                background-color: #909399;
                margin: 0 4px;
                animation: dotPulse 1.5s infinite ease-in-out;

                &:nth-child(2) {
                  animation-delay: 0.2s;
                }

                &:nth-child(3) {
                  animation-delay: 0.4s;
                }
              }
            }
          }

          .message-time {
            font-size: 12px;
            color: #909399;
            margin-top: 6px;
          }
        }
      }
    }

    .empty-chat {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      color: #c0c4cc;

      .empty-icon {
        font-size: 80px;
        margin-bottom: 20px;
        opacity: 0.5;
      }

      h3 {
        font-size: 18px;
        margin-bottom: 8px;
        color: #606266;
      }

      p {
        font-size: 14px;
        margin: 0;
      }
    }

    .input-area {
      padding: 20px;
      border-top: 1px solid #e4e7ed;
      background-color: white;

      .input-wrapper {
        position: relative;

        .message-input {
          :deep(.el-textarea__inner) {
            padding-right: 80px;
            font-size: 14px;
            line-height: 1.5;
            border-radius: 12px;
            border-color: #dcdfe6;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);

            &:focus {
              border-color: #409eff;
              box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
            }

            &:disabled {
              background-color: #f5f7fa;
              cursor: not-allowed;
            }
          }
        }

        .input-actions {
          position: absolute;
          bottom: 10px;
          right: 12px;
          display: flex;
          align-items: center;
          gap: 12px;

          .char-count {
            font-size: 12px;
            color: #909399;
          }

          .send-btn {
            padding: 6px 16px;
            border-radius: 8px;
            font-weight: 500;
          }
        }
      }

      .quick-actions {
        display: flex;
        gap: 8px;
        margin-top: 12px;
        flex-wrap: wrap;

        .el-button {
          border-radius: 16px;
          font-size: 12px;
          padding: 4px 12px;
        }
      }
    }
  }
}

@keyframes dotPulse {
  0%, 60%, 100% {
    transform: translateY(0);
    opacity: 0.6;
  }
  30% {
    transform: translateY(-6px);
    opacity: 1;
  }
}
</style>