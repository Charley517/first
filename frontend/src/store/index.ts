import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useGlobalStore = defineStore('global', () => {
  // 全局加载状态
  const loading = ref(false)
  const loadingText = ref('加载中...')

  // 设置加载状态
  const setLoading = (status: boolean, text?: string) => {
    loading.value = status
    if (text) {
      loadingText.value = text
    }
  }

  // 用户信息
  const userInfo = ref({
    id: '',
    username: '',
    role: '',
    token: ''
  })

  // 初始化时从localStorage加载用户信息
  const loadUserInfo = () => {
    const info = localStorage.getItem('user_info')
    if (info) {
      userInfo.value = JSON.parse(info)
    }
  }

  // 设置用户信息并持久化
  const setUserInfo = (info: any) => {
    userInfo.value = info
    localStorage.setItem('user_info', JSON.stringify(info))
  }

  // 清除用户信息
  const clearUserInfo = () => {
    userInfo.value = {
      id: '',
      username: '',
      role: '',
      token: ''
    }
    localStorage.removeItem('user_info')
    localStorage.removeItem('token')
  }

  loadUserInfo()

  // 主题设置
  const theme = ref('light')

  // 切换主题
  const toggleTheme = () => {
    theme.value = theme.value === 'light' ? 'dark' : 'light'
  }

  return {
    loading,
    loadingText,
    setLoading,
    userInfo,
    setUserInfo,
    clearUserInfo,
    theme,
    toggleTheme
  }
}) 