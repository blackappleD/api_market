import request from '@/utils/request'
import type { ApiResponse, PageDTO, PageReqDTO } from './types'

export interface MerchantLogDTO {
  id: number;
  merchantId: number;
  merchantName: string;
  apiId: number;
  apiName: string;
  requestTime: string;
  responseTime: string;
  duration: number;
  requestParams?: string;
  responseData?: string;
  resCode: string;
  errorMsg?: string;
  createTime: string;
}

export interface MerchantLogQueryReqDTO extends PageReqDTO {
  merchantId?: number;
  apiId?: number;
  resCode?: string;
  startTime?: string;
  endTime?: string;
}

export const merchantLogApi = {
  page(params: MerchantLogQueryReqDTO) {
    return request.post<ApiResponse<PageDTO<MerchantLogDTO>>>('/merchant/log/page', params)
  }
} 