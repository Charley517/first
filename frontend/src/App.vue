<template>
  <el-config-provider :locale="zhCn">
    <div class="app-container">
      <el-container>
        <el-header class="header">
          <div class="logo">
            <h1>物流追踪管理系统</h1>
          </div>
          <el-menu
            mode="horizontal"
            :router="true"
            :default-active="activeMenu"
            class="nav-menu"
          >
            <el-menu-item index="/">
              <el-icon><HomeFilled /></el-icon>
              首页
            </el-menu-item>
            <el-menu-item index="/create">
              <el-icon><Plus /></el-icon>
              创建订单
            </el-menu-item>
            <el-menu-item index="/orders">
              <el-icon><List /></el-icon>
              订单列表
            </el-menu-item>
            <el-menu-item index="/map">
              <el-icon><Location /></el-icon>
              地图追踪
            </el-menu-item>
            <el-menu-item index="/users">
              <el-icon><User /></el-icon>
              用户管理
            </el-menu-item>
            <el-menu-item index="/couriers">
              <el-icon><User /></el-icon>
              配送员管理
            </el-menu-item>
            <el-menu-item index="/notification">
              <el-icon><Bell /></el-icon>
              通知中心
            </el-menu-item>
          </el-menu>
        </el-header>

        <el-main>
          <router-view v-slot="{ Component }">
            <transition name="fade" mode="out-in">
              <component :is="Component" />
            </transition>
          </router-view>
        </el-main>

        <el-footer class="footer">
          <p>© 2024 物流追踪管理系统 - 版权所有</p>
        </el-footer>
      </el-container>
    </div>
  </el-config-provider>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { ElConfigProvider } from 'element-plus'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import {
  HomeFilled,
  Plus,
  List,
  Location,
  User,
  Bell
} from '@element-plus/icons-vue'

const route = useRoute()
const activeMenu = computed(() => route.path)
</script>

<style scoped>
.app-container {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.header {
  display: flex;
  align-items: center;
  padding: 0 20px;
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
}

.logo {
  display: flex;
  align-items: center;
  margin-right: 40px;
  
  h1 {
    font-size: 20px;
    color: #303133;
    margin: 0;
  }
}

.nav-menu {
  flex: 1;
  border-bottom: none;
}

.el-main {
  margin-top: 60px;
  padding: 20px;
  min-height: calc(100vh - 120px);
}

.footer {
  text-align: center;
  color: #909399;
  padding: 20px;
  background-color: #fff;
}

/* 页面切换动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 响应式布局 */
@media screen and (max-width: 768px) {
  .header {
    flex-direction: column;
    padding: 10px;
  }

  .logo {
    margin-right: 0;
    margin-bottom: 10px;
  }

  .nav-menu {
    width: 100%;
  }
}
</style> 