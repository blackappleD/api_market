<template>
    <div class="supplier-api-list">
        <!-- 搜索区域 -->
        <div class="search-bar">
            <el-form :inline="true" :model="queryParams">
                <el-form-item label="供应商">
                    <el-select v-model="queryParams.supplierId" placeholder="请选择供应商" clearable style="width: 200px">
                        <el-option v-for="item in supplierOptions" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="API">
                    <el-select v-model="queryParams.apiId" placeholder="请选择API" clearable style="width: 200px">
                        <el-option v-for="item in apiOptions" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="状态">
                    <el-select v-model="queryParams.enable" placeholder="请选择状态" clearable style="width: 120px">
                        <el-option label="启用" :value="true" />
                        <el-option label="禁用" :value="false" />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="handleQuery">查询</el-button>
                    <el-button @click="handleReset">重置</el-button>
                </el-form-item>
            </el-form>
        </div>

        <el-button type="primary" @click="handleAdd">新增绑定</el-button>

        <el-table v-loading="loading" :data="tableData" style="margin-top: 20px">
            <el-table-column prop="supplier.name" label="供应商名称" />
            <el-table-column prop="api.name" label="API名称" />
            <el-table-column prop="priority" label="优先级" min-width="100" align="center">
                <template #header>
                    <el-tooltip content="值越大优先级越低" placement="top">
                        <span>优先级</span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column prop="price" label="进价">
                <template #default="{ row }">
                    {{ formatPrice(row.price) }}
                </template>
            </el-table-column>
            <el-table-column prop="enable" label="状态" width="100">
                <template #default="{ row }">
                    <el-tag :type="row.enable ? 'success' : 'danger'">
                        {{ row.enable ? '启用' : '禁用' }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180" />
            <el-table-column label="操作" width="200" fixed="right">
                <template #default="{ row }">
                    <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
                    <el-button link :type="row.enable ? 'danger' : 'success'" @click="handleToggleStatus(row)">
                        {{ row.enable ? '禁用' : '启用' }}
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <div class="pagination">
            <el-pagination v-model:current-page="queryParams.page" v-model:page-size="queryParams.size"
                :page-sizes="[10, 20, 50]" :total="total" layout="total, sizes, prev, pager, next"
                @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </div>

        <!-- 绑定表单对话框 -->
        <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
            <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="供应商" prop="supplier.id">
                    <el-select v-model="form.supplier.id" placeholder="请选择供应商" :disabled="isEdit">
                        <el-option v-for="item in supplierOptions" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="API" prop="api.id">
                    <el-select v-model="form.api.id" placeholder="请选择API">
                        <el-option v-for="item in apiOptions" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="进价" prop="price">
                    <el-input-number v-model="form.price" :precision="2" :step="0.01" :min="0" style="width: 180px"
                        placeholder="请输入进价" />
                </el-form-item>
                <el-form-item label="优先级" prop="priority">
                    <el-tooltip content="值越大优先级越低" placement="top">
                        <el-input-number v-model="form.priority" :precision="0" :step="1" :min="0" style="width: 180px"
                            placeholder="请输入优先级" />
                    </el-tooltip>
                </el-form-item>
                <el-form-item label="状态" v-if="isEdit">
                    <el-switch v-model="form.enable" />
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleSubmit">确定</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { supplierApiApi } from '@/api/supplierApi'
import { supplierApi } from '@/api/supplier'
import { apiApi } from '@/api/api'
import type { SupplierApiResDTO, SupplierApiCreateDTO, SupplierApiUpdateDTO, SupplierApiQueryReqDTO } from '@/api/supplierApi'
import type { SupplierDTO } from '@/api/supplier'
import type { ApiDTO } from '@/api/api'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const total = ref(0)
const tableData = ref<SupplierApiResDTO[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)

const supplierOptions = ref<SupplierDTO[]>([])
const apiOptions = ref<ApiDTO[]>([])

const formRef = ref<FormInstance>()
const form = reactive<SupplierApiCreateDTO | SupplierApiUpdateDTO>({
    supplier: { id: undefined },
    api: { id: undefined },
    price: 0,
    priority: 999,
    enable: true
})

const queryParams = reactive<SupplierApiQueryReqDTO>({
    page: 1,
    size: 10,
    supplierId: undefined,
    apiId: undefined,
    enable: undefined
})

const rules = {
    'supplier.id': [{ required: true, message: '请选择供应商', trigger: 'change' }],
    'api.id': [{ required: true, message: '请选择API', trigger: 'change' }],
    price: [{ required: true, message: '请输入进价', trigger: 'blur' }],
    priority: [{ required: true, message: '请输入优先级', trigger: 'blur' }]
}

const dialogTitle = computed(() => isEdit.value ? '编辑绑定' : '新增绑定')

// 加载选项数据
const loadOptions = async () => {
    try {
        const [supplierRes, apiRes] = await Promise.all([
            supplierApi.list(),
            apiApi.list()
        ])

        if (supplierRes.code === 200) {
            supplierOptions.value = supplierRes.data
        }
        if (apiRes.code === 200) {
            apiOptions.value = apiRes.data
        }
    } catch (error) {
        console.error('加载选项数据失败:', error)
        ElMessage.error('加载选项数据失败')
    }
}

// 加载表格数据
const loadData = async () => {
    try {
        loading.value = true
        const res = await supplierApiApi.page(queryParams)
        if (res.code === 200) {
            tableData.value = res.data.records
            total.value = res.data.total
        }
    } finally {
        loading.value = false
    }
}

const handleQuery = () => {
    queryParams.page = 1
    loadData()
}

const handleReset = () => {
    queryParams.supplierId = undefined
    queryParams.apiId = undefined
    queryParams.enable = undefined
    handleQuery()
}

const handleSizeChange = (val: number) => {
    queryParams.size = val
    loadData()
}

const handleCurrentChange = (val: number) => {
    queryParams.page = val
    loadData()
}

const handleToggleStatus = async (row: SupplierApiResDTO) => {
    try {
        await ElMessageBox.confirm(
            `确定要${row.enable ? '禁用' : '启用'}该绑定吗？`,
            '提示',
            {
                type: 'warning'
            }
        )

        await supplierApiApi.update({
            id: row.id,
            enable: !row.enable
        })

        ElMessage.success('操作成功')
        loadData()
    } catch (error) {
        if (error !== 'cancel') {
            console.error('操作失败:', error)
        }
    }
}

const handleAdd = () => {
    isEdit.value = false
    resetForm()
    dialogVisible.value = true
}

const handleEdit = (row: SupplierApiResDTO) => {
    isEdit.value = true
    Object.assign(form, {
        id: row.id,
        supplier: { id: row.supplier.id },
        api: { id: row.api.id },
        price: row.price,
        priority: row.priority,
        enable: row.enable
    })
    dialogVisible.value = true
}

const resetForm = () => {
    if (isEdit.value) {
        form.price = 0
        form.priority = 999
        form.enable = true
        if ('id' in form) {
            delete (form as any).id
        }
    } else {
        form.supplier = { id: undefined }
        form.api = { id: undefined }
        form.price = 0
        form.priority = 999
        form.enable = true
    }
}

const handleSubmit = async () => {
    if (!formRef.value) return

    await formRef.value.validate(async (valid) => {
        if (valid) {
            try {
                if (isEdit.value) {
                    const updateDto: SupplierApiUpdateDTO = {
                        id: (form as SupplierApiUpdateDTO).id,
                        supplier: { id: form.supplier.id },
                        api: { id: form.api.id },
                        price: form.price,
                        priority: form.priority,
                        enable: form.enable
                    }
                    await supplierApiApi.update(updateDto)
                } else {
                    await supplierApiApi.create(form as SupplierApiCreateDTO)
                }
                ElMessage.success('操作成功')
                dialogVisible.value = false
                loadData()
            } catch (error) {
                console.error('操作失败:', error)
            }
        }
    })
}

// 添加价格格式化函数
const formatPrice = (price: number | undefined) => {
    if (price === undefined || price === null) {
        return '0.00'
    }
    return Number(price).toFixed(2)
}

onMounted(() => {
    loadOptions()
    loadData()
})
</script>

<style lang="scss" scoped>
.supplier-api-list {
    padding: 20px;

    .search-bar {
        margin-bottom: 20px;
    }

    .pagination {
        margin-top: 20px;
        display: flex;
        justify-content: flex-end;
    }
}
</style>