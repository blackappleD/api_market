import request from '@/utils/request'
import type { ApiResponse, PageDTO, PageReqDTO } from './types'

// 分类ID和名称类型
export interface LongIdNameDTO {
    id: string;      // Long 类型对应 string
    name: string;
}

// API响应数据类型
export interface ApiResDTO {
    id: string;      // Long 类型对应 string
    apiCode: string;
    name: string;
    category: LongIdNameDTO;
    description?: string;
    enable: boolean;
    createTime: string;
    updateTime: string;
}

// 创建API请求类型
export interface ApiCreateReqDTO {
    apiCode: string;      // required
    name: string;         // required
    category: LongIdDTO;  // required
    description?: string;
}

// 更新API请求类型
export interface ApiUpdateReqDTO {
    id: string;          // Long 类型对应 string
    name: string;        // required
    category: LongIdDTO;
    description?: string;
}

// 查询API请求类型
export interface ApiQueryReqDTO extends PageReqDTO {
    categoryId?: string;  // Long 类型对应 string
    enable?: boolean;
}

export const apiApi = {
    create(data: ApiCreateReqDTO) {
        return request.post<ApiResponse<string>>('/api', data)  // 返回值改为 string
    },

    update(data: ApiUpdateReqDTO) {
        return request.put<ApiResponse<void>>('/api', data)
    },

    get(id: string) {  // 参数改为 string
        return request.get<ApiResponse<ApiResDTO>>(`/api/${id}`)
    },

    page(data: ApiQueryReqDTO) {
        return request.post<ApiResponse<PageDTO<ApiResDTO>>>('/api/page', data)
    },

    batchEnable(data: { ids: string[]; enable: boolean }) {  // ids 改为 string[]
        return request.post<ApiResponse<void>>('/api/enable', data)
    }
} 