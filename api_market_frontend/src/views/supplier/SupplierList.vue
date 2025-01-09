<template>
    <div class="supplier-list">
        <!-- 搜索区域 -->
        <el-card class="search-card">
            <el-form :inline="true" :model="queryForm" class="search-form">
                <el-form-item label="供应商名称">
                    <el-input v-model="queryForm.supplierName" placeholder="请输入供应商名称" clearable />
                </el-form-item>
                <el-form-item label="供应商编码">
                    <el-input v-model="queryForm.supplierCode" placeholder="请输入供应商编码" clearable />
                </el-form-item>
                <el-form-item label="状态">
                    <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
                        <el-option label="启用" :value="1" />
                        <el-option label="禁用" :value="0" />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="handleSearch">
                        <el-icon><Search /></el-icon>查询
                    </el-button>
                    <el-button @click="resetQuery">
                        <el-icon><Refresh /></el-icon>重置
                    </el-button>
                </el-form-item>
            </el-form>
            <div class="operation-group">
                <el-button type="primary" @click="handleAdd">
                    <el-icon><Plus /></el-icon>新增供应商
                </el-button>
                <el-button type="success" @click="handleExport">
                    <el-icon><Download /></el-icon>导出
                </el-button>
                <el-button type="warning" @click="handleImport">
                    <el-icon><Upload /></el-icon>导入
                </el-button>
            </div>
        </el-card>

        <!-- 表格区域 -->
        <el-card class="table-card">
            <el-table
                v-loading="loading"
                :data="tableData"
                border
                stripe
                style="width: 100%"
            >
                <el-table-column type="selection" width="55" align="center" />
                <el-table-column prop="supplierName" label="供应商名称" min-width="150" show-overflow-tooltip />
                <el-table-column prop="supplierCode" label="供应商编码" min-width="120" />
                <el-table-column prop="contactName" label="联系人" min-width="100" />
                <el-table-column prop="contactPhone" label="联系电话" min-width="120" />
                <el-table-column prop="contactEmail" label="联系邮箱" min-width="150" show-overflow-tooltip />
                <el-table-column prop="status" label="状态" width="100" align="center">
                    <template #default="{ row }">
                        <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                            {{ row.status === 1 ? '启用' : '禁用' }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" label="创建时间" width="180" />
                <el-table-column label="操作" width="180" fixed="right" align="center">
                    <template #default="{ row }">
                        <el-button type="primary" link @click="handleEdit(row)">
                            <el-icon><Edit /></el-icon>编辑
                        </el-button>
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

            <!-- 分页 -->
            <div class="pagination">
                <el-pagination
                    v-model:current-page="queryForm.pageNum"
                    v-model:page-size="queryForm.pageSize"
                    :total="total"
                    :page-sizes="[10, 20, 50, 100]"
                    layout="total, sizes, prev, pager, next, jumper"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                />
            </div>
        </el-card>

        <!-- 新增/编辑对话框 -->
        <el-dialog
            :title="dialogType === 'add' ? '新增供应商' : '编辑供应商'"
            v-model="dialogVisible"
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
                <el-form-item label="供应商编码" prop="supplierCode" v-if="dialogType === 'add'">
                    <el-input v-model="form.supplierCode" />
                </el-form-item>
                <el-form-item label="联系人" prop="contactName">
                    <el-input v-model="form.contactName" />
                </el-form-item>
                <el-form-item label="联系电话" prop="contactPhone">
                    <el-input v-model="form.contactPhone" />
                </el-form-item>
                <el-form-item label="联系邮箱" prop="contactEmail">
                    <el-input v-model="form.contactEmail" />
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
import type { Supplier, SupplierQueryReq } from '@/types/supplier';

const loading = ref(false);
const dialogVisible = ref(false);
const dialogType = ref<'add' | 'edit'>('add');
const formRef = ref<FormInstance>();
const tableData = ref<Supplier[]>([]);
const total = ref(0);

const queryForm = reactive<SupplierQueryReq>({
    pageNum: 1,
    pageSize: 10,
    supplierName: '',
    supplierCode: '',
    status: undefined
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
    try {
        loading.value = true;
        const res = await supplierApi.page(queryForm);
        tableData.value = res.content;
        total.value = res.totalElements;
    } catch (error) {
        console.error('加载数据失败:', error);
        ElMessage.error('加载数据失败，请稍后重试');
    } finally {
        loading.value = false;
    }
};

const handleSearch = () => {
    queryForm.pageNum = 1;
    loadData();
};

const resetQuery = () => {
    queryForm.pageNum = 1;
    queryForm.pageSize = 10;
    queryForm.supplierName = '';
    queryForm.supplierCode = '';
    queryForm.status = undefined;
    loadData();
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

<style lang="scss" scoped>
.supplier-list {
    min-height: 100%;
    padding: 20px;
    background-color: #f0f2f5;

    .search-card {
        margin-bottom: 20px;
        
        .search-form {
            @include flex(row, flex-start, center);
            flex-wrap: wrap;
            gap: 20px;
            margin-bottom: 20px;
        }

        .operation-group {
            @include flex(row, flex-start, center);
            gap: 10px;
        }
    }

    .table-card {
        .el-table {
            margin-top: 20px;
            
            th {
                background-color: #f5f7fa;
                color: $text-primary;
            }
        }

        .pagination {
            @include flex(row, flex-end, center);
            margin-top: 20px;
        }
    }
}
</style> 