import request from '@/utils/request'
import type { ApiResponse, PageDTO, PageReqDTO } from './types'
import type { LongIdDTO } from './supplierApi'

// API销售响应数据类型
export interface ApiSaleResDTO {
    id: number
    api: {
        id: number
        name: string
    }
    merchant: {
        id: number
        name: string
    }
    price: number
    description: string
    enable: boolean
    createTime: string
}

// 创建API销售请求类型
export interface ApiSaleCreateDTO {
    api: LongIdDTO
    merchant: LongIdDTO
    price: number
    description: string
    enable?: boolean
}

// 更新API销售请求类型
export interface ApiSaleUpdateDTO {
    id: number
    price?: number
    description?: string
    enable?: boolean
}

// 查询API销售请求类型
export interface ApiSaleQueryReqDTO extends PageReqDTO {
    apiId?: number
    merchantId?: number
    enable?: boolean
}

export const apiSaleApi = {
    create(data: ApiSaleCreateDTO) {
        return request.post<ApiResponse<number>>('/api/sale', data)
    },

    update(data: ApiSaleUpdateDTO) {
        return request.put<ApiResponse<void>>('/api/sale', data)
    },

    get(id: number) {
        return request.get<ApiResponse<ApiSaleResDTO>>(`/api/sale/${id}`)
    },

    list() {
        return request.get<ApiResponse<ApiSaleResDTO[]>>('/api/sale/list')
    },

    page(data: ApiSaleQueryReqDTO) {
        return request.post<ApiResponse<PageDTO<ApiSaleResDTO>>>('/api/sale/page', data)
    },

    batchEnable(data: { ids: number[]; enable: boolean }) {
        return request.put<ApiResponse<void>>('/api/sale/enable', data)
    }
} 