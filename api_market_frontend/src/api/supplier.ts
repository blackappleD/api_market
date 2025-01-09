import axios from '@/utils/axios';
import type { Supplier, SupplierCreateReq, SupplierUpdateReq, SupplierQueryReq } from '@/types/supplier';
import type { Page } from '@/types/common';

export const supplierApi = {
    create(data: SupplierCreateReq) {
        return axios.post<Supplier>('/supplier', data);
    },
    
    update(data: SupplierUpdateReq) {
        return axios.put<Supplier>('/supplier', data);
    },
    
    page(params: SupplierQueryReq) {
        const { pageNum, pageSize, ...rest } = params;
        return axios.get<Page<Supplier>>('/supplier/page', { 
            params: {
                ...rest,
                pageNum: pageNum - 1,
                pageSize
            }
        });
    },

    getById(id: number) {
        return axios.get<Supplier>(`/supplier/${id}`);
    },

    batchEnable(ids: number[]) {
        return axios.post('/supplier/batch-enable', ids);
    },

    batchDisable(ids: number[]) {
        return axios.post('/supplier/batch-disable', ids);
    },

    export(params: SupplierQueryReq) {
        return axios.get('/supplier/export', {
            params,
            responseType: 'blob'
        });
    },

    import(file: File) {
        const formData = new FormData();
        formData.append('file', file);
        return axios.post('/supplier/import', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
    }
}; 