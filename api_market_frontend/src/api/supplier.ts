import axios from '@/utils/axios';
import { SupplierCreateReq, SupplierUpdateReq, SupplierQueryReq } from '@/types/supplier';

export const supplierApi = {
    create(data: SupplierCreateReq) {
        return axios.post('/mg/supplier', data);
    },
    
    update(data: SupplierUpdateReq) {
        return axios.put('/mg/supplier', data);
    },
    
    page(params: SupplierQueryReq) {
        return axios.get('/mg/supplier/page', { params });
    },

    getById(id: string) {
        return axios.get(`/mg/supplier/${id}`);
    },

    batchEnable(ids: string[]) {
        return axios.post('/mg/supplier/batch-enable', ids);
    },

    batchDisable(ids: string[]) {
        return axios.post('/mg/supplier/batch-disable', ids);
    },

    export(params: SupplierQueryReq) {
        return axios.get('/mg/supplier/export', {
            params,
            responseType: 'blob'
        });
    },

    import(file: File) {
        const formData = new FormData();
        formData.append('file', file);
        return axios.post('/mg/supplier/import', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
    }
}; 