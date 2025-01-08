export interface Merchant {
    id: string;
    merchantName: string;
    merchantCode: string;
    contactName: string;
    contactPhone: string;
    contactEmail: string;
    appKey: string;
    appSecret: string;
    status: number;
    createTime: string;
    updateTime: string;
}

export interface MerchantCreateReq {
    merchantName: string;
    merchantCode: string;
    contactName?: string;
    contactPhone?: string;
    contactEmail?: string;
}

export interface MerchantUpdateReq {
    id: string;
    merchantName: string;
    contactName?: string;
    contactPhone?: string;
    contactEmail?: string;
    status?: number;
}

export interface MerchantQueryReq {
    pageNum?: number;
    pageSize?: number;
    merchantName?: string;
    merchantCode?: string;
    contactName?: string;
    status?: number;
} 