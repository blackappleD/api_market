// API响应类型
export interface ApiResponse<T> {
  code: number;
  message?: string;
  data?: T;
}

// 分页响应类型
export interface PageDTO<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
  pages: number;
}

// 分页请求类型
export interface PageReqDTO {
  page: number;
  size: number;
}

// 批量启用/禁用请求类型
export interface BatchEnableReqDTO {
  ids: number[];
  enable: boolean;
} 