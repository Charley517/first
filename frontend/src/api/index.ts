import { get, post, put, del } from '@/utils/request'
import type { 
  UserInfo, 
  Order, 
  Courier, 
  Notification,
  PaginationParams,
  PaginationResponse,
  ResponseData,
  Location,
  Route,
  FileInfo,
  Settings
} from '@/types'

// 用户相关接口
export const userApi = {
  // 登录
  login: (data: { username: string; password: string }) => {
    return post<ResponseData<{ token: string; userInfo: UserInfo }>>('/api/users/login', data)
  },
  // 注册
  register: (data: { username: string; password: string; email: string }) => {
    return post<ResponseData<UserInfo>>('/api/users/register', data)
  },
  // 获取用户信息
  getUserInfo: () => {
    return get<ResponseData<UserInfo>>('/api/users/info')
  },
  // 更新用户信息
  updateUserInfo: (data: Partial<UserInfo>) => {
    return put<ResponseData<UserInfo>>('/api/users/info', data)
  },
  // 修改密码
  changePassword: (data: { oldPassword: string; newPassword: string }) => {
    return post<ResponseData<void>>('/api/users/password', data)
  },
  // 获取用户列表
  getUserList: (params?: any) => {
    return get<ResponseData<UserInfo[]>>('/api/users', params)
  },
  // 创建用户
  createUser: (data: Partial<UserInfo>) => {
    return post<ResponseData<UserInfo>>('/api/users', data)
  }
}

// 订单相关接口
export const orderApi = {
  // 创建订单
  createOrder: (data: Partial<Order>) => {
    return post<ResponseData<Order>>('/api/orders', data)
  },
  // 获取订单列表
  getOrderList: (params: PaginationParams & Partial<Order>) => {
    return get<ResponseData<PaginationResponse<Order>>>('/api/orders', params)
  },
  // 获取订单详情
  getOrderDetail: (id: string) => {
    return get<ResponseData<Order>>(`/api/orders/${id}`)
  },
  // 更新订单状态
  updateOrderStatus: (id: string, status: string) => {
    return put<ResponseData<Order>>(`/api/orders/${id}/status`, { status })
  },
  // 取消订单
  cancelOrder: (id: string) => {
    return post<ResponseData<void>>(`/api/orders/${id}/cancel`)
  }
}

// 快递员相关接口
export const courierApi = {
  // 获取快递员列表
  getCourierList: (params?: any) => {
    return get<ResponseData<Courier[]>>('/api/couriers', params)
  },
  // 获取快递员详情
  getCourierDetail: (id: string) => {
    return get<ResponseData<Courier>>(`/api/couriers/${id}`)
  },
  // 更新快递员位置
  updateLocation: (id: string, location: Location) => {
    return put<ResponseData<void>>(`/api/couriers/${id}/location`, location)
  },
  // 分配订单
  assignOrder: (courierId: string, orderId: string) => {
    return post<ResponseData<void>>(`/api/couriers/${courierId}/orders/${orderId}`)
  },
  // 创建快递员
  createCourier: (data: Partial<Courier>) => {
    return post<ResponseData<Courier>>('/api/couriers', data)
  }
}

// 通知相关接口
export const notificationApi = {
  // 获取通知列表
  getNotificationList: (params: PaginationParams) => {
    return get<ResponseData<PaginationResponse<Notification>>>('/notification/list', params)
  },
  // 标记通知为已读
  markAsRead: (id: string) => {
    return put<ResponseData<void>>(`/notification/${id}/read`)
  },
  // 删除通知
  deleteNotification: (id: string) => {
    return del<ResponseData<void>>(`/notification/${id}`)
  }
}

// 地图相关接口
export const mapApi = {
  // 获取路线规划
  getRoute: (origin: Location, destination: Location) => {
    return get<ResponseData<Route>>('/api/map/route', { origin, destination })
  },
  // 地理编码
  geocode: (address: string) => {
    return get<ResponseData<Location>>('/api/map/geocode', { address })
  },
  // 逆地理编码
  reverseGeocode: (location: Location) => {
    return get<ResponseData<{ address: string }>>('/api/map/regeocode', location)
  }
}

// 文件相关接口
export const fileApi = {
  // 上传文件
  uploadFile: (file: File) => {
    const formData = new FormData()
    formData.append('file', file)
    return post<ResponseData<FileInfo>>('/api/files/upload', formData)
  },
  // 获取文件信息
  getFileInfo: (id: string) => {
    return get<ResponseData<FileInfo>>(`/api/files/${id}`)
  },
  // 删除文件
  deleteFile: (id: string) => {
    return del<ResponseData<void>>(`/api/files/${id}`)
  }
}

// 系统设置相关接口
export const settingsApi = {
  // 获取系统设置
  getSettings: () => {
    return get<ResponseData<Settings>>('/api/settings')
  },
  // 更新系统设置
  updateSettings: (data: Partial<Settings>) => {
    return put<ResponseData<Settings>>('/api/settings', data)
  }
} 