import { Router } from 'vue-router'
import { useGlobalStore } from '@/store'
import { ROUTE_NAMES, ROUTE_PATHS } from '@/constants'
import { ElMessage } from 'element-plus'

// 白名单路由
const whiteList = [
  ROUTE_NAMES.LOGIN,
  ROUTE_NAMES.REGISTER
]

export function setupRouterGuard(router: Router) {
  router.beforeEach(async (to, from, next) => {
    // 设置页面标题
    document.title = `${to.meta.title || '物流跟踪管理系统'}`
    next()
  })

  router.afterEach(() => {
    // 路由切换后的操作
    // 例如：关闭loading、滚动到顶部等
    window.scrollTo(0, 0)
  })

  router.onError((error) => {
    console.error('路由错误:', error)
    ElMessage.error('页面加载失败，请刷新重试')
  })
} 