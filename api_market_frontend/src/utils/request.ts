import axios from 'axios'
import type { AxiosInstance, InternalAxiosRequestConfig } from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

const request: AxiosInstance = axios.create({
  baseURL: '/mg',
  timeout: 15000
})

// 请求拦截器
request.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // 从 localStorage 获取 token
    const token = localStorage.getItem('token')
    console.log('Current token:', token) // 调试日志

    // 确保 headers 对象存在
    config.headers = config.headers || {}
    
    // 设置基础请求头
    config.headers['Content-Type'] = 'application/json'
    
    if (token) {
      // 设置认证头
      config.headers['Authorization'] = `Bearer ${token}`
    }

    // 调试日志
    console.log('Request Config:', {
      url: config.url,
      method: config.method,
      headers: config.headers,
      data: config.data
    })

    return config
  },
  (error) => {
    console.error('Request Error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    // 调试日志
    console.log('Response:', {
      status: response.status,
      headers: response.headers,
      data: response.data
    })

    // 直接返回响应数据，不做额外处理
    return response.data
  },
  (error) => {
    console.error('Response Error:', error.response || error)
    
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      router.push('/login')
      ElMessage.error('未登录或登录已过期')
    } else {
      ElMessage.error(error.response?.data?.message || '请求失败')
    }
    return Promise.reject(error)
  }
)

export default request 