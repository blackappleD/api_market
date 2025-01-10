import request from '@/utils/request'
import type { ApiResponse, PageDTO, PageReqDTO } from './types'

export interface ApiCategoryDTO {
  id: number;
  name: string;
  description: string;
  enable: boolean;
  createTime: string;
  updateTime: string;
}

export interface ApiCategoryCreateReqDTO {
  name: string;
  description?: string;
}

export interface ApiCategoryUpdateReqDTO {
  id: number;
  name: string;
  description?: string;
}

export interface ApiCategoryQueryReqDTO extends PageReqDTO {
  name?: string;
}

export const apiCategoryApi = {
  create(data: ApiCategoryCreateReqDTO) {
    return request.post<ApiResponse<number>>('/api/category', data)
  },

  update(data: ApiCategoryUpdateReqDTO) {
    return request.put<ApiResponse<void>>('/api/category', data)
  },

  get(id: number) {
    return request.get<ApiResponse<ApiCategoryDTO>>(`/api/category/${id}`)
  },

  page(data: ApiCategoryQueryReqDTO) {
    return request.post<ApiResponse<PageDTO<ApiCategoryDTO>>>('/api/category/page', data)
  },

  list() {
    return request.get<ApiResponse<ApiCategoryDTO[]>>('/api/category/list')
  }
} 