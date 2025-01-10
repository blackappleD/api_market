<template>
    <div class="merchant-log-list">
        <!-- 搜索区域 -->
        <el-card class="search-card">
            <el-form :inline="true" :model="queryParams" class="search-form">
                <el-form-item label="商户">
                    <el-select v-model="queryParams.merchantId" placeholder="请选择商户" clearable style="width: 200px"
                        :popper-append-to-body="false">
                        <el-option v-for="item in merchantOptions" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="API">
                    <el-select v-model="queryParams.apiId" placeholder="请选择API" clearable style="width: 200px"
                        :popper-append-to-body="false">
                        <el-option v-for="item in apiOptions" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="响应状态">
                    <el-select v-model="queryParams.resCode" placeholder="请选择状态" clearable style="width: 120px"
                        :popper-append-to-body="false">
                        <el-option label="成功" value="200" />
                        <el-option label="失败" value="500" />
                    </el-select>
                </el-form-item>
                <el-form-item label="调用时间">
                    <el-date-picker v-model="dateRange" type="datetimerange" range-separator="至"
                        start-placeholder="开始时间" end-placeholder="结束时间" value-format="YYYY-MM-DD HH:mm:ss"
                        @change="handleDateChange" style="width: 400px" />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="handleQuery">查询</el-button>
                    <el-button @click="handleReset">重置</el-button>
                </el-form-item>
            </el-form>
        </el-card>

        <el-card>
            <el-table v-loading="loading" :data="tableData" border>
                <el-table-column prop="merchantName" label="商户名称" min-width="150" show-overflow-tooltip />
                <el-table-column prop="apiName" label="API名称" min-width="150" show-overflow-tooltip />
                <el-table-column prop="requestTime" label="请求时间" width="180" />
                <el-table-column prop="responseTime" label="响应时间" width="180" />
                <el-table-column prop="duration" label="耗时(ms)" width="100" align="right" />
                <el-table-column prop="resCode" label="响应状态" width="100" align="center">
                    <template #default="{ row }">
                        <el-tag :type="row.resCode === '200' ? 'success' : 'danger'">
                            {{ row.resCode === '200' ? '成功' : '失败' }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="100" fixed="right">
                    <template #default="{ row }">
                        <el-button link type="primary" @click="handleView(row)">查看详情</el-button>
                    </template>
                </el-table-column>
            </el-table>

            <div class="pagination">
                <el-pagination v-model:current-page="queryParams.page" v-model:page-size="queryParams.size"
                    :page-sizes="[10, 20, 50]" :total="total" layout="total, sizes, prev, pager, next"
                    @size-change="handleSizeChange" @current-change="handleCurrentChange" />
            </div>
        </el-card>

        <!-- 日志详情对话框 -->
        <el-dialog title="日志详情" v-model="dialogVisible" width="800px">
            <el-descriptions :column="2" border>
                <el-descriptions-item label="商户名称">{{ currentLog.merchantName }}</el-descriptions-item>
                <el-descriptions-item label="API名称">{{ currentLog.apiName }}</el-descriptions-item>
                <el-descriptions-item label="请求时间">{{ currentLog.requestTime }}</el-descriptions-item>
                <el-descriptions-item label="响应时间">{{ currentLog.responseTime }}</el-descriptions-item>
                <el-descriptions-item label="耗时">{{ currentLog.duration }}ms</el-descriptions-item>
                <el-descriptions-item label="响应状态">
                    <el-tag :type="currentLog.resCode === '200' ? 'success' : 'danger'">
                        {{ currentLog.resCode === '200' ? '成功' : '失败' }}
                    </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="请求参数" :span="2">
                    <pre>{{ formatJson(currentLog.requestParams) }}</pre>
                </el-descriptions-item>
                <el-descriptions-item label="响应数据" :span="2">
                    <pre>{{ formatJson(currentLog.responseData) }}</pre>
                </el-descriptions-item>
                <el-descriptions-item v-if="currentLog.errorMsg" label="错误信息" :span="2">
                    {{ currentLog.errorMsg }}
                </el-descriptions-item>
            </el-descriptions>
        </el-dialog>
    </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { merchantLogApi } from '@/api/merchantLog'
import { merchantApi } from '@/api/merchant'
import { apiApi } from '@/api/api'
import type { MerchantLogDTO, MerchantLogQueryReqDTO } from '@/api/merchantLog'
import type { MerchantDTO } from '@/api/merchant'
import type { ApiDTO } from '@/api/api'

const loading = ref(false)
const dialogVisible = ref(false)
const tableData = ref<MerchantLogDTO[]>([])
const total = ref(0)
const merchantOptions = ref<MerchantDTO[]>([])
const apiOptions = ref<ApiDTO[]>([])
const dateRange = ref<[string, string]>()
const currentLog = ref<MerchantLogDTO>({} as MerchantLogDTO)

const queryParams = reactive<MerchantLogQueryReqDTO>({
    page: 1,
    size: 10,
    merchantId: undefined,
    apiId: undefined,
    resCode: undefined,
    startTime: undefined,
    endTime: undefined
})

// 加载商户列表
const loadMerchants = async () => {
    try {
        const res = await merchantApi.page({ page: 1, size: 1000 })
        if (res.code === 200) {
            merchantOptions.value = res.data.records
        }
    } catch (error) {
        console.error('加载商户列表失败:', error)
        ElMessage.error('加载商户列表失败')
    }
}

// 加载API列表
const loadApis = async () => {
    try {
        const res = await apiApi.page({ page: 1, size: 1000 })
        if (res.code === 200) {
            apiOptions.value = res.data.records
        }
    } catch (error) {
        console.error('加载API列表失败:', error)
        ElMessage.error('加载API列表失败')
    }
}

// 加载日志数据
const loadData = async () => {
    try {
        loading.value = true
        const res = await merchantLogApi.page(queryParams)
        if (res.code === 200) {
            tableData.value = res.data.records
            total.value = res.data.total
        }
    } finally {
        loading.value = false
    }
}

// 日期范围变化
const handleDateChange = (val: [string, string] | null) => {
    if (val) {
        queryParams.startTime = val[0]
        queryParams.endTime = val[1]
    } else {
        queryParams.startTime = undefined
        queryParams.endTime = undefined
    }
}

const handleQuery = () => {
    queryParams.page = 1
    loadData()
}

const handleReset = () => {
    queryParams.merchantId = undefined
    queryParams.apiId = undefined
    queryParams.resCode = undefined
    queryParams.startTime = undefined
    queryParams.endTime = undefined
    dateRange.value = undefined
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

const handleView = (row: MerchantLogDTO) => {
    currentLog.value = row
    dialogVisible.value = true
}

// 格式化JSON
const formatJson = (json: string | undefined) => {
    if (!json) return ''
    try {
        return JSON.stringify(JSON.parse(json), null, 2)
    } catch {
        return json
    }
}

onMounted(() => {
    loadMerchants()
    loadApis()
    loadData()
})
</script>

<style lang="scss" scoped>
.merchant-log-list {
    padding: 20px;

    .search-card {
        margin-bottom: 20px;
    }

    // 确保下拉选项的宽度与选择框一致
    :deep(.el-select__popper) {
        width: auto !important;
        min-width: 120px !important;
    }

    // 确保下拉选项文字不会被截断
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

    :deep(.el-descriptions__cell) {
        pre {
            margin: 0;
            white-space: pre-wrap;
            word-wrap: break-word;
        }
    }
}
</style>