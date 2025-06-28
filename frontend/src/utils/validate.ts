// 手机号验证
export const validatePhone = (rule: any, value: string, callback: any) => {
  if (!value) {
    callback(new Error('请输入手机号'))
  } else if (!/^1[3-9]\d{9}$/.test(value)) {
    callback(new Error('请输入正确的手机号'))
  } else {
    callback()
  }
}

// 邮箱验证
export const validateEmail = (rule: any, value: string, callback: any) => {
  if (!value) {
    callback(new Error('请输入邮箱'))
  } else if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(value)) {
    callback(new Error('请输入正确的邮箱'))
  } else {
    callback()
  }
}

// 密码验证
export const validatePassword = (rule: any, value: string, callback: any) => {
  if (!value) {
    callback(new Error('请输入密码'))
  } else if (value.length < 6) {
    callback(new Error('密码长度不能小于6位'))
  } else {
    callback()
  }
}

// 必填验证
export const validateRequired = (message: string) => {
  return { required: true, message, trigger: 'blur' }
}

// 长度验证
export const validateLength = (min: number, max: number, message?: string) => {
  return {
    min,
    max,
    message: message || `长度在 ${min} 到 ${max} 个字符`,
    trigger: 'blur'
  }
}

// 数字范围验证
export const validateNumberRange = (min: number, max: number, message?: string) => {
  return {
    type: 'number',
    min,
    max,
    message: message || `数值范围在 ${min} 到 ${max} 之间`,
    trigger: 'blur'
  }
}

// 自定义正则验证
export const validatePattern = (pattern: RegExp, message: string) => {
  return {
    pattern,
    message,
    trigger: 'blur'
  }
} 