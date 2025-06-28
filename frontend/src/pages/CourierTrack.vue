<template>
  <div class="track-container">
    <el-card class="track-card">
      <template #header>
        <div class="card-header">
          <h2>配送员实时位置追踪</h2>
          <div class="header-actions">
            <el-button type="primary" @click="startTracking" :disabled="!courierId || isTracking">
              开始追踪
            </el-button>
            <el-button type="danger" @click="stopTracking" :disabled="!isTracking">
              停止追踪
            </el-button>
          </div>
        </div>
      </template>

      <div class="control-panel">
        <el-form :inline="true" :model="trackForm">
          <el-form-item label="配送员">
            <el-select 
              v-model="courierId" 
              placeholder="选择配送员"
              :disabled="isTracking"
            >
              <el-option
                v-for="courier in couriers"
                :key="courier.id"
                :label="courier.name"
                :value="courier.id"
              >
                <span>{{ courier.name }}</span>
                <span class="courier-status">
                  ({{ getStatusText(courier.status) }})
                </span>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="更新间隔">
            <el-select 
              v-model="trackForm.interval" 
              placeholder="选择更新间隔"
              :disabled="isTracking"
            >
              <el-option label="3秒" :value="3000" />
              <el-option label="5秒" :value="5000" />
              <el-option label="10秒" :value="10000" />
            </el-select>
          </el-form-item>
        </el-form>
      </div>

      <div class="map-container">
        <div id="map" class="map"></div>
        <div v-if="selectedCourier" class="courier-info">
          <el-descriptions :column="1" border>
            <el-descriptions-item label="配送员">
              {{ selectedCourier.name }}
            </el-descriptions-item>
            <el-descriptions-item label="电话">
              {{ selectedCourier.phone }}
            </el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="getStatusType(selectedCourier.status)">
                {{ getStatusText(selectedCourier.status) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="当前位置">
              {{ selectedCourier.currentLocation }}
            </el-descriptions-item>
            <el-descriptions-item label="最后更新">
              {{ formatDate(selectedCourier.lastUpdateTime) }}
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

// 数据定义
const courierId = ref(null)
const couriers = ref([])
const selectedCourier = ref(null)
const isTracking = ref(false)
let trackingTimer: number | null = null

const trackForm = reactive({
  interval: 5000
})

// 地图相关
let map: any = null
let marker: any = null
let infoWindow: any = null

// 加载配送员列表
const loadCouriers = async () => {
  try {
    const res = await axios.get('/api/couriers')
    couriers.value = res.data
  } catch (error) {
    ElMessage.error('加载配送员列表失败')
  }
}

// 加载配送员位置
const loadLocation = async () => {
  if (!courierId.value) return

  try {
    const res = await axios.get(`/api/couriers/${courierId.value}`)
    selectedCourier.value = res.data
    
    const locationStr = res.data.currentLocation
    if (!locationStr) {
      ElMessage.warning('未获取到配送员位置信息')
      return
    }

    const [lng, lat] = locationStr.split(',').map(Number)
    const position = new AMap.LngLat(lng, lat)

    // 更新地图中心点
    map.setCenter(position)

    // 更新或创建标记
    if (!marker) {
      marker = new AMap.Marker({
        position,
        icon: new AMap.Icon({
          size: new AMap.Size(25, 34),
          image: 'https://webapi.amap.com/theme/v1.3/markers/n/mark_b.png',
          imageSize: new AMap.Size(25, 34)
        })
      })
      map.add(marker)
    } else {
      marker.setPosition(position)
    }

    // 更新信息窗体
    if (!infoWindow) {
      infoWindow = new AMap.InfoWindow({
        isCustom: true,
        content: createInfoWindowContent(res.data),
        offset: new AMap.Pixel(0, -30)
      })
    } else {
      infoWindow.setContent(createInfoWindowContent(res.data))
    }
    infoWindow.open(map, position)

  } catch (error) {
    ElMessage.error('获取配送员位置失败')
    stopTracking()
  }
}

// 创建信息窗体内容
const createInfoWindowContent = (courier: any) => {
  return `
    <div class="info-window">
      <h4>${courier.name}</h4>
      <p>状态：${getStatusText(courier.status)}</p>
      <p>位置：${courier.currentLocation}</p>
      <p>更新时间：${formatDate(courier.lastUpdateTime)}</p>
    </div>
  `
}

// 开始追踪
const startTracking = () => {
  if (!courierId.value) {
    ElMessage.warning('请选择配送员')
    return
  }

  isTracking.value = true
  loadLocation()
  trackingTimer = window.setInterval(loadLocation, trackForm.interval)
}

// 停止追踪
const stopTracking = () => {
  if (trackingTimer) {
    clearInterval(trackingTimer)
    trackingTimer = null
  }
  isTracking.value = false
}

// 获取状态类型
const getStatusType = (status: string) => {
  const types: Record<string, string> = {
    'IDLE': 'success',
    'DELIVERING': 'primary',
    'REST': 'info'
  }
  return types[status] || 'info'
}

// 获取状态文本
const getStatusText = (status: string) => {
  const texts: Record<string, string> = {
    'IDLE': '空闲',
    'DELIVERING': '配送中',
    'REST': '休息'
  }
  return texts[status] || status
}

// 格式化日期
const formatDate = (date: string) => {
  return new Date(date).toLocaleString()
}

// 初始化地图
const initMap = () => {
  map = new AMap.Map('map', {
    zoom: 14,
    center: [116.397428, 39.90923]
  })
}

onMounted(() => {
  initMap()
  loadCouriers()
})

onUnmounted(() => {
  stopTracking()
})
</script>

<style scoped>
.track-container {
  padding: 20px;
}

.track-card {
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

.control-panel {
  margin-bottom: 20px;
}

.map-container {
  position: relative;
  height: 600px;
}

.map {
  width: 100%;
  height: 100%;
  border-radius: 4px;
}

.courier-info {
  position: absolute;
  top: 20px;
  right: 20px;
  width: 300px;
  background: white;
  padding: 15px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.courier-status {
  color: #909399;
  margin-left: 5px;
}

:deep(.info-window) {
  padding: 10px;
  
  h4 {
    margin: 0 0 10px 0;
    color: #303133;
  }
  
  p {
    margin: 5px 0;
    color: #606266;
  }
}
</style> 