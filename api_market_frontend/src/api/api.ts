import request from '@/utils/request'
import type { ApiResponse, PageDTO, PageReqDTO } from './types'

// API响应数据类型
export interface ApiDTO {
    id: number
    name: string
    apiCode: string
    description?: string
    category: {
        id: number
        name: string
    }
    enable: boolean
    createTime: string
}

// 创建API请求类型
export interface ApiCreateReqDTO {
    name: string
    apiCode: string
    description?: string
    category: {
        id: number
    }
}

// 更新API请求类型
export interface ApiUpdateReqDTO {
    id: number
    name: string
    description?: string
    category: {
        id: number
    }
    enable?: boolean
}

// 查询API请求类型
export interface ApiQueryReqDTO extends PageReqDTO {
    name?: string
    categoryId?: number
    enable?: boolean
}

export const apiApi = {
    create(data: ApiCreateReqDTO) {
        return request.post<ApiResponse<number>>('/api', data)
    },

    update(data: ApiUpdateReqDTO) {
        return request.put<ApiResponse<void>>('/api', data)
    },

    get(id: number) {
        return request.get<ApiResponse<ApiDTO>>(`/api/${id}`)
    },

    list() {
        return request.get<ApiResponse<ApiDTO[]>>('/api/list')
    },

    page(data: ApiQueryReqDTO) {
        return request.post<ApiResponse<PageDTO<ApiDTO>>>('/api/page', data)
    },

    batchEnable(data: { ids: number[]; enable: boolean }) {
        return request.put<ApiResponse<void>>('/api/enable', data)
    }
} 