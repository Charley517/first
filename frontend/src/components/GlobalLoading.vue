<template>
  <div v-if="loading" class="global-loading">
    <el-icon class="loading-icon" :size="40"><Loading /></el-icon>
    <p class="loading-text">{{ text }}</p>
  </div>
</template>

<script setup lang="ts">
import { Loading } from '@element-plus/icons-vue'

defineProps<{
  loading: boolean
  text?: string
}>()
</script>

<style scoped>
.global-loading {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.9);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.loading-icon {
  animation: rotate 1s linear infinite;
  color: #409EFF;
}

.loading-text {
  margin-top: 10px;
  color: #606266;
  font-size: 14px;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style> 