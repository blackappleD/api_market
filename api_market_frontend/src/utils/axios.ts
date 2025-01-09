import axios from 'axios';
import { ElMessage } from 'element-plus';
import router from '@/router';

const instance = axios.create({
    timeout: 10000,
    withCredentials: true
});

// 请求拦截器
instance.interceptors.request.use(
    config => {
        // 可以在这里添加loading状态
        return config;
    },
    error => {
        console.error('Request error:', error);
        return Promise.reject(error);
    }
);

// 响应拦截器
instance.interceptors.response.use(
    response => {
        const res = response.data;
        
        // 这里可以根据后端的响应结构进行适配
        if (res.code === 0 || res.code === 200) {
            return res.data;
        }
        
        ElMessage.error(res.message || '请求失败');
        return Promise.reject(new Error(res.message || '请求失败'));
    },
    error => {
        console.error('Response error:', error);
        
        if (error.response) {
            switch (error.response.status) {
                case 401:
                    ElMessage.error('未登录或登录已过期');
                    router.push('/login');
                    break;
                case 403:
                    ElMessage.error('没有权限访问');
                    break;
                case 404:
                    ElMessage.error('请求的资源不存在');
                    break;
                case 500:
                    ElMessage.error('服务器错误，请稍后重试');
                    break;
                default:
                    ElMessage.error(error.response.data?.message || '请求失败');
            }
        } else {
            ElMessage.error('网络错误，请检查网络连接');
        }
        
        return Promise.reject(error);
    }
);

export default instance;
