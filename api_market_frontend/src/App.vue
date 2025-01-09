<template>
  <div id="app">
    <router-view v-slot="{ Component }">
      <keep-alive>
        <component :is="Component" />
      </keep-alive>
    </router-view>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'App',
  errorCaptured(err, vm, info) {
    console.error('Error captured in App.vue:', err)
    console.error('Error info:', info)
    return false // 阻止错误继续传播
  }
});
</script>

<style>
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  background-color: #f0f2f5;
}

#app {
  height: 100%;
}

/* 全局滚动条样式 */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* Element Plus 全局样式覆盖 */
.el-card {
  --el-card-padding: 16px;
  margin-bottom: 16px;
  border-radius: 4px;
}

.el-table {
  --el-table-header-bg-color: #fafafa;
  --el-table-border-color: #e8e8e8;
}

.el-table th {
  font-weight: 600;
  color: #1f2d3d;
  background: var(--el-table-header-bg-color);
}

.el-button {
  font-weight: 500;
}

.el-form-item__label {
  font-weight: 500;
}

.el-dialog {
  border-radius: 4px;
}

.el-dialog__header {
  margin: 0;
  padding: 16px 20px;
  border-bottom: 1px solid #f0f0f0;
}

.el-dialog__body {
  padding: 20px;
}

.el-dialog__footer {
  padding: 16px 20px;
  border-top: 1px solid #f0f0f0;
}

/* 通用样式类 */
.flex-center {
  display: flex;
  align-items: center;
  justify-content: center;
}

.flex-between {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.text-ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.hover-effect {
  transition: all 0.3s;
}

.hover-effect:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
}
</style>