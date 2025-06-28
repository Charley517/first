import { ElMessage, ElMessageBox } from 'element-plus'
import type { MessageParams } from 'element-plus'

// 成功提示
export const showSuccess = (message: string, duration = 3000) => {
  ElMessage({
    type: 'success',
    message,
    duration
  })
}

// 错误提示
export const showError = (message: string, duration = 3000) => {
  ElMessage({
    type: 'error',
    message,
    duration
  })
}

// 警告提示
export const showWarning = (message: string, duration = 3000) => {
  ElMessage({
    type: 'warning',
    message,
    duration
  })
}

// 信息提示
export const showInfo = (message: string, duration = 3000) => {
  ElMessage({
    type: 'info',
    message,
    duration
  })
}

// 确认对话框
export const showConfirm = (
  message: string,
  title = '提示',
  type: 'warning' | 'info' | 'success' | 'error' = 'warning'
) => {
  return ElMessageBox.confirm(message, title, {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type
  })
}

// 自定义消息
export const showMessage = (options: MessageParams) => {
  ElMessage(options)
} 