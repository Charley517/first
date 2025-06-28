<template>
  <div class="create-order-container">
    <el-card class="form-card">
      <template #header>
        <div class="card-header">
          <h2>创建物流订单</h2>
        </div>
      </template>

      <el-form 
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="120px"
        class="order-form"
      >
        <el-tabs v-model="activeTab">
          <el-tab-pane label="寄件信息" name="sender">
            <el-form-item label="寄件人" prop="senderId">
              <el-select v-model="form.senderId" placeholder="请选择寄件人">
                <el-option v-for="user in senderList" :key="user.id" :label="user.username" :value="user.id" />
              </el-select>
            </el-form-item>
            <el-form-item label="收件人" prop="receiverId">
              <el-select v-model="form.receiverId" placeholder="请选择收件人">
                <el-option v-for="user in receiverList" :key="user.id" :label="user.username" :value="user.id" />
              </el-select>
            </el-form-item>
            <el-form-item label="快递员" prop="courierId">
              <el-select v-model="form.courierId" placeholder="请选择快递员">
                <el-option v-for="courier in courierList" :key="courier.id" :label="courier.name" :value="courier.id" />
              </el-select>
            </el-form-item>
          </el-tab-pane>

          <el-tab-pane label="包裹信息" name="package">
            <el-form-item label="包裹重量(kg)" prop="weight">
              <el-input-number v-model="form.weight" :min="0.1" :max="100" :precision="2" />
            </el-form-item>
            <el-form-item label="包裹类型" prop="packageType">
              <el-select v-model="form.packageType" placeholder="请选择包裹类型">
                <el-option label="文件" value="DOCUMENT" />
                <el-option label="小件" value="SMALL" />
                <el-option label="中件" value="MEDIUM" />
                <el-option label="大件" value="LARGE" />
              </el-select>
            </el-form-item>
            <el-form-item label="备注" prop="remark">
              <el-input 
                v-model="form.remark" 
                type="textarea" 
                placeholder="请输入备注信息"
                :rows="3"
              />
            </el-form-item>
          </el-tab-pane>
        </el-tabs>

        <div class="form-actions">
          <el-button @click="$router.push('/')">取消</el-button>
          <el-button type="primary" @click="submitForm">提交订单</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { userApi, courierApi, orderApi } from '@/api'
import type { UserInfo, Courier } from '@/types'

const formRef = ref<FormInstance>()
const activeTab = ref('sender')

const form = reactive({
  senderId: '',
  receiverId: '',
  courierId: '',
  weight: 1,
  packageType: '',
  remark: ''
})

const senderList = ref<UserInfo[]>([])
const receiverList = ref<UserInfo[]>([])
const courierList = ref<Courier[]>([])

onMounted(async () => {
  // 获取所有用户
  const users = await userApi.getUserList()
  senderList.value = users.filter(u => u.role === 'SENDER')
  receiverList.value = users.filter(u => u.role === 'RECEIVER')
  // 获取所有快递员
  courierList.value = await courierApi.getCourierList()
})

const rules = reactive<FormRules>({
  senderId: [
    { required: true, message: '请选择寄件人', trigger: 'change' }
  ],
  receiverId: [
    { required: true, message: '请选择收件人', trigger: 'change' }
  ],
  courierId: [
    { required: true, message: '请选择快递员', trigger: 'change' }
  ],
  weight: [
    { required: true, message: '请输入包裹重量', trigger: 'blur' }
  ],
  packageType: [
    { required: true, message: '请选择包裹类型', trigger: 'change' }
  ]
})

const submitForm = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await orderApi.createOrder(form)
        ElMessage.success('订单创建成功！')
        formRef.value?.resetFields()
        // 返回首页
        window.location.href = '/'
      } catch (error) {
        ElMessage.error('订单创建失败，请重试')
      }
    }
  })
}
</script>

<style scoped>
.create-order-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.form-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-form {
  margin-top: 20px;
}

.form-actions {
  margin-top: 20px;
  text-align: right;
}

:deep(.el-tabs__content) {
  padding: 20px 0;
}
</style> 