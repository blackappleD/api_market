export interface EnumField {
    id: string;
    name: string;
}

export interface EnumDTO {
    key: string;
    fields: EnumField[];
}

import request from '@/utils/request'

export const enumApi = {
    getAllEnum(): Promise<ApiResponse<EnumDTO[]>> {
        return request.get('/enum/list')
    }
}

// 全局枚举缓存
let enumCache: Record<string, EnumField[]> = {}

// 获取并缓存所有枚举
export async function loadAllEnums(): Promise<void> {
    try {
        const res = await enumApi.getAllEnum()
        if (res.code === 200 && res.data) {
            res.data.forEach(item => {
                enumCache[item.key] = item.fields
            })
        }
    } catch (error) {
        console.error('加载枚举数据失败:', error)
    }
}

// 获取指定枚举的选项
export function getEnumOptions(enumKey: string): EnumField[] {
    return enumCache[enumKey] || []
}

// 获取枚举显示文本
export function getEnumMessage(enumKey: string, code: string | null | undefined): string {
    if (code === null || code === undefined) {
        return ''
    }
    const fields = enumCache[enumKey]
    if (!fields) return code
    const field = fields.find(f => f.id === code)
    return field ? field.name : code
} 