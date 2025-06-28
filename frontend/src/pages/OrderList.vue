<template>
  <div class="order-list-container">
    <el-card class="order-card">
      <template #header>
        <div class="card-header">
          <h2>订单列表</h2>
          <el-button type="primary" @click="$router.push('/create')">
            创建订单
          </el-button>
        </div>
      </template>

      <div class="search-bar">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="订单编号">
            <el-input v-model="searchForm.orderNumber" placeholder="请输入订单编号" />
          </el-form-item>
          <el-form-item label="订单状态">
            <el-select v-model="searchForm.status" placeholder="请选择状态">
              <el-option label="全部" value="" />
              <el-option label="待接单" value="PENDING" />
              <el-option label="配送中" value="DELIVERING" />
              <el-option label="已完成" value="COMPLETED" />
              <el-option label="已取消" value="CANCELLED" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table 
        :data="orders" 
        style="width: 100%" 
        v-loading="loading"
        border
      >
        <el-table-column prop="orderNumber" label="订单编号" width="180" />
        <el-table-column prop="senderName" label="寄件人" width="120" />
        <el-table-column prop="senderPhone" label="寄件人电话" width="150" />
        <el-table-column prop="receiverName" label="收件人" width="120" />
        <el-table-column prop="receiverPhone" label="收件人电话" width="150" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="200">
          <template #default="scope">
            <el-button 
              size="small" 
              type="primary" 
              @click="viewTrace(scope.row.id)"
            >
              查看轨迹
            </el-button>
            <el-button 
              size="small" 
              type="success" 
              @click="viewMap(scope.row.id)"
              v-if="scope.row.status === 'DELIVERING'"
            >
              实时位置
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const router = useRouter()
const loading = ref(false)
const orders = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

const searchForm = reactive({
  orderNumber: '',
  status: ''
})

// 加载订单列表
const loadOrders = async () => {
  loading.value = true
  try {
    const res = await axios.get('/api/order/list', {
      params: {
        page: currentPage.value - 1,
        size: pageSize.value,
        orderNumber: searchForm.orderNumber,
        status: searchForm.status
      }
    })
    orders.value = res.data.content
    total.value = res.data.totalElements
  } catch (error) {
    ElMessage.error('加载订单列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  loadOrders()
}

// 重置搜索
const resetSearch = () => {
  searchForm.orderNumber = ''
  searchForm.status = ''
  handleSearch()
}

// 分页处理
const handleSizeChange = (val: number) => {
  pageSize.value = val
  loadOrders()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  loadOrders()
}

// 查看轨迹
const viewTrace = (orderId: string) => {
  router.push(`/trace/${orderId}`)
}

// 查看地图
const viewMap = (orderId: string) => {
  router.push(`/map?orderId=${orderId}`)
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

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.order-list-container {
  padding: 20px;
}

.order-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-bar {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 