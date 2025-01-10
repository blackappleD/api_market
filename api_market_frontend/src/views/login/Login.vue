<template>
    <div class="login-container">
        <div class="login-box">
            <div class="title">API Market</div>
            <el-form ref="formRef" :model="form" :rules="rules" class="login-form">
                <el-form-item prop="userAccount">
                    <el-input v-model="form.userAccount" placeholder="请输入用户名" prefix-icon="User" />
                </el-form-item>
                <el-form-item prop="password">
                    <el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="Lock"
                        show-password @keyup.enter="handleLogin" />
                </el-form-item>
                <el-form-item>
                    <el-button :loading="loading" type="primary" class="login-button" @click="handleLogin">
                        登录
                    </el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import type { FormInstance, FormRules } from 'element-plus'
import { useUserStore } from '@/store/modules/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const formRef = ref<FormInstance>()

const form = reactive({
    userAccount: '',
    password: ''
})

const rules: FormRules = {
    userAccount: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' }
    ]
}

const handleLogin = async () => {
    if (!formRef.value) return

    await formRef.value.validate(async (valid) => {
        if (valid) {
            try {
                loading.value = true
                await userStore.login(form.userAccount, form.password)
                router.push('/')
            } catch (error) {
                console.error('登录失败:', error)
                ElMessage.error('登录失败，请检查用户名和密码')
            } finally {
                loading.value = false
            }
        }
    })
}
</script>

<style lang="scss" scoped>
.login-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: #f5f5f5;

    .login-box {
        width: 400px;
        padding: 40px;
        background: #fff;
        border-radius: 4px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

        .title {
            margin-bottom: 30px;
            text-align: center;
            font-size: 24px;
            font-weight: bold;
            color: #409EFF;
        }

        .login-form {
            .login-button {
                width: 100%;
            }
        }
    }
}
</style>