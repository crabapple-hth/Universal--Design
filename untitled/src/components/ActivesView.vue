<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { apiActivityList } from '@/net/api/activity'
import { ElMessage } from 'element-plus'
import {Clock, Location} from "@element-plus/icons-vue";
import axios from "axios";

const router = useRouter()
const activities = ref([])
const loading = ref(true)

// 获取活动列表
const fetchActivities = async () => {
  try {
    apiActivityList(data => {
      if (data.code === 200) {
        activities.value = data.data
      } else {
        ElMessage.error('获取活动列表失败')
      }
    })
  } catch (error) {
    console.error('获取活动列表失败:', error)
    ElMessage.error('获取活动列表失败')
  } finally {
    loading.value = false
  }
}

// 跳转到活动详情
const goToDetail = (activityId) => {
  router.push(`/actives/details?id=${activityId}`)
}

onMounted(() => {
  fetchActivities()
})
</script>

<template>
  <div class="activities-container">
    <router-view v-if="$route.path.includes('details')" />
    <template v-else>
      <h1 class="page-title">校园活动</h1>
      
      <div v-if="loading" class="loading">
        <el-skeleton :rows="3" animated />
      </div>
      
      <div v-else-if="activities.length === 0" class="empty">
        <el-empty description="暂无活动" />
      </div>
      
      <div v-else class="activities-grid">
        <div v-for="activity in activities" 
             :key="activity.id" 
             class="activity-card"
             @click="goToDetail(activity.id)">
          <div class="activity-image">
            <img :src="`${axios.defaults.baseURL}/images/${activity.coverImage || 'default-activity.jpg'}`" :alt="activity.title">
          </div>
          <div class="activity-info">
            <h3 class="activity-title">{{ activity.title }}</h3>
            <p class="activity-time">
              <el-icon><Clock /></el-icon>
              {{ new Date(activity.startTime).toLocaleString() }}
            </p>
            <p class="activity-location">
              <el-icon><Location /></el-icon>
              {{ activity.location }}
            </p>
            <p class="activity-description">{{ activity.description }}</p>
            <div class="activity-status">
              <el-tag :type="activity.status === 1 ? 'success' : activity.status === 2 ? 'warning' : 'info'">
                {{ activity.status === 1 ? '未开始' : activity.status === 2 ? '进行中' : '已结束' }}
              </el-tag>
              <span class="participants">
                {{ activity.currentParticipants }}/{{ activity.maxParticipants }}人
              </span>
            </div>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<style scoped>
.activities-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.page-title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.loading {
  padding: 20px;
}

.empty {
  padding: 40px;
  text-align: center;
}

.activities-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.activity-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.activity-card:hover {
  transform: translateY(-5px);
}

.activity-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.activity-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.activity-info {
  padding: 15px;
}

.activity-title {
  margin: 0 0 10px 0;
  font-size: 1.2em;
  color: #333;
}

.activity-time,
.activity-location {
  margin: 5px 0;
  color: #666;
  font-size: 0.9em;
  display: flex;
  align-items: center;
  gap: 5px;
}

.activity-description {
  margin: 10px 0;
  color: #666;
  font-size: 0.9em;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.activity-status {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.participants {
  color: #666;
  font-size: 0.9em;
}
</style>