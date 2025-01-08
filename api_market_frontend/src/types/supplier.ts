export interface Supplier {
    id: string;
    supplierName: string;
    supplierCode: string;
    description?: string;
    contactName?: string;
    contactPhone?: string;
    contactEmail?: string;
    address?: string;
    status: number;
    createTime: string;
    updateTime: string;
}

export interface SupplierCreateReq {
    supplierName: string;
    supplierCode: string;
    description?: string;
    contactName?: string;
    contactPhone?: string;
    contactEmail?: string;
    address?: string;
}

export interface SupplierUpdateReq {
    id: string;
    supplierName: string;
    description?: string;
    contactName?: string;
    contactPhone?: string;
    contactEmail?: string;
    address?: string;
    status?: number;
}

export interface SupplierQueryReq {
    pageNum?: number;
    pageSize?: number;
    supplierName?: string;
    supplierCode?: string;
    contactName?: string;
    status?: number;
} 