import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import { useGlobalStore } from '@/store'

// 创建axios实例
const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
service.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    const globalStore = useGlobalStore()
    globalStore.setLoading(true)
    
    // 从localStorage获取token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers = {
        ...config.headers,
        Authorization: `Bearer ${token}`
      }
    }
    
    return config
  },
  (error) => {
    const globalStore = useGlobalStore()
    globalStore.setLoading(false)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const globalStore = useGlobalStore()
    globalStore.setLoading(false)
    
    const res = response.data

    // 如果后端返回的对象包含自定义code，则按照统一格式处理
    if (res && typeof res === 'object' && 'code' in res) {
      const { code, message, data } = res
      if (code === 200) {
        return data
      } else {
        ElMessage.error(message || '请求失败')
        return Promise.reject(new Error(message || '请求失败'))
      }
    }

    // 否则直接返回原始数据
    return res
  },
  (error) => {
    const globalStore = useGlobalStore()
    globalStore.setLoading(false)
    
    // 处理 HTTP 网络错误
    let message = ''
    // HTTP 状态码
    const status = error.response?.status
    switch (status) {
      case 401:
        message = '未授权，请重新登录'
        // 清除token并跳转到登录页
        localStorage.removeItem('token')
        window.location.href = '/login'
        break
      case 403:
        message = '拒绝访问'
        break
      case 404:
        message = '请求地址错误'
        break
      case 500:
        message = '服务器故障'
        break
      default:
        message = '网络连接故障'
    }
    
    ElMessage.error(message)
    return Promise.reject(error)
  }
)

// 封装GET请求
export const get = <T>(url: string, params?: any): Promise<T> => {
  return service.get(url, { params })
}

// 封装POST请求
export const post = <T>(url: string, data?: any): Promise<T> => {
  return service.post(url, data)
}

// 封装PUT请求
export const put = <T>(url: string, data?: any): Promise<T> => {
  return service.put(url, data)
}

// 封装DELETE请求
export const del = <T>(url: string): Promise<T> => {
  return service.delete(url)
}

export default service 