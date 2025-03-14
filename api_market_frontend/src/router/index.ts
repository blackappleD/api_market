import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
    {
        path: '/',
        component: () => import('@/views/Home.vue'),
        redirect: '/supplier',
        children: [
            // 供应商管理
            {
                path: '/supplier',
                name: 'Supplier',
                component: () => import('@/views/supplier/SupplierList.vue')
            },
            {
                path: '/supplier-api',
                name: 'SupplierApi',
                component: () => import('@/views/supplier/SupplierApiList.vue')
            },
            
            // API管理
            {
                path: '/api-category',
                name: 'ApiCategory',
                component: () => import('@/views/api/ApiCategoryList.vue')
            },
            {
                path: '/api',
                name: 'Api',
                component: () => import('@/views/api/ApiList.vue')
            },
            {
                path: '/api/sale',
                name: 'ApiSale',
                component: () => import('@/views/api/ApiSaleList.vue')
            },
            
            // 商户管理
            {
                path: '/merchant',
                name: 'Merchant',
                component: () => import('@/views/merchant/MerchantList.vue')
            },
            {
                path: '/merchant-log',
                name: 'MerchantLog',
                component: () => import('@/views/merchant/MerchantLogList.vue')
            },
            {
                path: '/merchant-stats',
                name: 'MerchantStats',
                component: () => import('@/views/merchant/MerchantStatsList.vue')
            },
            {
                path: '/merchant-api-log',
                name: 'MerchantApiLog',
                component: () => import('@/views/merchant/MerchantApiLogList.vue')
            }
        ]
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/login/Login.vue')
    }
]

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')
    if (to.path === '/login') {
        next()
    } else if (!token) {
        next('/login')
    } else {
        next()
    }
})

export default router 