<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { apiActivityDetail, apiActivityRegister, apiActivityCancel, apiActivityCheckRegistration } from '@/net/api/activity'
import { ElMessage } from 'element-plus'
import { useStore } from '@/store'
import {ArrowLeft, Clock, Location, User} from "@element-plus/icons-vue";
import axios from "axios";

const route = useRoute()
const router = useRouter()
const store = useStore()
const activity = ref(null)
const loading = ref(true)
const error = ref(null)
const isRegistered = ref(false)

// 获取活动详情
const fetchActivityDetail = async () => {
  try {
    const activityId = route.query.id
    if (!activityId) {
      error.value = '活动 ID 不存在'
      ElMessage.error('活动 ID 不存在')
      return
    }
    
    apiActivityDetail(activityId, data => {
      if (data.code === 200) {
        activity.value = data.data
        // 检查用户是否已报名
        checkRegistrationStatus()
      } else {
        error.value = '获取活动详情失败'
        ElMessage.error('获取活动详情失败')
      }
    })
  } catch (err) {
    error.value = '获取活动详情失败'
    console.error('获取活动详情失败:', err)
    ElMessage.error('获取活动详情失败')
  } finally {
    loading.value = false
  }
}

// 检查用户是否已报名
const checkRegistrationStatus = () => {
  if (!store.user.userid || !activity.value) {
    return
  }
  
  // 调用后端接口检查用户是否已报名
  apiActivityCheckRegistration(route.query.id, data => {
    if (data.code === 200) {
      isRegistered.value = data.data
    } else {
      console.error('检查报名状态失败:', data.message)
    }
  })
}

// 返回活动列表
const goBack = () => {
  router.push('/actives')
}

// 处理报名
const handleJoin = async () => {
  if (!store.user.userid) {
    ElMessage.warning('请先登录')
    return
  }
  
  try {
    const activityId = route.query.id
    if (!activityId) {
      ElMessage.error('活动 ID 不存在')
      return
    }
    
    if (isRegistered.value) {
      // 取消报名
      apiActivityCancel(activityId, () => {
          ElMessage.success('取消报名成功')
          isRegistered.value = false
          activity.value.currentParticipants--
      })
    } else {
      // 报名活动
      apiActivityRegister(activityId, () => {
          ElMessage.success('报名成功')
          isRegistered.value = true
          activity.value.currentParticipants++
      })
    }
  } catch (err) {
    console.error('操作失败:', err)
    ElMessage.error('操作失败')
  }
}

onMounted(() => {
  fetchActivityDetail()
})
</script>

<template>
  <div class="activity-detail-container">
    <div v-if="loading" class="loading">
      <el-skeleton :rows="10" animated />
    </div>
    
    <div v-else-if="error" class="error">
      {{ error }}
      <el-button @click="goBack" type="primary">返回活动列表</el-button>
    </div>
    
    <div v-else-if="activity" class="activity-detail">
      <el-button @click="goBack" type="primary" plain>
        <el-icon><ArrowLeft /></el-icon> 返回活动列表
      </el-button>
      
      <div class="activity-header">
        <h1 class="activity-title">{{ activity.title }}</h1>
        <div class="activity-meta">
          <span class="meta-item">
            <el-icon><Clock /></el-icon>
            {{ new Date(activity.startTime).toLocaleString() }}
          </span>
          <span class="meta-item">
            <el-icon><Location /></el-icon>
            {{ activity.location }}
          </span>
          <span class="meta-item">
            <el-icon><User /></el-icon>
            主办方：{{ activity.organizer }}
          </span>
        </div>
        <div class="activity-status">
          <el-tag :type="activity.status === 1 ? 'success' : activity.status === 2 ? 'warning' : 'info'">
            {{ activity.status === 1 ? '未开始' : activity.status === 2 ? '进行中' : '已结束' }}
          </el-tag>
          <span class="participants">
            已报名：{{ activity.currentParticipants }}/{{ activity.maxParticipants }}人
          </span>
        </div>
      </div>
      
      <div class="activity-content">
        <div class="activity-image">
          <img :src="`${axios.defaults.baseURL}/images/${activity.coverImage || 'default-activity.jpg'}`" :alt="activity.title">
        </div>
        
        <div class="activity-description">
          <h2>活动详情</h2>
          <p>{{ activity.description }}</p>
        </div>
        
        <div class="activity-requirements" v-if="activity.requirements">
          <h2>参与要求</h2>
          <p>{{ activity.requirements }}</p>
        </div>
      </div>
      
      <div class="activity-footer">
        <el-button 
          :type="isRegistered ? 'danger' : 'primary'"
          :disabled="activity.status !== 1"
          @click="handleJoin">
          {{ isRegistered ? '取消报名' : '立即报名' }}
        </el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.activity-detail-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.loading, .error {
  text-align: center;
  padding: 40px;
  color: #666;
}

.activity-header {
  margin: 30px 0;
}

.activity-title {
  font-size: 2em;
  color: #333;
  margin-bottom: 15px;
}

.activity-meta {
  display: flex;
  gap: 20px;
  color: #666;
  margin-bottom: 15px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

.activity-status {
  display: flex;
  gap: 15px;
  align-items: center;
}

.participants {
  color: #666;
}

.activity-content {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 30px;
}

.activity-image {
  width: 100%;
  height: 400px;
  overflow: hidden;
  border-radius: 8px;
  margin-bottom: 20px;
}

.activity-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.activity-description,
.activity-requirements {
  margin-bottom: 30px;
}

h2 {
  color: #333;
  margin-bottom: 15px;
  font-size: 1.5em;
}

.activity-footer {
  text-align: center;
  margin-top: 30px;
}
</style> 