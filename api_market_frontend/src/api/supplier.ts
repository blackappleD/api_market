import request from '@/utils/request'
import type { ApiResponse, PageDTO, PageReqDTO } from './types'

export interface SupplierDTO {
  id: number;
  name: string;
  supCode: string;
  description?: string;
  contactName?: string;
  contactPhone?: string;
  contactEmail?: string;
  appKey: string;
  appSecret: string;
  enable: boolean;
  createTime: string;
}

export interface SupplierCreateReqDTO {
  name: string;
  supCode: string;
  description?: string;
  contactName?: string;
  contactPhone?: string;
  contactEmail?: string;
  enable?: boolean;
}

export interface SupplierUpdateReqDTO {
  id: number;
  name: string;
  description?: string;
  contactName?: string;
  contactPhone?: string;
  contactEmail?: string;
  enable?: boolean;
}

export interface SupplierQueryReqDTO extends PageReqDTO {
  name?: string;
  supCode?: string;
  enable?: boolean;
}

export const supplierApi = {
  create(data: SupplierCreateReqDTO) {
    return request.post<ApiResponse<number>>('/supplier', data)
  },

  update(data: SupplierUpdateReqDTO) {
    return request.put<ApiResponse<void>>('/supplier', data)
  },

  get(id: number) {
    return request.get<ApiResponse<SupplierDTO>>(`/supplier/${id}`)
  },

  page(data: SupplierQueryReqDTO) {
    return request.post<ApiResponse<PageDTO<SupplierDTO>>>('/supplier/page', data)
  },

  batchEnable(data: { ids: number[]; enable: boolean }) {
    return request.post<ApiResponse<void>>('/supplier/enable', data)
  },

  list() {
    return request.get<ApiResponse<SupplierDTO[]>>('/supplier/list')
  }
} 