import axios from '@/utils/axios';
import { MerchantCreateReq, MerchantUpdateReq, MerchantQueryReq } from '@/types/merchant';

export const merchantApi = {
    create(data: MerchantCreateReq) {
        return axios.post('/mg/merchant', data);
    },
    
    update(data: MerchantUpdateReq) {
        return axios.put('/mg/merchant', data);
    },
    
    page(params: MerchantQueryReq) {
        return axios.get('/mg/merchant/page', { params });
    }
}; 