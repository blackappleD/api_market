import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Home from '@/views/Home.vue'
import Subscribe from '@/views/Subscribe.vue'
import Login from '@/views/Login.vue'
import Register from '@/views/Register.vue'
import RealTimePrice from '@/views/RealTimePrice.vue'
import store from '@/store'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    children: [
      {
        path: '/supplier',
        name: 'SupplierList',
        component: () => import('@/views/supplier/SupplierList.vue'),
        meta: { title: '供应商管理' }
      },
      {
        path: '/api',
        name: 'ApiList',
        component: () => import('@/views/api/ApiList.vue'),
        meta: { title: 'API管理' }
      },
      {
        path: '/api/category',
        name: 'ApiCategoryList',
        component: () => import('@/views/api/category/ApiCategoryList.vue'),
        meta: { title: 'API分类管理' }
      }
    ]
  },
  {
    path: '/subscribe',
    name: 'Subscribe',
    component: Subscribe,
    meta: { requiresAuth: true }
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/realtime',
    name: 'RealTimePrice',
    component: RealTimePrice,
    meta: { requiresAuth: true }
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/'
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!store.state.auth.isLoggedIn) {
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      })
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router 