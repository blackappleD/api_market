// 通用响应类型
export interface ApiResponse<T> {
  code: number;
  message: string | null;
  data: T;
  stackTraces: string[] | null;
}

// 分页响应类型
export interface PageDTO<T> {
  total: number;      // 总记录数
  records: T[];       // 数据列表
  page: number;       // 当前页码
  size: number;       // 每页大小
}

// 分页请求类型
export interface PageReqDTO {
  page: number;       // 页码，与后端 page 对应
  size: number;       // 每页大小，与后端 size 对应
}

// 批量启用/禁用请求类型
export interface BatchEnableReqDTO {
  ids: number[];
  enable: boolean;
} 