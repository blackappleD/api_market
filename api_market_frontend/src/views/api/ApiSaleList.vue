<template>
    <div class="api-sale-container">
        <!-- 搜索区域 -->
        <div class="search-bar">
            <el-form :inline="true" :model="queryParams" class="search-form">
                <el-form-item label="商户">
                    <el-select v-model="queryParams.merchantId" placeholder="请选择商户" clearable style="width: 200px">
                        <el-option v-for="item in merchantOptions" :key="item.id" :label="item.name" :value="item.id" />
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

        <el-button type="primary" @click="handleAdd" style="margin-bottom: 16px">新增销售</el-button>

        <el-table v-loading="loading" :data="tableData" style="margin-top: 20px">
            <el-table-column prop="merchant.name" label="商户名称" min-width="120" show-overflow-tooltip />
            <el-table-column prop="api.name" label="API名称" min-width="120" show-overflow-tooltip />
            <el-table-column prop="api.apiCode" label="API编码" min-width="120">
                <template #default="{ row }">
                    {{ getEnumMessage('ApiCode', row.api?.apiCode) }}
                </template>
            </el-table-column>
            <el-table-column prop="price" label="销售价格" min-width="100">
                <template #default="{ row }">
                    {{ formatPrice(row.price) }}
                </template>
            </el-table-column>
            <el-table-column prop="description" label="描述" min-width="150" show-overflow-tooltip />
            <el-table-column prop="enable" label="状态" width="80" align="center">
                <template #default="{ row }">
                    <el-tag :type="row.enable ? 'success' : 'danger'">
                        {{ row.enable ? '启用' : '禁用' }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="160" />
            <el-table-column label="操作" width="180" fixed="right">
                <template #default="{ row }">
                    <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
                    <el-button link type="primary" @click="handleView(row)">查看</el-button>
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

        <!-- 销售表单对话框 -->
        <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
            <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" :disabled="dialogType === 'view'">
                <el-form-item label="商户" prop="merchant.id" v-if="dialogType === 'add'">
                    <el-select v-model="form.merchant.id" placeholder="请选择商户">
                        <el-option v-for="item in merchantOptions" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="API" prop="api.id" v-if="dialogType === 'add'">
                    <el-select v-model="form.api.id" placeholder="请选择API">
                        <el-option v-for="item in apiOptions" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="销售价格" prop="price">
                    <el-input-number v-model="form.price" :precision="2" :step="0.01" :min="0" style="width: 180px"
                        placeholder="请输入销售价格" />
                </el-form-item>
                <el-form-item label="描述" prop="description">
                    <el-input v-model="form.description" type="textarea" placeholder="请输入描述" />
                </el-form-item>
                <el-form-item label="状态" v-if="dialogType === 'edit'">
                    <el-switch v-model="form.enable" />
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="dialogVisible = false">{{ dialogType === 'view' ? '关闭' : '取消' }}</el-button>
                <el-button type="primary" @click="handleSubmit" v-if="dialogType !== 'view'">确定</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup>
import { ref, reactive, computed, onMounted, onBeforeUnmount } from 'vue'
import { apiSaleApi } from '@/api/apiSale'
import { apiApi } from '@/api/api'
import { merchantApi } from '@/api/merchant'
import type { ApiSaleResDTO, ApiSaleCreateDTO, ApiSaleUpdateDTO, ApiSaleQueryReqDTO } from '@/api/apiSale'
import type { ApiDTO } from '@/api/api'
import type { MerchantDTO } from '@/api/merchant'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getEnumMessage } from '@/api/enum'

const loading = ref(false)
const total = ref(0)
const tableData = ref<ApiSaleResDTO[]>([])
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit' | 'view'>('add')

const apiOptions = ref<ApiDTO[]>([])
const merchantOptions = ref<MerchantDTO[]>([])

// 添加取消控制器
const abortController = ref<AbortController | null>(null)

// 在组件卸载前清理资源
onBeforeUnmount(() => {
    // 取消未完成的请求
    if (abortController.value) {
        abortController.value.abort()
    }
    // 清理数据
    tableData.value = []
    apiOptions.value = []
    merchantOptions.value = []
})

const formRef = ref<FormInstance>()
const form = reactive<ApiSaleCreateDTO | ApiSaleUpdateDTO>({
    api: { id: undefined },
    merchant: { id: undefined },
    routerSupplierApis: [],
    price: 0,
    description: '',
    enable: true
})

const queryParams = reactive<ApiSaleQueryReqDTO>({
    page: 1,
    size: 10,
    apiId: undefined,
    merchantId: undefined,
    enable: undefined
})

const rules = {
    'merchant.id': [{ required: true, message: '请选择商户', trigger: 'change' }],
    'api.id': [{ required: true, message: '请选择API', trigger: 'change' }],
    price: [{ required: true, message: '请输入销售价格', trigger: 'blur' }]
}

const dialogTitle = computed(() => {
    switch (dialogType.value) {
        case 'add':
            return '新增API销售'
        case 'edit':
            return '编辑API销售'
        case 'view':
            return 'API销售详情'
        default:
            return ''
    }
})

const loadData = async () => {
    // 如果存在之前的请求，取消它
    if (abortController.value) {
        abortController.value.abort()
    }
    // 创建新的取消控制器
    abortController.value = new AbortController()

    loading.value = true
    try {
        const res = await apiSaleApi.page(queryParams)
        if (res.code === 200 && res.data) {
            // 确保每条记录都有价格字段，如果没有则设为0
            tableData.value = res.data.records.map(record => ({
                ...record,
                price: record.price ?? 0
            }))
            total.value = res.data.total
        }
    } catch (error) {
        if (error.name !== 'AbortError') {
            console.error('加载数据失败:', error)
        }
    } finally {
        loading.value = false
    }
}

const loadApiOptions = async () => {
    try {
        const res = await apiApi.list()
        if (res.code === 200 && res.data) {
            apiOptions.value = res.data
        }
    } catch (error) {
        console.error('加载API选项失败:', error)
    }
}

const loadMerchantOptions = async () => {
    try {
        const res = await merchantApi.page({
            page: 1,
            size: 100,
            name: undefined,
            merCode: undefined,
            enable: true
        })

        if (res.code === 200 && res.data) {
            merchantOptions.value = res.data.records
        }
    } catch (error) {
        console.error('加载商户选项失败:', error)
    }
}

const handleQuery = () => {
    queryParams.page = 1
    loadData()
}

const handleReset = () => {
    queryParams.apiId = undefined
    queryParams.merchantId = undefined
    queryParams.enable = undefined
    handleQuery()
}

const handleSizeChange = (size: number) => {
    queryParams.size = size
    loadData()
}

const handleCurrentChange = (page: number) => {
    queryParams.page = page
    loadData()
}

const handleView = (row: ApiSaleResDTO) => {
    dialogType.value = 'view'
    Object.assign(form, {
        id: row.id,
        api: { id: row.api?.id },
        merchant: { id: row.merchant?.id },
        routerSupplierApis: row.routerSupplierApis?.map(supplierApi => ({ id: supplierApi.id })) || [],
        price: row.price ?? 0,
        description: row.description || '',
        enable: row.enable ?? true
    })
    dialogVisible.value = true
}

const handleToggleStatus = async (row: ApiSaleResDTO) => {
    try {
        await ElMessageBox.confirm(
            `确定要${row.enable ? '禁用' : '启用'}该销售吗？`,
            '提示',
            {
                type: 'warning'
            }
        )

        await apiSaleApi.batchEnable({
            ids: [row.id],
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
    dialogType.value = 'add'
    resetForm()
    dialogVisible.value = true
}

const handleEdit = (row: ApiSaleResDTO) => {
    dialogType.value = 'edit'
    // 更新表单数据，包含价格字段
    const updateForm = {
        id: row.id,
        api: { id: row.api?.id },
        merchant: { id: row.merchant?.id },
        routerSupplierApis: row.routerSupplierApis?.map(supplierApi => ({ id: supplierApi.id })) || [],
        price: row.price ?? 0,
        description: row.description || '',
        enable: row.enable ?? true
    } as ApiSaleUpdateDTO

    // 将表单数据复制到响应式对象中
    Object.assign(form, updateForm)
    dialogVisible.value = true
}

const resetForm = () => {
    if (dialogType.value === 'edit') {
        form.price = 0
        form.description = ''
        form.routerSupplierApis = []
        form.enable = true
        if ('id' in form) {
            delete (form as any).id
        }
    } else {
        form.api = { id: undefined }
        form.merchant = { id: undefined }
        form.routerSupplierApis = []
        form.price = 0
        form.description = ''
        form.enable = true
    }
    if (formRef.value) {
        formRef.value.resetFields()
    }
}

const handleSubmit = async () => {
    if (!formRef.value) return

    await formRef.value.validate(async (valid) => {
        if (valid) {
            try {
                if (dialogType.value === 'edit') {
                    // 构造更新请求DTO，确保与后端 ApiSaleUpdateReqDTO 匹配
                    const updateDto: ApiSaleUpdateDTO = {
                        id: (form as ApiSaleUpdateDTO).id,
                        api: { id: form.api.id },
                        merchant: { id: form.merchant.id },
                        routerSupplierApis: form.routerSupplierApis || [],
                        price: form.price,
                        description: form.description,
                        enable: form.enable
                    }
                    await apiSaleApi.update(updateDto)
                } else {
                    // 构造创建请求DTO，确保与后端 ApiSaleCreateReqDTO 匹配
                    const createDto: ApiSaleCreateDTO = {
                        api: { id: form.api.id },
                        merchant: { id: form.merchant.id },
                        routerSupplierApis: form.routerSupplierApis || [],
                        enable: form.enable
                    }
                    await apiSaleApi.create(createDto)
                }
                ElMessage.success('操作成功')
                dialogVisible.value = false
                loadData()
            } catch (error) {
                console.error('操作失败:', error)
                ElMessage.error('操作失败')
            }
        }
    })
}

// 价格格式化函数
const formatPrice = (price: number | undefined) => {
    if (price === undefined || price === null) {
        return '0.00'
    }
    return Number(price).toFixed(2)
}

onMounted(() => {
    loadApiOptions()
    loadMerchantOptions()
    loadData()
})
</script>

<style lang="scss" scoped>
.api-sale-container {
    padding: 20px;

    .search-bar {
        margin-bottom: 20px;
        background-color: #fff;
        padding: 16px;
        border-radius: 4px;
    }

    .el-table {
        :deep(th.el-table__cell) {
            background-color: #fff;
            border-bottom: 1px solid #ebeef5;
        }

        :deep(.el-table__row) {
            background-color: #fff;
        }

        :deep(.el-table__cell) {
            border-bottom: 1px solid #ebeef5;
        }
    }

    :deep(.el-select__popper) {
        width: auto !important;
        min-width: 120px !important;
    }

    :deep(.el-select-dropdown__item) {
        padding: 0 12px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .pagination {
        margin-top: 20px;
        display: flex;
        justify-content: flex-end;
        background-color: #fff;
        padding: 16px;
        border-radius: 4px;
    }
}
</style>