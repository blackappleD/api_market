<template>
    <div class="supplier-container">
        <div class="search-bar">
            <el-form :inline="true" :model="queryParams">
                <el-form-item>
                    <el-input v-model="queryParams.name" placeholder="供应商名称" clearable @keyup.enter="handleQuery" />
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
        </div>

        <el-button type="primary" @click="handleAdd">新增供应商</el-button>

        <el-table v-loading="loading" :data="tableData" style="margin-top: 20px">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="name" label="供应商名称" min-width="120" />
            <el-table-column prop="supCode" label="供应商编码" min-width="120" />
            <el-table-column prop="description" label="描述" show-overflow-tooltip min-width="150" />
            <el-table-column prop="contactName" label="联系人" min-width="100" />
            <el-table-column prop="contactPhone" label="联系电话" min-width="120" />
            <el-table-column prop="contactEmail" label="联系邮箱" min-width="150" show-overflow-tooltip />
            <el-table-column prop="appKey" label="AppKey" min-width="200" show-overflow-tooltip />
            <el-table-column prop="enable" label="状态" width="80" align="center">
                <template #default="{ row }">
                    <el-tag :type="row.enable ? 'success' : 'danger'">
                        {{ row.enable ? '启用' : '禁用' }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180" />
            <el-table-column label="操作" width="200">
                <template #default="{ row }">
                    <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
                    <el-button link :type="row.enable ? 'danger' : 'success'" @click="handleToggleEnable(row)">
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

        <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
            <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="供应商名称" prop="name">
                    <el-input v-model="form.name" placeholder="请输入供应商名称" />
                </el-form-item>
                <el-form-item label="供应商编码" prop="supCode" v-if="!isEdit">
                    <el-input v-model="form.supCode" placeholder="请输入供应商编码" />
                </el-form-item>
                <el-form-item label="描述">
                    <el-input v-model="form.description" type="textarea" placeholder="请输入描述" />
                </el-form-item>
                <el-form-item label="联系人">
                    <el-input v-model="form.contactName" placeholder="请输入联系人" />
                </el-form-item>
                <el-form-item label="联系电话">
                    <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
                </el-form-item>
                <el-form-item label="联系邮箱" prop="contactEmail">
                    <el-input v-model="form.contactEmail" placeholder="请输入联系邮箱" />
                </el-form-item>
                <el-form-item label="AppKey">
                    <el-input v-model="form.appKey" placeholder="请输入AppKey" />
                </el-form-item>
                <el-form-item label="AppSecret">
                    <el-input v-model="form.appSecret" placeholder="请输入AppSecret" />
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
import { ref, reactive, onMounted, computed } from 'vue'
import { supplierApi } from '@/api/supplier'
import type { SupplierDTO, SupplierQueryReqDTO } from '@/api/supplier'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const total = ref(0)
const tableData = ref<SupplierDTO[]>([])

const queryParams = reactive<SupplierQueryReqDTO>({
    page: 1,
    size: 10,
    name: '',
    enable: undefined
})

const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref<FormInstance>()

const form = reactive({
    id: 0,
    name: '',
    supCode: '',
    description: '',
    contactName: '',
    contactPhone: '',
    contactEmail: '',
    appKey: '',
    appSecret: '',
    enable: true
})

const rules: FormRules = {
    name: [{ required: true, message: '请输入供应商名称', trigger: 'blur' }],
    supCode: [{ required: true, message: '请输入供应商编码', trigger: 'blur' }],
    contactEmail: [{ type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }]
}

const dialogTitle = computed(() => isEdit.value ? '编辑供应商' : '新增供应商')

const loadData = async () => {
    try {
        loading.value = true
        const res = await supplierApi.page(queryParams)
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
    queryParams.name = ''
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

const handleAdd = () => {
    isEdit.value = false
    resetForm()
    dialogVisible.value = true
}

const handleEdit = (row: SupplierDTO) => {
    isEdit.value = true
    Object.assign(form, row)
    dialogVisible.value = true
}

const handleToggleEnable = async (row: SupplierDTO) => {
    try {
        await ElMessageBox.confirm(
            `确定要${row.enable ? '禁用' : '启用'}该供应商吗？`,
            '提示',
            {
                type: 'warning'
            }
        )

        await supplierApi.batchEnable({
            ids: [row.id],
            enable: !row.enable
        })

        ElMessage.success('操作成功')
        loadData()
    } catch (error) {
        // 用户取消操作
    }
}

const resetForm = () => {
    form.id = 0
    form.name = ''
    form.supCode = ''
    form.description = ''
    form.contactName = ''
    form.contactPhone = ''
    form.contactEmail = ''
    form.appKey = ''
    form.appSecret = ''
    form.enable = true
}

const handleSubmit = async () => {
    if (!formRef.value) return

    await formRef.value.validate(async (valid) => {
        if (valid) {
            try {
                if (isEdit.value) {
                    await supplierApi.update(form)
                } else {
                    await supplierApi.create(form)
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

onMounted(() => {
    loadData()
})
</script>

<style lang="scss" scoped>
.supplier-container {
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