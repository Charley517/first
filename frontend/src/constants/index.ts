// 订单状态
export const ORDER_STATUS = {
  PENDING: 'pending',
  PROCESSING: 'processing',
  SHIPPING: 'shipping',
  DELIVERED: 'delivered',
  CANCELLED: 'cancelled'
} as const

// 订单状态映射
export const ORDER_STATUS_MAP = {
  [ORDER_STATUS.PENDING]: '待处理',
  [ORDER_STATUS.PROCESSING]: '处理中',
  [ORDER_STATUS.SHIPPING]: '运输中',
  [ORDER_STATUS.DELIVERED]: '已送达',
  [ORDER_STATUS.CANCELLED]: '已取消'
} as const

// 包裹类型
export const PACKAGE_TYPES = {
  DOCUMENT: 'document',
  SMALL: 'small',
  MEDIUM: 'medium',
  LARGE: 'large',
  SPECIAL: 'special'
} as const

// 包裹类型映射
export const PACKAGE_TYPE_MAP = {
  [PACKAGE_TYPES.DOCUMENT]: '文件',
  [PACKAGE_TYPES.SMALL]: '小件',
  [PACKAGE_TYPES.MEDIUM]: '中件',
  [PACKAGE_TYPES.LARGE]: '大件',
  [PACKAGE_TYPES.SPECIAL]: '特殊件'
} as const

// 支付方式
export const PAYMENT_METHODS = {
  CASH: 'cash',
  WECHAT: 'wechat',
  ALIPAY: 'alipay',
  CREDIT_CARD: 'credit_card'
} as const

// 支付方式映射
export const PAYMENT_METHOD_MAP = {
  [PAYMENT_METHODS.CASH]: '现金',
  [PAYMENT_METHODS.WECHAT]: '微信支付',
  [PAYMENT_METHODS.ALIPAY]: '支付宝',
  [PAYMENT_METHODS.CREDIT_CARD]: '信用卡'
} as const

// 用户角色
export const USER_ROLES = {
  ADMIN: 'admin',
  COURIER: 'courier',
  CUSTOMER: 'customer'
} as const

// 用户角色映射
export const USER_ROLE_MAP = {
  [USER_ROLES.ADMIN]: '管理员',
  [USER_ROLES.COURIER]: '快递员',
  [USER_ROLES.CUSTOMER]: '客户'
} as const

// 通知类型
export const NOTIFICATION_TYPES = {
  ORDER: 'order',
  SYSTEM: 'system',
  PAYMENT: 'payment',
  DELIVERY: 'delivery'
} as const

// 通知类型映射
export const NOTIFICATION_TYPE_MAP = {
  [NOTIFICATION_TYPES.ORDER]: '订单通知',
  [NOTIFICATION_TYPES.SYSTEM]: '系统通知',
  [NOTIFICATION_TYPES.PAYMENT]: '支付通知',
  [NOTIFICATION_TYPES.DELIVERY]: '配送通知'
} as const

// 分页配置
export const PAGINATION = {
  PAGE_SIZE: 10,
  PAGE_SIZES: [10, 20, 50, 100]
} as const

// 缓存键
export const CACHE_KEYS = {
  TOKEN: 'token',
  USER_INFO: 'user_info',
  THEME: 'theme',
  LANGUAGE: 'language'
} as const

// 路由名称
export const ROUTE_NAMES = {
  HOME: 'home',
  LOGIN: 'login',
  REGISTER: 'register',
  ORDER_CREATE: 'order-create',
  ORDER_LIST: 'order-list',
  ORDER_DETAIL: 'order-detail',
  MAP: 'map',
  COURIER: 'courier',
  NOTIFICATION: 'notification',
  PROFILE: 'profile',
  SETTINGS: 'settings'
} as const

// 路由路径
export const ROUTE_PATHS = {
  [ROUTE_NAMES.HOME]: '/',
  [ROUTE_NAMES.LOGIN]: '/login',
  [ROUTE_NAMES.REGISTER]: '/register',
  [ROUTE_NAMES.ORDER_CREATE]: '/order/create',
  [ROUTE_NAMES.ORDER_LIST]: '/order/list',
  [ROUTE_NAMES.ORDER_DETAIL]: '/order/detail/:id',
  [ROUTE_NAMES.MAP]: '/map',
  [ROUTE_NAMES.COURIER]: '/courier',
  [ROUTE_NAMES.NOTIFICATION]: '/notification',
  [ROUTE_NAMES.PROFILE]: '/profile',
  [ROUTE_NAMES.SETTINGS]: '/settings'
} as const 