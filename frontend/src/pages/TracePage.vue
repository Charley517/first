<template>
  <div class="trace-container">
    <el-card class="trace-card">
      <template #header>
        <div class="card-header">
          <h2>物流轨迹</h2>
          <el-button @click="$router.back()">返回</el-button>
        </div>
      </template>

      <div v-loading="loading">
        <div v-if="orderInfo" class="order-info">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="订单编号">
              {{ orderInfo.orderNumber }}
            </el-descriptions-item>
            <el-descriptions-item label="订单状态">
              <el-tag :type="getStatusType(orderInfo.status)">
                {{ getStatusText(orderInfo.status) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="寄件人">
              {{ orderInfo.senderName }} ({{ orderInfo.senderPhone }})
            </el-descriptions-item>
            <el-descriptions-item label="收件人">
              {{ orderInfo.receiverName }} ({{ orderInfo.receiverPhone }})
            </el-descriptions-item>
          </el-descriptions>
        </div>

        <div class="trace-timeline">
          <el-timeline>
            <el-timeline-item
              v-for="(trace, index) in traces"
              :key="index"
              :type="getTraceType(trace.status)"
              :timestamp="formatDate(trace.timestamp)"
              :hollow="index !== 0"
            >
              <div class="trace-content">
                <h4>{{ trace.location }}</h4>
                <p>{{ trace.description }}</p>
                <p v-if="trace.operator">操作员：{{ trace.operator }}</p>
              </div>
            </el-timeline-item>
          </el-timeline>
        </div>

        <div v-if="!traces.length" class="no-data">
          <el-empty description="暂无物流轨迹信息" />
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const orderInfo = ref(null)
const traces = ref([])

// 加载订单信息和轨迹
const loadTrace = async () => {
  loading.value = true
  try {
    const orderId = route.params.orderId
    const [orderRes, traceRes] = await Promise.all([
      axios.get(`/api/order/${orderId}`),
      axios.get(`/api/trace/${orderId}`)
    ])
    orderInfo.value = orderRes.data
    traces.value = traceRes.data
  } catch (error) {
    ElMessage.error('加载物流轨迹失败')
  } finally {
    loading.value = false
  }
}

// 格式化日期
const formatDate = (date: string) => {
  return new Date(date).toLocaleString()
}

// 获取状态类型
const getStatusType = (status: string) => {
  const types: Record<string, string> = {
    'PENDING': 'warning',
    'DELIVERING': 'primary',
    'COMPLETED': 'success',
    'CANCELLED': 'info'
  }
  return types[status] || 'info'
}

// 获取状态文本
const getStatusText = (status: string) => {
  const texts: Record<string, string> = {
    'PENDING': '待接单',
    'DELIVERING': '配送中',
    'COMPLETED': '已完成',
    'CANCELLED': '已取消'
  }
  return texts[status] || status
}

// 获取轨迹类型
const getTraceType = (status: string) => {
  const types: Record<string, string> = {
    'PENDING': 'warning',
    'DELIVERING': 'primary',
    'COMPLETED': 'success',
    'CANCELLED': 'info'
  }
  return types[status] || 'info'
}

onMounted(() => {
  loadTrace()
})
</script>

<style scoped>
.trace-container {
  padding: 20px;
}

.trace-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-info {
  margin-bottom: 30px;
}

.trace-timeline {
  padding: 20px;
}

.trace-content {
  h4 {
    margin: 0 0 8px 0;
    font-size: 16px;
  }

  p {
    margin: 4px 0;
    color: #666;
  }
}

.no-data {
  padding: 40px 0;
}
</style> 