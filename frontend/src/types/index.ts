// 用户信息
export interface UserInfo {
  id: string
  username: string
  role: string
  avatar?: string
  email?: string
  phone?: string
  createTime?: string
  updateTime?: string
}

// 订单信息
export interface Order {
  id: string
  orderNo: string
  status: string
  sender: {
    name: string
    phone: string
    address: string
  }
  receiver: {
    name: string
    phone: string
    address: string
  }
  package: {
    type: string
    weight: number
    size: string
    description?: string
  }
  payment: {
    method: string
    amount: number
    status: string
  }
  courier?: {
    id: string
    name: string
    phone: string
  }
  createTime: string
  updateTime: string
}

// 快递员信息
export interface Courier {
  id: string
  name: string
  phone: string
  status: string
  currentLocation?: {
    latitude: number
    longitude: number
  }
  orders?: Order[]
  createTime: string
  updateTime: string
}

// 通知信息
export interface Notification {
  id: string
  type: string
  title: string
  content: string
  isRead: boolean
  createTime: string
}

// 分页参数
export interface PaginationParams {
  page: number
  pageSize: number
  total?: number
}

// 分页响应
export interface PaginationResponse<T> {
  list: T[]
  total: number
  page: number
  pageSize: number
}

// 响应数据
export interface ResponseData<T> {
  code: number
  message: string
  data: T
}

// 路由元信息
export interface RouteMeta {
  title: string
  icon?: string
  roles?: string[]
  requiresAuth?: boolean
}

// 地图坐标
export interface Location {
  latitude: number
  longitude: number
  address?: string
}

// 路线信息
export interface Route {
  distance: number
  duration: number
  steps: {
    distance: number
    duration: number
    instruction: string
    path: Location[]
  }[]
}

// 文件信息
export interface FileInfo {
  id: string
  name: string
  size: number
  type: string
  url: string
  createTime: string
}

// 系统设置
export interface Settings {
  theme: string
  language: string
  notification: {
    email: boolean
    sms: boolean
    push: boolean
  }
  privacy: {
    location: boolean
    profile: boolean
  }
} 