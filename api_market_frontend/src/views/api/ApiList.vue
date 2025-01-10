<template>
    <div class="api-list">
        <!-- 搜索区域 -->
        <div class="search-bar">
            <el-form :inline="true" :model="queryParams">
                <el-form-item label="API分类">
                    <el-select v-model="queryParams.categoryId" placeholder="请选择分类" clearable style="width: 200px">
                        <el-option v-for="item in categoryOptions" :key="item.id" :label="item.name" :value="item.id" />
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

        <el-button type="primary" @click="handleAdd">新增API</el-button>

        <el-table v-loading="loading" :data="tableData" style="margin-top: 20px">
            <el-table-column prop="name" label="API名称" />
            <el-table-column prop="apiCode" label="API编码" />
            <el-table-column prop="category.name" label="所属分类" />
            <el-table-column prop="description" label="描述" show-overflow-tooltip />
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

        <!-- API表单对话框 -->
        <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
            <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="API名称" prop="name">
                    <el-input v-model="form.name" placeholder="请输入API名称" />
                </el-form-item>
                <el-form-item label="API编码" prop="apiCode" v-if="!isEdit">
                    <el-input v-model="form.apiCode" placeholder="请输入API编码" />
                </el-form-item>
                <el-form-item label="所属分类" prop="category.id">
                    <el-select v-model="selectedCategory" placeholder="请选择分类" @change="handleCategoryChange">
                        <el-option v-for="item in categoryOptions" :key="item.id" :label="item.name" :value="item" />
                    </el-select>
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
import { ref, reactive, computed, onMounted } from 'vue'
import { apiApi } from '@/api/api'
import { apiCategoryApi } from '@/api/apiCategory'
import type { ApiResDTO, ApiCreateReqDTO, ApiUpdateReqDTO, ApiQueryReqDTO } from '@/api/api'
import type { ApiCategoryDTO } from '@/api/apiCategory'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const total = ref(0)
const tableData = ref<ApiResDTO[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const categoryOptions = ref<ApiCategoryDTO[]>([])

const formRef = ref<FormInstance>()
const form = reactive<ApiCreateReqDTO | ApiUpdateReqDTO>({
    name: '',
    apiCode: '',
    category: {
        id: undefined
    },
    description: ''
})

const queryParams = reactive<ApiQueryReqDTO>({
    page: 1,
    size: 10,
    name: '',
    categoryId: undefined,
    enable: undefined
})

const rules = {
    name: [{ required: true, message: '请输入API名称', trigger: 'blur' }],
    apiCode: [{ required: true, message: '请输入API编码', trigger: 'blur' }],
    'category.id': [{ required: true, message: '请选择分类', trigger: 'change' }]
}

const dialogTitle = computed(() => isEdit.value ? '编辑API' : '新增API')

// 添加选中分类的响应式变量
const selectedCategory = ref<ApiCategoryDTO | null>(null)

// 处理分类选择变化
const handleCategoryChange = (val: ApiCategoryDTO | null) => {
    if (val) {
        form.category = {
            id: val.id
        }
    } else {
        form.category.id = undefined
        selectedCategory.value = undefined
    }
}

// 加载分类选项
const loadCategories = async () => {
    try {
        const res = await apiCategoryApi.list()
        if (res.code === 200) {
            categoryOptions.value = res.data
        }
    } catch (error) {
        console.error('加载分类列表失败:', error)
        ElMessage.error('加载分类列表失败')
    }
}

// 加载API列表
const loadData = async () => {
    try {
        loading.value = true
        const res = await apiApi.page(queryParams)
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
    queryParams.categoryId = undefined
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
    form.name = ''
    form.apiCode = ''
    form.category.id = undefined
    form.description = ''
    selectedCategory.value = null
    if ('id' in form) {
        delete (form as any).id
    }
}

const handleAdd = () => {
    isEdit.value = false
    resetForm()
    dialogVisible.value = true
}

const handleEdit = (row: ApiResDTO) => {
    isEdit.value = true
    Object.assign(form, {
        id: row.id,
        name: row.name,
        category: {
            id: row.category.id
        },
        description: row.description
    })
    selectedCategory.value = row.category
    dialogVisible.value = true
}

const handleSubmit = async () => {
    if (!formRef.value) return

    await formRef.value.validate(async (valid) => {
        if (valid) {
            try {
                if (isEdit.value) {
                    await apiApi.update({
                        ...form,
                        id: (form as ApiUpdateReqDTO).id
                    })
                } else {
                    await apiApi.create(form as ApiCreateReqDTO)
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

const handleToggleStatus = async (row: ApiResDTO) => {
    try {
        await ElMessageBox.confirm(
            `确定要${row.enable ? '禁用' : '启用'}该API吗？`,
            '提示',
            {
                type: 'warning'
            }
        )

        await apiApi.batchEnable({
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

onMounted(() => {
    loadCategories()
    loadData()
})
</script>

<style lang="scss" scoped>
.api-list {
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