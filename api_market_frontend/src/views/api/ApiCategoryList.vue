<template>
    <div class="api-category-list">
        <!-- 搜索区域 -->
        <div class="search-bar">
            <el-form :inline="true" :model="queryParams">
                <el-form-item label="分类名称">
                    <el-input v-model="queryParams.name" placeholder="分类名称" clearable @keyup.enter="handleQuery" />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="handleQuery">查询</el-button>
                    <el-button @click="handleReset">重置</el-button>
                </el-form-item>
            </el-form>
        </div>

        <el-button type="primary" @click="handleAdd">新增分类</el-button>

        <el-table v-loading="loading" :data="tableData" style="margin-top: 20px">
            <el-table-column prop="name" label="分类名称" />
            <el-table-column prop="description" label="描述" show-overflow-tooltip />
            <el-table-column prop="createTime" label="创建时间" width="180" />
            <el-table-column label="操作" width="200" fixed="right">
                <template #default="{ row }">
                    <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
                    <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <div class="pagination">
            <el-pagination v-model:current-page="queryParams.page" v-model:page-size="queryParams.size"
                :page-sizes="[10, 20, 50]" :total="total" layout="total, sizes, prev, pager, next"
                @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </div>

        <!-- 分类表单对话框 -->
        <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
            <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="分类名称" prop="name">
                    <el-input v-model="form.name" placeholder="请输入分类名称" />
                </el-form-item>
                <el-form-item label="描述">
                    <el-input v-model="form.description" type="textarea" placeholder="请输入描述" />
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
import { ref, reactive, computed } from 'vue'
import { apiCategoryApi } from '@/api/apiCategory'
import type { ApiCategoryDTO, ApiCategoryQueryReqDTO } from '@/api/apiCategory'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const total = ref(0)
const tableData = ref<ApiCategoryDTO[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)

const formRef = ref<FormInstance>()
const form = reactive({
    id: '',
    name: '',
    description: ''
})

const queryParams = reactive<ApiCategoryQueryReqDTO>({
    page: 1,
    size: 10,
    name: ''
})

const rules: FormRules = {
    name: [
        { required: true, message: '请输入分类名称', trigger: 'blur' }
    ]
}

const dialogTitle = computed(() => isEdit.value ? '编辑分类' : '新增分类')

const loadData = async () => {
    try {
        loading.value = true
        const res = await apiCategoryApi.page(queryParams)
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
    form.id = ''
    form.name = ''
    form.description = ''
}

const handleAdd = () => {
    isEdit.value = false
    resetForm()
    dialogVisible.value = true
}

const handleEdit = (row: ApiCategoryDTO) => {
    isEdit.value = true
    Object.assign(form, row)
    dialogVisible.value = true
}

const handleSubmit = async () => {
    if (!formRef.value) return

    await formRef.value.validate(async (valid) => {
        if (valid) {
            try {
                if (isEdit.value) {
                    await apiCategoryApi.update(form)
                } else {
                    await apiCategoryApi.create(form)
                }
                ElMessage.success('操作成功')
                dialogVisible.value = false
                loadData()
            } catch (error) {
            }
        }
    })
}

const handleDelete = async (row: ApiCategoryDTO) => {
    try {
        await ElMessageBox.confirm(
            '确定要删除该分类吗？',
            '提示',
            {
                type: 'warning',
                confirmButtonText: '确定',
                cancelButtonText: '取消'
            }
        )

        await apiCategoryApi.delete(row.id)
        ElMessage.success('删除成功')
        loadData()
    } catch (error) {
        if (error !== 'cancel') {
            console.error('删除失败:', error)
            ElMessage.error('删除失败')
        }
    }
}

// 初始加载数据
loadData()
</script>

<style lang="scss" scoped>
.api-category-list {
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