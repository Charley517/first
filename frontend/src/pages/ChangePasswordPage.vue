<template>
  <el-card class="change-password-card">
    <template #header>
      <div class="card-header">
        <h2>修改密码</h2>
      </div>
    </template>
    <el-form :model="form" label-width="80px" style="max-width: 400px;">
      <el-form-item label="旧密码">
        <el-input v-model="form.oldPassword" type="password" />
      </el-form-item>
      <el-form-item label="新密码">
        <el-input v-model="form.newPassword" type="password" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onChangePassword">修改</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { userApi } from '@/api'
import { ElMessage } from 'element-plus'

const form = ref({ oldPassword: '', newPassword: '' })

const onChangePassword = async () => {
  if (!form.value.oldPassword || !form.value.newPassword) {
    ElMessage.error('请填写完整')
    return
  }
  await userApi.changePassword(form.value)
  ElMessage.success('密码修改成功')
  form.value.oldPassword = ''
  form.value.newPassword = ''
}
</script>

<style scoped>
.change-password-card {
  max-width: 500px;
  margin: 40px auto;
}
</style> 