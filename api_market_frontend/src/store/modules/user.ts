import { defineStore } from 'pinia'
import { ref } from 'vue'
import { userApi } from '@/api/user'
import type { UserInfo } from '@/types/user'
import { ElMessage } from 'element-plus'

export const useUserStore = defineStore('user', () => {
    const token = ref(localStorage.getItem('token') || '')
    const userInfo = ref<UserInfo>()

    const login = async (userAccount: string, password: string) => {
        try {
            const res = await userApi.login({ userAccount, password })
            console.log('Login response:', res)

            if (res.code !== 200 || !res.data?.tokenValue) {
                throw new Error(res.message || '登录失败：未获取到token')
            }

            // 存储token
            const newToken = res.data.tokenValue
            localStorage.setItem('token', newToken)
            token.value = newToken

            console.log('Token stored:', newToken)

            // 获取用户信息
            const userInfoRes = await userApi.getUserInfo()
            console.log('UserInfo response:', userInfoRes)

            if (userInfoRes.code === 200) {
                userInfo.value = userInfoRes.data
            } else {
                throw new Error(userInfoRes.message || '获取用户信息失败')
            }
        } catch (error) {
            console.error('Login error:', error)
            ElMessage.error(error instanceof Error ? error.message : '登录失败')
            throw error
        }
    }

    const clearUserInfo = () => {
        token.value = ''
        userInfo.value = undefined
        localStorage.removeItem('token')
    }

    const logout = async () => {
        try {
            await userApi.logout()
        } finally {
            clearUserInfo()
        }
    }

    return {
        token,
        userInfo,
        login,
        logout,
        clearUserInfo
    }
}) 