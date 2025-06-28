import type { Directive } from 'vue'
import { useGlobalStore } from '@/store'
import { USER_ROLES } from '@/constants'

// 角色权限指令
export const role: Directive = {
  mounted(el, binding) {
    const { value } = binding
    const globalStore = useGlobalStore()
    const userRole = globalStore.userInfo.role

    if (value && value instanceof Array && value.length > 0) {
      if (!value.includes(userRole)) {
        el.parentNode?.removeChild(el)
      }
    } else {
      throw new Error('需要指定角色数组! 例如 v-role="[\'admin\',\'courier\']"')
    }
  }
}

// 管理员权限指令
export const admin: Directive = {
  mounted(el) {
    const globalStore = useGlobalStore()
    const userRole = globalStore.userInfo.role

    if (userRole !== USER_ROLES.ADMIN) {
      el.parentNode?.removeChild(el)
    }
  }
}

// 快递员权限指令
export const courier: Directive = {
  mounted(el) {
    const globalStore = useGlobalStore()
    const userRole = globalStore.userInfo.role

    if (userRole !== USER_ROLES.COURIER) {
      el.parentNode?.removeChild(el)
    }
  }
}

// 客户权限指令
export const customer: Directive = {
  mounted(el) {
    const globalStore = useGlobalStore()
    const userRole = globalStore.userInfo.role

    if (userRole !== USER_ROLES.CUSTOMER) {
      el.parentNode?.removeChild(el)
    }
  }
}

// 注册所有权限指令
export function setupPermissionDirectives(app: any) {
  app.directive('role', role)
  app.directive('admin', admin)
  app.directive('courier', courier)
  app.directive('customer', customer)
} 