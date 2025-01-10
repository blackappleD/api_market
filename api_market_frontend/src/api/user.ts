import request from '@/utils/request'
import type { ApiResponse } from './types'
import type { TokenData } from '@/types/auth'
import type { UserInfo } from '@/types/user'

export interface LoginReqDTO {
  userAccount: string;
  password: string;
}

export const userApi = {
  login(data: LoginReqDTO) {
    return request.post<ApiResponse<TokenData>>('/user/login', data)
  },
  
  getUserInfo() {
    return request.get<ApiResponse<UserInfo>>('/user/info')
  },
  
  logout() {
    return request.post<ApiResponse<void>>('/user/logout')
  }
} 