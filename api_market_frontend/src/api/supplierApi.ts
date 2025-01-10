import request from '@/utils/request'
import type { ApiResponse, PageDTO, PageReqDTO } from './types'

// 供应商API响应数据类型
export interface SupplierApiResDTO {
    id: number
    supplier: {
        id: number
        name: string
    }
    api: {
        id: number
        name: string
    }
    price: number
    enable: boolean
    createTime: string
}

// 通用的ID DTO
export interface LongIdDTO {
    id: number
}

// 创建供应商API绑定请求类型
export interface SupplierApiCreateDTO {
    supplier: LongIdDTO
    api: LongIdDTO
    price: number
    enable?: boolean
}

// 更新供应商API绑定请求类型
export interface SupplierApiUpdateDTO {
    id: number
    price?: number
    enable?: boolean
}

// 查询供应商API请求类型
export interface SupplierApiQueryReqDTO extends PageReqDTO {
    supplierId?: number
    apiId?: number
    enable?: boolean
}

export const supplierApiApi = {
    create(data: SupplierApiCreateDTO) {
        return request.post<ApiResponse<number>>('/supplier/api/bind', data)
    },

    update(data: SupplierApiUpdateDTO) {
        return request.put<ApiResponse<void>>('/supplier/api/bind', data)
    },

    page(data: SupplierApiQueryReqDTO) {
        return request.post<ApiResponse<PageDTO<SupplierApiResDTO>>>('/supplier/api/page', data)
    }
} 