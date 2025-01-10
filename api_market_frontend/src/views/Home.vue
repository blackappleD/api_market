<template>
    <el-container class="layout-container">
        <el-aside width="200px" class="menu-aside">
            <div class="logo">API Market</div>
            <el-menu :default-active="activeMenu" class="menu" :collapse="isCollapse" @select="handleSelect">
                <!-- 供应商管理 -->
                <el-sub-menu index="supplier">
                    <template #title>
                        <el-icon>
                            <Monitor />
                        </el-icon>
                        <span>供应商管理</span>
                    </template>
                    <el-menu-item index="/supplier">供应商信息管理</el-menu-item>
                    <el-menu-item index="/supplier-api">供应商Api管理</el-menu-item>
                </el-sub-menu>

                <!-- API管理 -->
                <el-sub-menu index="api">
                    <template #title>
                        <el-icon>
                            <Connection />
                        </el-icon>
                        <span>API管理</span>
                    </template>
                    <el-menu-item index="/api-category">API分类管理</el-menu-item>
                    <el-menu-item index="/api">API管理</el-menu-item>
                </el-sub-menu>

                <!-- 商户管理 -->
                <el-sub-menu index="merchant">
                    <template #title>
                        <el-icon>
                            <User />
                        </el-icon>
                        <span>商户管理</span>
                    </template>
                    <el-menu-item index="/merchant">商户信息管理</el-menu-item>
                    <el-menu-item index="/merchant-log">商户操作记录</el-menu-item>
                    <el-menu-item index="/merchant-stats">商户调用统计</el-menu-item>
                    <el-menu-item index="/merchant-api-log">商户调用日志</el-menu-item>
                </el-sub-menu>
            </el-menu>
        </el-aside>

        <el-container>
            <el-header class="layout-header">
                <div class="header-left">
                    <el-icon class="collapse-btn" @click="toggleCollapse">
                        <Fold v-if="!isCollapse" />
                        <Expand v-else />
                    </el-icon>
                </div>
                <div class="header-right">
                    <el-dropdown>
                        <span class="user-info">
                            {{ userStore.username }}
                            <el-icon>
                                <ArrowDown />
                            </el-icon>
                        </span>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </div>
            </el-header>

            <el-main>
                <router-view></router-view>
            </el-main>
        </el-container>
    </el-container>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/store/modules/user'
import {
    Monitor,
    Connection,
    User,
    Fold,
    Expand,
    ArrowDown
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const isCollapse = ref(false)
const activeMenu = computed(() => route.path)

const toggleCollapse = () => {
    isCollapse.value = !isCollapse.value
}

const handleSelect = (index: string) => {
    router.push(index)
}

const handleLogout = async () => {
    await userStore.logout()
    router.push('/login')
}
</script>

<style lang="scss" scoped>
.layout-container {
    height: 100vh;

    .menu-aside {
        background-color: #304156;
        transition: width 0.3s;

        .logo {
            height: 60px;
            line-height: 60px;
            text-align: center;
            color: #fff;
            font-size: 20px;
            font-weight: bold;
            border-bottom: 1px solid #1f2d3d;
        }

        .menu {
            border-right: none;
            background-color: transparent;
        }
    }

    .layout-header {
        background-color: #fff;
        border-bottom: 1px solid #dcdfe6;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 0 20px;

        .header-left {
            .collapse-btn {
                font-size: 20px;
                cursor: pointer;

                &:hover {
                    color: #409eff;
                }
            }
        }

        .header-right {
            .user-info {
                cursor: pointer;
                display: flex;
                align-items: center;
                gap: 4px;
            }
        }
    }

    :deep(.el-menu) {
        background-color: #304156;

        .el-sub-menu__title {
            color: #bfcbd9;

            &:hover {
                color: #fff;
                background-color: #263445 !important;
            }
        }

        .el-menu-item {
            color: #bfcbd9;
            background-color: #1f2d3d !important;

            &:hover {
                color: #fff;
                background-color: #001528 !important;
            }

            &.is-active {
                color: #409eff;
                background-color: #001528 !important;
            }
        }

        .el-sub-menu.is-opened {
            >.el-sub-menu__title {
                background-color: #263445 !important;
            }
        }
    }
}
</style>