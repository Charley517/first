<template>
  <div class="map-container">
    <el-card class="map-card">
      <template #header>
        <div class="card-header">
          <h2>物流配送地图</h2>
        </div>
      </template>

      <div class="search-form">
        <el-form :model="form" label-width="100px">
          <el-form-item label="起点地址">
            <el-input 
              v-model="form.origin" 
              placeholder="请输入起点地址"
              @input="handleOriginInput"
            >
              <template #append>
                <el-button @click="getOriginLocation">
                  <el-icon><Location /></el-icon>
                </el-button>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item label="终点地址">
            <el-input 
              v-model="form.destination" 
              placeholder="请输入终点地址"
              @input="handleDestinationInput"
            >
              <template #append>
                <el-button @click="getDestinationLocation">
                  <el-icon><Location /></el-icon>
                </el-button>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="getRoute" :loading="loading">
              查询路线
            </el-button>
            <el-button @click="clearMap">清除路线</el-button>
          </el-form-item>
        </el-form>
      </div>

      <div class="map-info" v-if="routeInfo">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="总距离">
            {{ (routeInfo.distance / 1000).toFixed(2) }} 公里
          </el-descriptions-item>
          <el-descriptions-item label="预计时间">
            {{ Math.ceil(routeInfo.duration / 60) }} 分钟
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <div id="map" class="map-content"></div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Location } from '@element-plus/icons-vue'
import axios from 'axios'

const form = reactive({
  origin: '',
  destination: ''
})

const loading = ref(false)
const routeInfo = ref<any>(null)
let map: any = null
let polyline: any = null
let markers: any[] = []

// 初始化地图
onMounted(() => {
  map = new AMap.Map('map', {
    zoom: 11,
    center: [116.397428, 39.90923]
  })
})

// 清理地图
onUnmounted(() => {
  if (map) {
    map.destroy()
  }
})

// 获取起点位置
const getOriginLocation = async () => {
  if (!form.origin) {
    ElMessage.warning('请输入起点地址')
    return
  }
  try {
    const res = await axios.get('/api/map/geocode', {
      params: { address: form.origin }
    })
    if (res.data.status === '1' && res.data.geocodes.length > 0) {
      const location = res.data.geocodes[0].location
      addMarker(location, '起点')
      map.setCenter([location.lng, location.lat])
    } else {
      ElMessage.error('未找到起点位置')
    }
  } catch (error) {
    ElMessage.error('获取起点位置失败')
  }
}

// 获取终点位置
const getDestinationLocation = async () => {
  if (!form.destination) {
    ElMessage.warning('请输入终点地址')
    return
  }
  try {
    const res = await axios.get('/api/map/geocode', {
      params: { address: form.destination }
    })
    if (res.data.status === '1' && res.data.geocodes.length > 0) {
      const location = res.data.geocodes[0].location
      addMarker(location, '终点')
      map.setCenter([location.lng, location.lat])
    } else {
      ElMessage.error('未找到终点位置')
    }
  } catch (error) {
    ElMessage.error('获取终点位置失败')
  }
}

// 添加标记点
const addMarker = (location: any, title: string) => {
  const marker = new AMap.Marker({
    position: [location.lng, location.lat],
    title: title,
    map: map
  })
  markers.push(marker)
}

// 获取路线
const getRoute = async () => {
  if (!form.origin || !form.destination) {
    ElMessage.warning('请输入起点和终点地址')
    return
  }

  loading.value = true
  try {
    const res = await axios.get('/api/map/route', {
      params: {
        origin: form.origin,
        destination: form.destination
      }
    })

    if (res.data.status === '1') {
      const path = res.data.route.paths[0]
      routeInfo.value = {
        distance: path.distance,
        duration: path.duration
      }
      drawRoute(path.steps)
    } else {
      ElMessage.error('获取路线失败')
    }
  } catch (error) {
    ElMessage.error('获取路线失败')
  } finally {
    loading.value = false
  }
}

// 绘制路线
const drawRoute = (steps: any[]) => {
  clearMap()
  
  const path = steps.map(step => {
    return step.polyline.split(';').map(point => {
      const [lng, lat] = point.split(',').map(Number)
      return [lng, lat]
    })
  }).flat()

  polyline = new AMap.Polyline({
    path: path,
    strokeColor: '#3366FF',
    strokeWeight: 6,
    strokeOpacity: 0.8,
    showDir: true,
    map: map
  })

  // 调整地图视野以包含整个路线
  map.setFitView([polyline])
}

// 清除地图上的路线和标记
const clearMap = () => {
  if (polyline) {
    polyline.setMap(null)
    polyline = null
  }
  markers.forEach(marker => marker.setMap(null))
  markers = []
  routeInfo.value = null
}

// 输入防抖
let originTimer: any = null
const handleOriginInput = () => {
  clearTimeout(originTimer)
  originTimer = setTimeout(() => {
    getOriginLocation()
  }, 500)
}

let destinationTimer: any = null
const handleDestinationInput = () => {
  clearTimeout(destinationTimer)
  destinationTimer = setTimeout(() => {
    getDestinationLocation()
  }, 500)
}
</script>

<style scoped>
.map-container {
  padding: 20px;
}

.map-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.map-info {
  margin-bottom: 20px;
}

.map-content {
  width: 100%;
  height: 600px;
  border-radius: 4px;
  overflow: hidden;
}
</style> 