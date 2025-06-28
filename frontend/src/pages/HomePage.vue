<template>
  <div class="home-container">
    <el-row :gutter="20" class="mb-4">
      <el-col :span="24">
        <el-card class="welcome-card">
          <template #header>
            <div class="card-header">
              <h2>物流跟踪管理系统</h2>
            </div>
          </template>
          <div class="card-content">
            <p>欢迎使用物流跟踪管理系统，您可以在这里管理订单、查看物流状态、追踪配送进度。</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="feature-card" @click="$router.push('/create')">
          <el-icon class="feature-icon"><Plus /></el-icon>
          <h3>创建订单</h3>
          <p>快速创建新的物流订单</p>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="feature-card" @click="$router.push('/orders')">
          <el-icon class="feature-icon"><Search /></el-icon>
          <h3>订单查询</h3>
          <p>查询订单状态和物流信息</p>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="feature-card" @click="$router.push('/map')">
          <el-icon class="feature-icon"><Location /></el-icon>
          <h3>物流地图</h3>
          <p>实时查看配送路线和位置</p>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-4">
      <el-col :span="12">
        <el-card class="feature-card" @click="$router.push('/couriers')">
          <el-icon class="feature-icon"><User /></el-icon>
          <h3>配送员管理</h3>
          <p>管理配送员信息和状态</p>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="feature-card" @click="$router.push('/notifications')">
          <el-icon class="feature-icon"><Bell /></el-icon>
          <h3>通知中心</h3>
          <p>查看系统通知和提醒</p>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { Plus, Search, Location, User, Bell } from '@element-plus/icons-vue'
</script>

<style scoped>
.home-container {
  padding: 20px;
}

.mb-4 {
  margin-bottom: 20px;
}

.mt-4 {
  margin-top: 20px;
}

.welcome-card {
  text-align: center;
  background: linear-gradient(135deg, #409EFF 0%, #36D1DC 100%);
  color: white;
}

.card-header {
  display: flex;
  justify-content: center;
  align-items: center;
}

.card-content {
  font-size: 16px;
  line-height: 1.6;
}

.feature-card {
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  height: 200px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.feature-icon {
  font-size: 48px;
  color: #409EFF;
  margin-bottom: 16px;
}

h3 {
  margin: 10px 0;
  color: #303133;
}

p {
  color: #606266;
  margin: 0;
}
</style> 