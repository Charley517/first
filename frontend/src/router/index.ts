import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '@/pages/HomePage.vue'
import CreateOrder from '@/pages/CreateOrder.vue'
import OrderList from '@/pages/OrderList.vue'
import MapPage from '@/pages/MapPage.vue'
import TracePage from '@/pages/TracePage.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomePage
    },
    {
      path: '/create',
      name: 'create',
      component: CreateOrder
    },
    {
      path: '/orders',
      name: 'orders',
      component: OrderList
    },
    {
      path: '/map',
      name: 'map',
      component: MapPage
    },
    {
      path: '/trace/:orderId',
      name: 'trace',
      component: TracePage
    },
    {
      path: '/couriers',
      name: 'couriers',
      component: () => import('../pages/CourierPage.vue')
    },
    {
      path: '/notifications',
      name: 'notifications',
      component: () => import('../pages/NotificationPage.vue')
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('../pages/ProfilePage.vue')
    },
    {
      path: '/change-password',
      name: 'change-password',
      component: () => import('../pages/ChangePasswordPage.vue')
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../pages/LoginPage.vue')
    },
    {
      path: '/users',
      name: 'users',
      component: () => import('../pages/UserPage.vue')
    }
  ]
})

export default router 