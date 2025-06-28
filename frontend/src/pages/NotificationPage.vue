<template>
  <div class="notification-container">
    <el-card class="notification-card">
      <template #header>
        <div class="card-header">
          <h2>通知中心</h2>
          <div class="header-actions">
            <el-button type="primary" @click="refreshNotifications">
              刷新
            </el-button>
            <el-button type="danger" @click="clearNotifications">
              清空
            </el-button>
          </div>
        </div>
      </template>

      <div class="filter-bar">
        <el-form :inline="true" :model="filterForm">
          <el-form-item label="通知类型">
            <el-select v-model="filterForm.type" placeholder="请选择类型">
              <el-option label="全部" value="" />
              <el-option label="短信" value="SMS" />
              <el-option label="邮件" value="EMAIL" />
              <el-option label="钉钉" value="DINGTALK" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="filterForm.status" placeholder="请选择状态">
              <el-option label="全部" value="" />
              <el-option label="成功" value="SUCCESS" />
              <el-option label="失败" value="FAILED" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleFilter">筛选</el-button>
            <el-button @click="resetFilter">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <div v-loading="loading">
        <el-timeline>
          <el-timeline-item
            v-for="msg in filteredMessages"
            :key="msg.timestamp"
            :type="getStatusType(msg.status)"
            :timestamp="formatDate(msg.timestamp)"
            :hollow="msg.status === 'FAILED'"
          >
            <div class="notification-content">
              <div class="notification-header">
                <el-tag :type="getTypeTag(msg.type)" size="small">
                  {{ getTypeText(msg.type) }}
                </el-tag>
                <el-tag 
                  :type="getStatusType(msg.status)" 
                  size="small"
                  class="status-tag"
                >
                  {{ getStatusText(msg.status) }}
                </el-tag>
              </div>
              
              <div class="notification-body">
                <p class="order-info">
                  订单编号：{{ msg.orderId }}
                </p>
                <p class="target-info">
                  通知对象：{{ msg.target }}
                </p>
                <p class="content-info">
                  通知内容：{{ msg.content }}
                </p>
                <p v-if="msg.error" class="error-info">
                  错误信息：{{ msg.error }}
                </p>
              </div>
            </div>
          </el-timeline-item>
        </el-timeline>

        <div v-if="!filteredMessages.length" class="no-data">
          <el-empty description="暂无通知记录" />
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

// 数据定义
const loading = ref(false)
const messages = ref([])

// 筛选表单
const filterForm = reactive({
  type: '',
  status: ''
})

// 筛选后的消息
const filteredMessages = computed(() => {
  return messages.value.filter(msg => {
    const typeMatch = !filterForm.type || msg.type === filterForm.type
    const statusMatch = !filterForm.status || msg.status === filterForm.status
    return typeMatch && statusMatch
  })
})

// 加载通知记录
const loadNotifications = async () => {
  loading.value = true
  try {
    const res = await axios.get('/api/notifications')
    messages.value = res.data
  } catch (error) {
    ElMessage.error('加载通知记录失败')
  } finally {
    loading.value = false
  }
}

// 刷新通知
const refreshNotifications = () => {
  loadNotifications()
}

// 清空通知
const clearNotifications = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要清空所有通知记录吗？',
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await axios.delete('/api/notifications')
    ElMessage.success('清空成功')
    loadNotifications()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('清空失败')
    }
  }
}

// 筛选
const handleFilter = () => {
  // 已使用计算属性实现筛选
}

// 重置筛选
const resetFilter = () => {
  filterForm.type = ''
  filterForm.status = ''
}

// 格式化日期
const formatDate = (date: string) => {
  return new Date(date).toLocaleString()
}

// 获取通知类型标签样式
const getTypeTag = (type: string) => {
  const types: Record<string, string> = {
    'SMS': 'success',
    'EMAIL': 'primary',
    'DINGTALK': 'warning'
  }
  return types[type] || 'info'
}

// 获取通知类型文本
const getTypeText = (type: string) => {
  const texts: Record<string, string> = {
    'SMS': '短信',
    'EMAIL': '邮件',
    'DINGTALK': '钉钉'
  }
  return texts[type] || type
}

// 获取状态类型
const getStatusType = (status: string) => {
  const types: Record<string, string> = {
    'SUCCESS': 'success',
    'FAILED': 'danger'
  }
  return types[status] || 'info'
}

// 获取状态文本
const getStatusText = (status: string) => {
  const texts: Record<string, string> = {
    'SUCCESS': '成功',
    'FAILED': '失败'
  }
  return texts[status] || status
}

onMounted(() => {
  loadNotifications()
})
</script>

<style scoped>
.notification-container {
  padding: 20px;
}

.notification-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.filter-bar {
  margin-bottom: 20px;
}

.notification-content {
  padding: 10px;
  background-color: #f8f9fa;
  border-radius: 4px;
}

.notification-header {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
}

.notification-body {
  p {
    margin: 5px 0;
    color: #666;
  }

  .order-info {
    font-weight: bold;
  }

  .error-info {
    color: #f56c6c;
  }
}

.no-data {
  padding: 40px 0;
}

.status-tag {
  margin-left: auto;
}
</style> 