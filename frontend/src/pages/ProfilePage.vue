<template>
  <el-card class="profile-card">
    <template #header>
      <div class="card-header">
        <h2>个人信息</h2>
      </div>
    </template>
    <el-form :model="form" label-width="80px" style="max-width: 400px;">
      <el-form-item label="用户名">
        <el-input v-model="form.username" disabled />
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" />
      </el-form-item>
      <el-form-item label="手机号">
        <el-input v-model="form.phone" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSave">保存</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { userApi } from '@/api'
import { ElMessage } from 'element-plus'

const form = ref({ username: '', email: '', phone: '' })

const loadUserInfo = async () => {
  const data = await userApi.getUserInfo()
  form.value = {
    username: data.username,
    email: data.email,
    phone: data.phone
  }
}
onMounted(loadUserInfo)

const onSave = async () => {
  await userApi.updateUserInfo(form.value)
  ElMessage.success('保存成功')
}
</script>

<style scoped>
.profile-card {
  max-width: 500px;
  margin: 40px auto;
}
</style> 