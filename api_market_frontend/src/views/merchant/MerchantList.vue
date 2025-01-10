<template>
    <div class="merchant-list">
        <!-- 搜索区域 -->
        <el-card class="search-card">
            <el-form :inline="true" :model="queryParams" class="search-form">
                <el-form-item label="商户名称">
                    <el-input v-model="queryParams.name" placeholder="请输入商户名称" clearable />
                </el-form-item>
                <el-form-item label="商户编码">
                    <el-input v-model="queryParams.merCode" placeholder="请输入商户编码" clearable />
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

        <el-button type="primary" @click="handleAdd" style="margin-bottom: 16px">新增商户</el-button>

        <el-card>
            <el-table v-loading="loading" :data="tableData" border>
                <el-table-column prop="name" label="商户名称" min-width="150" show-overflow-tooltip />
                <el-table-column prop="merCode" label="商户编码" min-width="120" />
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
                <el-table-column label="操作" width="280" fixed="right">
                    <template #default="{ row }">
                        <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
                        <el-button link type="primary" @click="handleView(row)">查看</el-button>
                        <el-button link type="primary" @click="handleSendEmail(row)">
                            <el-icon>
                                <Message />
                            </el-icon>
                            邮件
                        </el-button>
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

        <!-- 商户表单对话框 -->
        <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
            <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" :disabled="dialogType === 'view'">
                <el-form-item label="商户名称" prop="name">
                    <el-input v-model="form.name" placeholder="请输入商户名称" />
                </el-form-item>
                <el-form-item label="商户编码" prop="merCode" v-if="dialogType === 'add' || dialogType === 'view'">
                    <el-input v-model="form.merCode" placeholder="请输入商户编码" />
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
                <el-form-item label="AppKey" v-if="dialogType === 'view'">
                    <el-input v-model="form.appKey" readonly />
                </el-form-item>
                <el-form-item label="AppSecret" v-if="dialogType === 'view'">
                    <el-input v-model="form.appSecret" readonly />
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
import { ref, reactive, computed } from 'vue'
import { merchantApi } from '@/api/merchant'
import type { MerchantDTO, MerchantQueryReqDTO } from '@/api/merchant'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Message } from '@element-plus/icons-vue'

const loading = ref(false)
const total = ref(0)
const tableData = ref<MerchantDTO[]>([])
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit' | 'view'>('add')

const formRef = ref<FormInstance>()
const form = reactive({
    id: 0,
    name: '',
    merCode: '',
    contactName: '',
    contactPhone: '',
    contactEmail: '',
    description: '',
    appKey: '',
    appSecret: ''
})

const queryParams = reactive<MerchantQueryReqDTO>({
    page: 1,
    size: 10,
    name: '',
    merCode: '',
    enable: undefined
})

const rules: FormRules = {
    name: [{ required: true, message: '请输入商户名称', trigger: 'blur' }],
    merCode: [{ required: true, message: '请输入商户编码', trigger: 'blur' }],
    contactEmail: [{ required: true, type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }]
}

const dialogTitle = computed(() => {
    switch (dialogType.value) {
        case 'add':
            return '新增商户'
        case 'edit':
            return '编辑商户'
        case 'view':
            return '商户详情'
        default:
            return ''
    }
})

const loadData = async () => {
    try {
        loading.value = true
        const res = await merchantApi.page(queryParams)
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
    queryParams.merCode = ''
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

const resetForm = () => {
    form.id = 0
    form.name = ''
    form.merCode = ''
    form.contactName = ''
    form.contactPhone = ''
    form.contactEmail = ''
    form.description = ''
    form.appKey = ''
    form.appSecret = ''
}

const handleAdd = () => {
    dialogType.value = 'add'
    resetForm()
    dialogVisible.value = true
}

const handleEdit = (row: MerchantDTO) => {
    dialogType.value = 'edit'
    Object.assign(form, row)
    dialogVisible.value = true
}

const handleView = (row: MerchantDTO) => {
    dialogType.value = 'view'
    Object.assign(form, row)
    dialogVisible.value = true
}

const handleSubmit = async () => {
    if (!formRef.value) return

    await formRef.value.validate(async (valid) => {
        if (valid) {
            try {
                if (dialogType.value === 'add') {
                    await merchantApi.create(form)
                } else {
                    await merchantApi.update(form)
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

const handleToggleStatus = async (row: MerchantDTO) => {
    try {
        await ElMessageBox.confirm(
            `确定要${row.enable ? '禁用' : '启用'}该商户吗？`,
            '提示',
            {
                type: 'warning'
            }
        )

        await merchantApi.update({
            id: row.id,
            name: row.name,
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

const handleSendEmail = async (row: MerchantDTO) => {
    try {
        await ElMessageBox.confirm(
            '确定要发送AppKey和AppSecret到商户邮箱吗？',
            '提示',
            {
                type: 'warning',
                confirmButtonText: '确定',
                cancelButtonText: '取消'
            }
        )

        await merchantApi.sendEmail(row.id)
        ElMessage.success('邮件发送成功')
    } catch (error) {
        if (error !== 'cancel') {
            console.error('发送邮件失败:', error)
            ElMessage.error('发送邮件失败')
        }
    }
}

// 初始加载数据
loadData()
</script>

<style lang="scss" scoped>
.merchant-list {
    padding: 20px;

    .search-card {
        margin-bottom: 20px;
    }

    .pagination {
        margin-top: 20px;
        display: flex;
        justify-content: flex-end;
    }
}
</style>