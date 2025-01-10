import request from '@/utils/request'
import type { ApiResponse, PageDTO, PageReqDTO } from './types'

export interface MerchantDTO {
  id: number;
  name: string;
  merCode: string;
  appKey: string;
  appSecret: string;
  contactName: string;
  contactPhone: string;
  contactEmail: string;
  description?: string;
  enable: boolean;
  createTime: string;
}

export interface MerchantCreateReqDTO {
  name: string;
  merCode: string;
  contactName: string;
  contactPhone: string;
  contactEmail: string;
  description?: string;
}

export interface MerchantUpdateReqDTO {
  id: number;
  name: string;
  contactName: string;
  contactPhone: string;
  contactEmail: string;
  description?: string;
  enable?: boolean;
}

export interface MerchantQueryReqDTO extends PageReqDTO {
  name?: string;
  merCode?: string;
  enable?: boolean;
}

export const merchantApi = {
  create(data: MerchantCreateReqDTO) {
    return request.post<ApiResponse<number>>('/merchant', data)
  },

  update(data: MerchantUpdateReqDTO) {
    return request.put<ApiResponse<void>>('/merchant', data)
  },

  get(id: number) {
    return request.get<ApiResponse<MerchantDTO>>(`/merchant/${id}`)
  },

  page(data: MerchantQueryReqDTO) {
    return request.post<ApiResponse<PageDTO<MerchantDTO>>>('/merchant/page', data)
  },

  sendEmail(id: string) {
    return request.post<ApiResponse<void>>(`/merchant/${id}/send_email`)
  }
} 