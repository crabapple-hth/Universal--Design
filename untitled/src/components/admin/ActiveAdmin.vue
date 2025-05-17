<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Edit, Delete, View } from '@element-plus/icons-vue'
import { apiActivityList, apiActivityDetail, apiActivityCreate, apiActivityUpdate, apiActivityDelete } from '@/net/api/activity'
import axios from "axios";
import {getAccessToken} from "@/net/index.js";

// 数据
const loading = ref(false)
const activities = ref([])
const searchQuery = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)

// 表单数据
const form = ref({
  id: '',
  title: '',
  description: '',
  coverImage: '',
  startTime: '',
  endTime: '',
  location: '',
  organizer: '',
  requirements: '',
  maxParticipants: 0,
  status: '0',
  schedules: []
})

// 日程表单数据
const scheduleForm = ref({
  timeSlot: '',
  content: ''
})

// 日程对话框显示状态
const scheduleDialogVisible = ref(false)
const scheduleDialogTitle = ref('')

// 表单验证规则
const rules = {
  title: [{ required: true, message: '请输入活动标题', trigger: 'blur' }],
  description: [{ required: true, message: '请输入活动描述', trigger: 'blur' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  location: [{ required: true, message: '请输入活动地点', trigger: 'blur' }],
  organizer: [{ required: true, message: '请输入组织者信息', trigger: 'blur' }],
  maxParticipants: [{ required: true, message: '请输入最大参与人数', trigger: 'blur' }],
  status: [{ required: true, message: '请选择活动状态', trigger: 'change' }]
}

// 日程表单验证规则
const scheduleRules = {
  timeSlot: [{ required: true, message: '请输入时间段', trigger: 'blur' }],
  content: [{ required: true, message: '请输入日程内容', trigger: 'blur' }]
}

// 计算属性：过滤后的活动列表
const filteredActivities = computed(() => {
  return activities.value.filter(activity => 
    activity.title.toLowerCase().includes(searchQuery.value.toLowerCase())
  )
})

// 获取活动列表
const fetchActivities = async () => {
  loading.value = true
  try {
    apiActivityList(data => {
      if (data.code === 200) {
        activities.value = data.data
        total.value = data.data.length
      } else {
        ElMessage.error(data.message || '获取活动列表失败')
      }
    })
  } catch (error) {
    ElMessage.error('获取活动列表失败')
  } finally {
    loading.value = false
  }
}

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchActivities()
}

// 处理分页大小变化
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchActivities()
}

// 处理页码变化
const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchActivities()
}

// 处理添加活动
const handleAdd = () => {
  dialogTitle.value = '添加活动'
  form.value = {
    id: '',
    title: '',
    description: '',
    coverImage: '',
    startTime: '',
    endTime: '',
    location: '',
    organizer: '',
    requirements: '',
    maxParticipants: 0,
    status: '0',
    schedules: []
  }
  dialogVisible.value = true
}

// 处理编辑活动
const handleEdit = (row) => {
  dialogTitle.value = '编辑活动'
  form.value = { ...row }
  dialogVisible.value = true
}

// 处理删除活动
const handleDelete = (row) => {
  ElMessageBox.confirm(
    '确定要删除该活动吗？',
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    apiActivityDelete(row.id, data => {
      if (data.code === 200) {
        ElMessage.success('删除成功')
        fetchActivities()
      } else {
        ElMessage.error(data.message || '删除失败')
      }
    })
  })
}

// 处理对话框关闭
const handleDialogClose = () => {
  formRef.value?.resetFields()
}

// 处理表单提交
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 处理日程数据
        const activityData = {
          ...form.value,
          schedules: form.value.schedules.map(schedule => ({
            timeSlot: schedule.timeSlot,
            content: schedule.content
          }))
        }

        if (form.value.id) {
          apiActivityUpdate(form.value.id, activityData, data => {
              ElMessage.success('更新成功')
              dialogVisible.value = false
              fetchActivities()
          })
        } else {
          apiActivityCreate(activityData, data => {
              ElMessage.success('添加成功')
              dialogVisible.value = false
              fetchActivities()
          })
        }
      } catch (error) {
        ElMessage.error(form.value.id ? '更新失败' : '添加失败')
      }
    }
  })
}

// 获取状态类型
const getStatusType = (status) => {
  const types = {
    '0': 'info',
    '1': 'success',
    '2': 'warning'
  }
  return types[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const texts = {
    '0': '未开始',
    '1': '进行中',
    '2': '已结束'
  }
  return texts[status] || '未知'
}

// 处理添加日程
const handleAddSchedule = () => {
  scheduleDialogTitle.value = '添加日程'
  scheduleForm.value = {
    timeSlot: '',
    content: ''
  }
  scheduleDialogVisible.value = true
}

// 处理编辑日程
const handleEditSchedule = (schedule) => {
  scheduleDialogTitle.value = '编辑日程'
  scheduleForm.value = { ...schedule }
  scheduleDialogVisible.value = true
}

// 处理删除日程
const handleDeleteSchedule = (index) => {
  form.value.schedules.splice(index, 1)
}

// 处理日程提交
const handleScheduleSubmit = () => {
  if (scheduleForm.value.id) {
    // 编辑现有日程
    const index = form.value.schedules.findIndex(s => s.id === scheduleForm.value.id)
    if (index !== -1) {
      form.value.schedules[index] = { ...scheduleForm.value }
    }
  } else {
    // 添加新日程
    form.value.schedules.push({
      id: Date.now(), // 临时ID
      timeSlot: scheduleForm.value.timeSlot,
      content: scheduleForm.value.content
    })
  }
  scheduleDialogVisible.value = false
}

// 处理日程对话框关闭
const handleScheduleDialogClose = () => {
  scheduleForm.value = {
    timeSlot: '',
    content: ''
  }
}

function beforeAvatarUpload(rawFile) {
  if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
    ElMessage.error('图片只能是 JPG/PNG 格式的')
    return false
  } else if(rawFile.size / 1024 > 100) {
    ElMessage.error('图片大小不能大于 100KB')
    return false
  }
  return true
}

function uploadSuccess(response){
  form.value.coverImage=response.data
}

// 生命周期钩子
onMounted(() => {
  fetchActivities()
})
</script>

<template>
  <div class="active-admin">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>活动管理</span>
          <el-button type="primary" @click="handleAdd">添加活动</el-button>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-input
          v-model="searchQuery"
          placeholder="搜索活动名称"
          class="search-input"
          clearable
          @clear="handleSearch"
          @input="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </div>

      <!-- 活动列表 -->
      <el-table :data="filteredActivities" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="封面" width="100">
          <template #default="scope">
            <el-image
              :src="`${axios.defaults.baseURL}/images/${scope.row.coverImage || 'default-activity.jpg'}`"
              :preview-src-list="[scope.row.coverImage]"
              fit="cover"
              style="width: 50px; height: 50px"
            />
          </template>
        </el-table-column>
        <el-table-column prop="title" label="活动标题" />
        <el-table-column prop="organizer" label="组织者" width="120" />
        <el-table-column prop="startTime" label="开始时间" width="180">
          <template #default="scope">
            {{ new Date(scope.row.startTime).toLocaleString() }}
          </template>
        </el-table-column>
        <el-table-column prop="endTime" label="结束时间" width="180">
          <template #default="scope">
            {{ new Date(scope.row.endTime).toLocaleString() }}
          </template>
        </el-table-column>
        <el-table-column prop="location" label="地点" width="150" />
        <el-table-column label="参与人数" width="120">
          <template #default="scope">
            {{ scope.row.currentParticipants }}/{{ scope.row.maxParticipants }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 添加/编辑活动对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="60%"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="活动标题" prop="title">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="活动封面" prop="coverImage">
          <el-upload
            class="avatar-uploader"
            :action="axios.defaults.baseURL + '/api/image/active'"
            :headers="getAccessToken()"
            :show-file-list="false"
            :on-success="uploadSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="form.coverImage" :src="axios.defaults.baseURL+'/images/'+form.coverImage" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="活动描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="4"
          />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
            v-model="form.startTime"
            type="datetime"
            placeholder="选择开始时间"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
            v-model="form.endTime"
            type="datetime"
            placeholder="选择结束时间"
          />
        </el-form-item>
        <el-form-item label="活动地点" prop="location">
          <el-input v-model="form.location" />
        </el-form-item>
        <el-form-item label="组织者" prop="organizer">
          <el-input v-model="form.organizer" />
        </el-form-item>
        <el-form-item label="活动要求" prop="requirements">
          <el-input
            v-model="form.requirements"
            type="textarea"
            :rows="3"
          />
        </el-form-item>
        <el-form-item label="最大人数" prop="maxParticipants">
          <el-input-number v-model="form.maxParticipants" :min="1" />
        </el-form-item>
        <el-form-item label="活动状态" prop="status">
          <el-select v-model="form.status" placeholder="选择活动状态">
            <el-option label="未开始" value="0" />
            <el-option label="进行中" value="1" />
            <el-option label="已结束" value="2" />
          </el-select>
        </el-form-item>
        
        <!-- 日程管理 -->
        <el-form-item label="活动日程">
          <div class="schedule-list">
            <div v-for="(schedule, index) in form.schedules" :key="schedule.id" class="schedule-item">
              <div class="schedule-info">
                <span class="time">{{ schedule.timeSlot }}</span>
                <span class="content">{{ schedule.content }}</span>
              </div>
              <div class="schedule-actions">
                <el-button size="small" @click="handleEditSchedule(schedule)">编辑</el-button>
                <el-button size="small" type="danger" @click="handleDeleteSchedule(index)">删除</el-button>
              </div>
            </div>
            <el-button type="primary" @click="handleAddSchedule">添加日程</el-button>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 日程管理对话框 -->
    <el-dialog
      :title="scheduleDialogTitle"
      v-model="scheduleDialogVisible"
      width="40%"
      @close="handleScheduleDialogClose"
    >
      <el-form
        :model="scheduleForm"
        :rules="scheduleRules"
        label-width="80px"
      >
        <el-form-item label="时间段" prop="timeSlot">
          <el-input v-model="scheduleForm.timeSlot" placeholder="例如：9:00-10:00" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
            v-model="scheduleForm.content"
            type="textarea"
            :rows="3"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="scheduleDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleScheduleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.active-admin {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-bar {
  margin-bottom: 20px;
}

.search-input {
  width: 300px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 178px;
  height: 178px;
  transition: border-color 0.3s;
}

.avatar-uploader:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
  object-fit: cover;
}

.schedule-list {
  width: 100%;
}

.schedule-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #eee;
  margin-bottom: 10px;
}

.schedule-info {
  flex: 1;
}

.schedule-info .time {
  font-weight: bold;
  margin-right: 20px;
}

.schedule-actions {
  display: flex;
  gap: 10px;
}
</style>