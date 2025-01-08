<template>
    <div class="merchant-list">
        <div class="search-bar">
            <el-form :inline="true" :model="queryForm" class="form-inline">
                <el-form-item label="商户名称">
                    <el-input v-model="queryForm.merchantName" placeholder="请输入商户名称" clearable />
                </el-form-item>
                <el-form-item label="商户编码">
                    <el-input v-model="queryForm.merchantCode" placeholder="请输入商户编码" clearable />
                </el-form-item>
                <el-form-item label="状态">
                    <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
                        <el-option label="启用" :value="1" />
                        <el-option label="禁用" :value="0" />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="handleSearch">查询</el-button>
                    <el-button @click="resetQuery">重置</el-button>
                    <el-button type="success" @click="handleAdd">新增商户</el-button>
                </el-form-item>
            </el-form>
        </div>

        <el-table v-loading="loading" :data="tableData" border style="width: 100%">
            <el-table-column prop="merchantName" label="商户名称" />
            <el-table-column prop="merchantCode" label="商户编码" />
            <el-table-column prop="contactName" label="联系人" />
            <el-table-column prop="contactPhone" label="联系电话" />
            <el-table-column prop="contactEmail" label="联系邮箱" />
            <el-table-column prop="appKey" label="AppKey" width="220" show-overflow-tooltip />
            <el-table-column prop="appSecret" label="AppSecret" width="220" show-overflow-tooltip />
            <el-table-column prop="status" label="状态" width="80">
                <template #default="scope">
                    <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
                        {{ scope.row.status === 1 ? '启用' : '禁用' }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="180" fixed="right">
                <template #default="scope">
                    <el-button type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
                    <el-button 
                        :type="scope.row.status === 1 ? 'danger' : 'success'" 
                        link 
                        @click="handleStatusChange(scope.row)"
                    >
                        {{ scope.row.status === 1 ? '禁用' : '启用' }}
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <div class="pagination">
            <el-pagination
                v-model:current-page="queryForm.pageNum"
                v-model:page-size="queryForm.pageSize"
                :total="total"
                :page-sizes="[10, 20, 50, 100]"
                layout="total, sizes, prev, pager, next"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
            />
        </div>

        <!-- 新增/编辑对话框 -->
        <el-dialog
            :title="dialogTitle"
            v-model="dialogVisible"
            width="500px"
            :close-on-click-modal="false"
        >
            <el-form
                ref="formRef"
                :model="form"
                :rules="rules"
                label-width="100px"
            >
                <el-form-item label="商户名称" prop="merchantName">
                    <el-input v-model="form.merchantName" placeholder="请输入商户名称" />
                </el-form-item>
                <el-form-item label="商户编码" prop="merchantCode" v-if="!form.id">
                    <el-input v-model="form.merchantCode" placeholder="请输入商户编码" />
                </el-form-item>
                <el-form-item label="联系人" prop="contactName">
                    <el-input v-model="form.contactName" placeholder="请输入联系人" />
                </el-form-item>
                <el-form-item label="联系电话" prop="contactPhone">
                    <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
                </el-form-item>
                <el-form-item label="联系邮箱" prop="contactEmail">
                    <el-input v-model="form.contactEmail" placeholder="请输入联系邮箱" />
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
                    确定
                </el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import type { FormInstance } from 'element-plus';
import { merchantApi } from '@/api/merchant';
import type { Merchant, MerchantQueryReq } from '@/types/merchant';

const loading = ref(false);
const submitLoading = ref(false);
const dialogVisible = ref(false);
const dialogTitle = ref('');
const total = ref(0);
const tableData = ref<Merchant[]>([]);

const queryForm = reactive<MerchantQueryReq>({
    pageNum: 1,
    pageSize: 10,
    merchantName: '',
    merchantCode: '',
    status: undefined
});

const formRef = ref<FormInstance>();
const form = reactive({
    id: '',
    merchantName: '',
    merchantCode: '',
    contactName: '',
    contactPhone: '',
    contactEmail: ''
});

const rules = {
    merchantName: [{ required: true, message: '请输入商户名称', trigger: 'blur' }],
    merchantCode: [{ required: true, message: '请输入商户编码', trigger: 'blur' }],
    contactEmail: [{ type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }]
};

const loadData = async () => {
    loading.value = true;
    try {
        const res = await merchantApi.page(queryForm);
        tableData.value = res.data.content;
        total.value = res.data.totalElements;
    } catch (error) {
        console.error('加载商户列表失败:', error);
        ElMessage.error('加载商户列表失败');
    } finally {
        loading.value = false;
    }
};

const handleSearch = () => {
    queryForm.pageNum = 1;
    loadData();
};

const resetQuery = () => {
    queryForm.merchantName = '';
    queryForm.merchantCode = '';
    queryForm.status = undefined;
    handleSearch();
};

const handleSizeChange = (val: number) => {
    queryForm.pageSize = val;
    loadData();
};

const handleCurrentChange = (val: number) => {
    queryForm.pageNum = val;
    loadData();
};

const handleAdd = () => {
    dialogTitle.value = '新增商户';
    form.id = '';
    form.merchantName = '';
    form.merchantCode = '';
    form.contactName = '';
    form.contactPhone = '';
    form.contactEmail = '';
    dialogVisible.value = true;
};

const handleEdit = (row: Merchant) => {
    dialogTitle.value = '编辑商户';
    Object.assign(form, row);
    dialogVisible.value = true;
};

const handleStatusChange = async (row: Merchant) => {
    try {
        await merchantApi.update({
            id: row.id,
            merchantName: row.merchantName,
            status: row.status === 1 ? 0 : 1
        });
        ElMessage.success('操作成功');
        loadData();
    } catch (error) {
        console.error('更新状态失败:', error);
        ElMessage.error('操作失败');
    }
};

const handleSubmit = async () => {
    if (!formRef.value) return;
    
    await formRef.value.validate(async (valid) => {
        if (valid) {
            submitLoading.value = true;
            try {
                if (form.id) {
                    await merchantApi.update(form);
                } else {
                    await merchantApi.create(form);
                }
                ElMessage.success('操作成功');
                dialogVisible.value = false;
                loadData();
            } catch (error) {
                console.error('保存商户失败:', error);
                ElMessage.error('操作失败');
            } finally {
                submitLoading.value = false;
            }
        }
    });
};

onMounted(() => {
    loadData();
});
</script>

<style scoped>
.merchant-list {
    padding: 20px;
}

.search-bar {
    margin-bottom: 20px;
    background: #fff;
    padding: 20px;
    border-radius: 4px;
}

.pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
}
</style> 