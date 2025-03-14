// 基础ID类型
export interface LongIdDTO {
    id?: number
}

// 基础ID和名称类型
export interface LongIdNameDTO extends LongIdDTO {
    name: string
} 