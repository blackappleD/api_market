<template>
    <div class="supplier-list">
        <div class="search-bar">
            <el-form :inline="true" :model="queryForm" class="form-inline">
                <el-form-item label="供应商名称">
                    <el-input v-model="queryForm.supplierName" placeholder="请输入供应商名称" clearable />
                </el-form-item>
                <el-form-item label="供应商编码">
                    <el-input v-model="queryForm.supplierCode" placeholder="请输入供应商编码" clearable />
                </el-form-item>
                <el-form-item label="联系人">
                    <el-input v-model="queryForm.contactName" placeholder="请输入联系人" clearable />
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
                    <el-button type="success" @click="handleAdd">新增供应商</el-button>
                </el-form-item>
            </el-form>
        </div>

        <el-table v-loading="loading" :data="tableData" border style="width: 100%">
            <el-table-column prop="supplierName" label="供应商名称" />
            <el-table-column prop="supplierCode" label="供应商编码" />
            <el-table-column prop="contactName" label="联系人" />
            <el-table-column prop="contactPhone" label="联系电话" />
            <el-table-column prop="contactEmail" label="联系邮箱" />
            <el-table-column prop="status" label="状态">
                <template #default="{ row }">
                    <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                        {{ row.status === 1 ? '启用' : '禁用' }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" />
            <el-table-column label="操作" width="200">
                <template #default="{ row }">
                    <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
                    <el-button
                        :type="row.status === 1 ? 'danger' : 'success'"
                        link
                        @click="handleStatusChange(row)"
                    >
                        {{ row.status === 1 ? '禁用' : '启用' }}
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

        <el-dialog
            v-model="dialogVisible"
            :title="dialogType === 'add' ? '新增供应商' : '编辑供应商'"
            width="500px"
        >
            <el-form
                ref="formRef"
                :model="form"
                :rules="rules"
                label-width="100px"
            >
                <el-form-item label="供应商名称" prop="supplierName">
                    <el-input v-model="form.supplierName" />
                </el-form-item>
                <el-form-item
                    v-if="dialogType === 'add'"
                    label="供应商编码"
                    prop="supplierCode"
                >
                    <el-input v-model="form.supplierCode" />
                </el-form-item>
                <el-form-item label="描述">
                    <el-input
                        v-model="form.description"
                        type="textarea"
                        :rows="3"
                    />
                </el-form-item>
                <el-form-item label="联系人">
                    <el-input v-model="form.contactName" />
                </el-form-item>
                <el-form-item label="联系电话">
                    <el-input v-model="form.contactPhone" />
                </el-form-item>
                <el-form-item label="联系邮箱" prop="contactEmail">
                    <el-input v-model="form.contactEmail" />
                </el-form-item>
                <el-form-item label="地址">
                    <el-input v-model="form.address" />
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleSubmit">确定</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import type { FormInstance, FormRules } from 'element-plus';
import { supplierApi } from '@/api/supplier';
import type { Supplier } from '@/types/supplier';

const loading = ref(false);
const dialogVisible = ref(false);
const dialogType = ref<'add' | 'edit'>('add');
const formRef = ref<FormInstance>();
const tableData = ref<Supplier[]>([]);
const total = ref(0);

const queryForm = reactive({
    pageNum: 1,
    pageSize: 10,
    supplierName: '',
    supplierCode: '',
    contactName: '',
    status: undefined as number | undefined
});

const form = reactive({
    id: '',
    supplierName: '',
    supplierCode: '',
    description: '',
    contactName: '',
    contactPhone: '',
    contactEmail: '',
    address: ''
});

const rules: FormRules = {
    supplierName: [
        { required: true, message: '请输入供应商名称', trigger: 'blur' }
    ],
    supplierCode: [
        { required: true, message: '请输入供应商编码', trigger: 'blur' }
    ],
    contactEmail: [
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
    ]
};

const loadData = async () => {
    loading.value = true;
    try {
        const res = await supplierApi.page(queryForm);
        tableData.value = res.content;
        total.value = res.totalElements;
    } finally {
        loading.value = false;
    }
};

const handleSearch = () => {
    queryForm.pageNum = 1;
    loadData();
};

const resetQuery = () => {
    queryForm.supplierName = '';
    queryForm.supplierCode = '';
    queryForm.contactName = '';
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

const resetForm = () => {
    form.id = '';
    form.supplierName = '';
    form.supplierCode = '';
    form.description = '';
    form.contactName = '';
    form.contactPhone = '';
    form.contactEmail = '';
    form.address = '';
};

const handleAdd = () => {
    dialogType.value = 'add';
    resetForm();
    dialogVisible.value = true;
};

const handleEdit = (row: Supplier) => {
    dialogType.value = 'edit';
    Object.assign(form, row);
    dialogVisible.value = true;
};

const handleStatusChange = async (row: Supplier) => {
    try {
        await ElMessageBox.confirm(
            `确认要${row.status === 1 ? '禁用' : '启用'}该供应商吗？`,
            '提示',
            {
                type: 'warning'
            }
        );
        await supplierApi.update({
            id: row.id,
            supplierName: row.supplierName,
            status: row.status === 1 ? 0 : 1
        });
        ElMessage.success('操作成功');
        loadData();
    } catch (error) {
        // 用户取消操作
    }
};

const handleSubmit = async () => {
    if (!formRef.value) return;
    await formRef.value.validate();
    
    try {
        if (dialogType.value === 'add') {
            await supplierApi.create(form);
        } else {
            await supplierApi.update(form);
        }
        ElMessage.success('操作成功');
        dialogVisible.value = false;
        loadData();
    } catch (error) {
        // 错误处理
    }
};

loadData();
</script>

<style scoped>
.supplier-list {
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