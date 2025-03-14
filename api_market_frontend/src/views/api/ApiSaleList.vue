<template>
    <div class="api-sale-container">
        <!-- 搜索区域 -->
        <el-card class="search-card">
            <el-form :inline="true" :model="queryParams" class="search-form">
                <el-form-item label="API">
                    <el-select v-model="queryParams.apiId" placeholder="请选择API" clearable style="width: 200px"
                        :popper-append-to-body="false">
                        <el-option v-for="item in apiOptions" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="商户">
                    <el-select v-model="queryParams.merchantId" placeholder="请选择商户" clearable style="width: 200px"
                        :popper-append-to-body="false">
                        <el-option v-for="item in merchantOptions" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="状态">
                    <el-select v-model="queryParams.enable" placeholder="请选择状态" clearable style="width: 120px"
                        :popper-append-to-body="false">
                        <el-option label="启用" :value="true" />
                        <el-option label="禁用" :value="false" />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="handleQuery">查询</el-button>
                    <el-button @click="handleReset">重置</el-button>
                </el-form-item>
            </el-form>
        </el-card>

        <el-button type="primary" @click="handleAdd" style="margin: 16px 0">新增销售</el-button>

        <el-card>
            <el-table v-loading="loading" :data="tableData" border>
                <el-table-column prop="api.name" label="API名称" min-width="120" show-overflow-tooltip />
                <el-table-column prop="merchant.name" label="商户名称" min-width="120" show-overflow-tooltip />
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
        </el-card>

        <!-- 销售表单对话框 -->
        <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
            <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="API" prop="api.id" v-if="!isEdit">
                    <el-select v-model="form.api.id" placeholder="请选择API">
                        <el-option v-for="item in apiOptions" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="商户" prop="merchant.id" v-if="!isEdit">
                    <el-select v-model="form.merchant.id" placeholder="请选择商户">
                        <el-option v-for="item in merchantOptions" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="销售价格" prop="price">
                    <el-input-number v-model="form.price" :precision="4" :step="0.0001" :min="0" style="width: 180px"
                        placeholder="请输入销售价格" />
                </el-form-item>
                <el-form-item label="描述" prop="description">
                    <el-input v-model="form.description" type="textarea" placeholder="请输入描述" />
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
import { apiSaleApi } from '@/api/apiSale'
import { apiApi } from '@/api/api'
import { merchantApi } from '@/api/merchant'
import type { ApiSaleResDTO, ApiSaleCreateDTO, ApiSaleUpdateDTO, ApiSaleQueryReqDTO } from '@/api/apiSale'
import type { ApiDTO } from '@/api/api'
import type { MerchantDTO } from '@/api/merchant'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const total = ref(0)
const tableData = ref<ApiSaleResDTO[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)

const apiOptions = ref<ApiDTO[]>([])
const merchantOptions = ref<MerchantDTO[]>([])

const formRef = ref<FormInstance>()
const form = reactive<ApiSaleCreateDTO | ApiSaleUpdateDTO>({
    api: { id: undefined },
    merchant: { id: undefined },
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
    'api.id': [{ required: true, message: '请选择API', trigger: 'change' }],
    'merchant.id': [{ required: true, message: '请选择商户', trigger: 'change' }],
    price: [{ required: true, message: '请输入销售价格', trigger: 'blur' }]
}

const dialogTitle = computed(() => isEdit.value ? '编辑API销售' : '新增API销售')

const loadData = async () => {
    loading.value = true
    try {
        const res = await apiSaleApi.page(queryParams)
        if (res.code === 200 && res.data) {
            tableData.value = res.data.records
            total.value = res.data.total
        }
    } catch (error) {
        console.error('加载数据失败:', error)
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

const handleView = async (row: ApiSaleResDTO) => {
    try {
        const res = await apiSaleApi.get(row.id)
        if (res.code === 200) {
            // 这里可以显示详情对话框
            ElMessage.info('查看功能待实现')
        }
    } catch (error) {
        console.error('获取详情失败:', error)
        ElMessage.error('获取详情失败')
    }
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
    isEdit.value = false
    resetForm()
    dialogVisible.value = true
}

const handleEdit = (row: ApiSaleResDTO) => {
    isEdit.value = true
    form.api = { id: row.api.id }
    form.merchant = { id: row.merchant.id }
    form.price = row.price
    form.description = row.description
    form.enable = row.enable
    if ('id' in form) {
        (form as ApiSaleUpdateDTO).id = row.id
    }
    dialogVisible.value = true
}

const resetForm = () => {
    if (isEdit.value) {
        form.price = 0
        form.description = ''
        form.enable = true
        if ('id' in form) {
            delete (form as any).id
        }
    } else {
        form.api = { id: undefined }
        form.merchant = { id: undefined }
        form.price = 0
        form.description = ''
        form.enable = true
    }
}

const handleSubmit = async () => {
    if (!formRef.value) return

    await formRef.value.validate(async (valid) => {
        if (valid) {
            try {
                if (isEdit.value) {
                    await apiSaleApi.update(form as ApiSaleUpdateDTO)
                } else {
                    await apiSaleApi.create(form as ApiSaleCreateDTO)
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
const formatPrice = (price: number) => {
    return price.toFixed(2)
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

    .search-card {
        margin-bottom: 20px;
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
    }
}
</style>