export interface Supplier {
    id: number;
    supplierName: string;
    supplierCode: string;
    contactName: string;
    contactPhone: string;
    contactEmail: string;
    status: number;
    createTime: string;
    updateTime: string;
}

export interface SupplierCreateReq {
    supplierName: string;
    supplierCode: string;
    contactName: string;
    contactPhone: string;
    contactEmail: string;
}

export interface SupplierUpdateReq {
    id: number;
    supplierName: string;
    supplierCode?: string;
    contactName?: string;
    contactPhone?: string;
    contactEmail?: string;
    status?: number;
}

export interface SupplierQueryReq {
    pageNum: number;
    pageSize: number;
    supplierName?: string;
    supplierCode?: string;
    status?: number;
} 