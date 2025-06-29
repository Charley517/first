<template>
  <div class="courier-container">
    <el-card class="courier-card">
      <template #header>
        <div class="card-header">
          <h2>配送员管理</h2>
          <el-button type="primary" @click="showAddDialog">
            添加配送员
          </el-button>
        </div>
      </template>

      <div class="search-bar">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="姓名">
            <el-input v-model="searchForm.name" placeholder="请输入姓名" />
          </el-form-item>
          <el-form-item label="电话">
            <el-input v-model="searchForm.phone" placeholder="请输入电话" />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchForm.status" placeholder="请选择状态">
              <el-option label="全部" value="" />
              <el-option label="空闲" value="IDLE" />
              <el-option label="配送中" value="DELIVERING" />
              <el-option label="休息" value="REST" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table 
        :data="couriers" 
        style="width: 100%" 
        v-loading="loading"
        border
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="phone" label="电话" width="150" />
        <el-table-column prop="currentLocation" label="当前位置" min-width="200" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="performanceScore" label="绩效评分" width="100">
          <template #default="scope">
            <el-rate
              v-model="scope.row.performanceScore"
              disabled
              show-score
              text-color="#ff9900"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="200">
          <template #default="scope">
            <el-button 
              size="small" 
              type="primary" 
              @click="viewLocation(scope.row)"
            >
              查看位置
            </el-button>
            <el-button 
              size="small" 
              type="danger" 
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加配送员对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="添加配送员"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="当前位置" prop="currentLocation">
          <el-input v-model="form.currentLocation" placeholder="请输入当前位置" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAdd">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import axios from 'axios'
import { courierApi } from '@/api'

// 数据定义
const loading = ref(false)
const couriers = ref([])
const dialogVisible = ref(false)
const formRef = ref<FormInstance>()

// 搜索表单
const searchForm = reactive({
  name: '',
  phone: '',
  status: ''
})

// 添加表单
const form = reactive({
  name: '',
  phone: '',
  currentLocation: ''
})

// 表单验证规则
const rules = reactive<FormRules>({
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  currentLocation: [
    { required: true, message: '请输入当前位置', trigger: 'blur' }
  ]
})

// 加载配送员列表
const loadCouriers = async () => {
  loading.value = true
  try {
    couriers.value = await courierApi.getCourierList()
  } catch (error) {
    ElMessage.error('加载配送员列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  loadCouriers()
}

// 重置搜索
const resetSearch = () => {
  searchForm.name = ''
  searchForm.phone = ''
  searchForm.status = ''
  handleSearch()
}

// 显示添加对话框
const showAddDialog = () => {
  dialogVisible.value = true
  form.name = ''
  form.phone = ''
  form.currentLocation = ''
}

// 添加配送员
const handleAdd = async () => {
  try {
    console.log('即将发起请求', form)
    await courierApi.createCourier({
      name: form.name,
      phone: form.phone,
      currentLocation: form.currentLocation
    })
    ElMessage.success('添加配送员成功')
    dialogVisible.value = false
    loadCouriers()
  } catch (error) {
    ElMessage.error('添加配送员失败')
  }
}

// 删除配送员
const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除该配送员吗？',
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await axios.delete(`/api/couriers/${row.id}`)
    ElMessage.success('删除成功')
    loadCouriers()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 查看位置
const viewLocation = async (row: any) => {
  try {
    const courier = await courierApi.getCourierDetail(row.id)
    const location = courier.currentLocation || '未知'
    await ElMessageBox.alert(`当前位置：${location}`, '配送员位置', {
      confirmButtonText: '确定'
    })
  } catch (error) {
    ElMessage.error('获取配送员位置失败')
  }
}

// 获取状态类型
const getStatusType = (status: string) => {
  const types: Record<string, string> = {
    'IDLE': 'success',
    'DELIVERING': 'primary',
    'REST': 'info'
  }
  return types[status] || 'info'
}

// 获取状态文本
const getStatusText = (status: string) => {
  const texts: Record<string, string> = {
    'IDLE': '空闲',
    'DELIVERING': '配送中',
    'REST': '休息'
  }
  return texts[status] || status
}

onMounted(() => {
  loadCouriers()
})
</script>

<style scoped>
.courier-container {
  padding: 20px;
}

.courier-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-bar {
  margin-bottom: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style> 