---
layout: doc
---

# 项目管理

<script setup>
import { ref, onMounted } from 'vue'
import AuthGuard from '../.vitepress/theme/components/AuthGuard.vue'

const API_URL = 'http://localhost:8080/api/projects'
const CONFIG_URL = 'http://localhost:8080/api/config'
const projectList = ref([])
const configOptions = ref({})
const loading = ref(true)
const showModal = ref(false)
const editingId = ref(null)
const form = ref({
  title: '',
  principalInvestigator: '',
  members: '',
  funding: '',
  amount: '',
  startDate: '',
  endDate: '',
  status: '',
  description: '',
  outcomes: ''
})

const statusOptions = ref([])

async function fetchConfig() {
  try {
    const res = await fetch(CONFIG_URL)
    const data = await res.json()
    configOptions.value = data.data || {}
    statusOptions.value = (configOptions.value.project_status || []).map(o => ({ value: o.optionValue, label: o.optionLabel }))
  } catch (e) {
    console.error('Failed to fetch config:', e)
    statusOptions.value = [
      { value: 'ongoing', label: '进行中' },
      { value: 'completed', label: '已完成' }
    ]
  }
}

async function fetchData() {
  loading.value = true
  try {
    const res = await fetch(API_URL)
    const data = await res.json()
    projectList.value = data.data || []
  } catch (e) {
    console.error('Failed to fetch:', e)
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  await fetchConfig()
  await fetchData()
})

function openAddModal() {
  editingId.value = null
  form.value = {
    title: '',
    principalInvestigator: '',
    members: '',
    funding: '',
    amount: '',
    startDate: '',
    endDate: '',
    status: statusOptions.value[0]?.value || '',
    description: '',
    outcomes: ''
  }
  showModal.value = true
}

function openEditModal(item) {
  editingId.value = item.id
  form.value = {
    title: item.title || '',
    principalInvestigator: item.principalInvestigator || '',
    members: item.members || '',
    funding: item.funding || '',
    amount: item.amount || '',
    startDate: item.startDate || '',
    endDate: item.endDate || '',
    status: item.status || 'ongoing',
    description: item.description || '',
    outcomes: item.outcomes || ''
  }
  showModal.value = true
}

async function saveItem() {
  if (!form.value.title.trim()) {
    alert('请输入项目名称')
    return
  }
  
  const url = editingId.value ? `${API_URL}/${editingId.value}` : API_URL
  const method = editingId.value ? 'PUT' : 'POST'
  
  try {
    const res = await fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form.value)
    })
    const data = await res.json()
    if (data.code === 200) {
      showModal.value = false
      fetchData()
    } else {
      alert(data.message || '保存失败')
    }
  } catch (e) {
    alert('保存失败')
    console.error(e)
  }
}

async function deleteItem(id) {
  if (!confirm('确定要删除该项目吗？')) return
  
  try {
    const res = await fetch(`${API_URL}/${id}`, { method: 'DELETE' })
    const data = await res.json()
    if (data.code === 200) {
      fetchData()
    } else {
      alert(data.message || '删除失败')
    }
  } catch (e) {
    alert('删除失败')
    console.error(e)
  }
}

function getStatusLabel(val) {
  return statusOptions.value.find(s => s.value === val)?.label || val
}
</script>

<AuthGuard password="icl2024">
<div class="toolbar">
  <button class="btn-primary" @click="openAddModal">+ 添加项目</button>
</div>

<div v-if="loading" class="loading">加载中...</div>

<div v-else class="data-table">
  <table>
    <thead>
      <tr>
        <th style="min-width: 200px;">项目名称</th>
        <th>负责人</th>
        <th>资助来源</th>
        <th>金额</th>
        <th>起止时间</th>
        <th>状态</th>
        <th>操作</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="item in projectList" :key="item.id">
        <td><strong>{{ item.title }}</strong></td>
        <td>{{ item.principalInvestigator || '-' }}</td>
        <td>{{ item.funding || '-' }}</td>
        <td>{{ item.amount || '-' }}</td>
        <td>{{ item.startDate }} ~ {{ item.endDate || '至今' }}</td>
        <td>
          <span :class="['status-badge', item.status]">{{ getStatusLabel(item.status) }}</span>
        </td>
        <td class="actions">
          <button class="btn-small" @click="openEditModal(item)">编辑</button>
          <button class="btn-small btn-danger" @click="deleteItem(item.id)">删除</button>
        </td>
      </tr>
    </tbody>
  </table>
  
  <div v-if="projectList.length === 0" class="empty">暂无数据</div>
</div>

<div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
  <div class="modal">
    <h3>{{ editingId ? '编辑项目' : '添加项目' }}</h3>
    <div class="form-grid">
      <div class="form-item full">
        <label>项目名称 *</label>
        <input v-model="form.title" placeholder="请输入项目名称" />
      </div>
      <div class="form-item">
        <label>负责人</label>
        <input v-model="form.principalInvestigator" placeholder="项目负责人" />
      </div>
      <div class="form-item">
        <label>参与人员</label>
        <input v-model="form.members" placeholder="多人用逗号分隔" />
      </div>
      <div class="form-item">
        <label>资助来源</label>
        <input v-model="form.funding" placeholder="如：国家自然科学基金" />
      </div>
      <div class="form-item">
        <label>项目金额</label>
        <input v-model="form.amount" placeholder="如：50万元" />
      </div>
      <div class="form-item">
        <label>开始日期</label>
        <input v-model="form.startDate" placeholder="如：2024-01" />
      </div>
      <div class="form-item">
        <label>结束日期</label>
        <input v-model="form.endDate" placeholder="如：2026-12" />
      </div>
      <div class="form-item">
        <label>状态</label>
        <select v-model="form.status">
          <option v-for="s in statusOptions" :key="s.value" :value="s.value">{{ s.label }}</option>
        </select>
      </div>
      <div class="form-item full">
        <label>项目描述</label>
        <textarea v-model="form.description" rows="3" placeholder="项目简介"></textarea>
      </div>
      <div class="form-item full">
        <label>研究成果</label>
        <textarea v-model="form.outcomes" rows="2" placeholder="多项成果用逗号分隔"></textarea>
      </div>
    </div>
    <div class="modal-actions">
      <button class="btn-secondary" @click="showModal = false">取消</button>
      <button class="btn-primary" @click="saveItem">保存</button>
    </div>
  </div>
</div>
</AuthGuard>

<style scoped>
.toolbar {
  margin-bottom: 1rem;
  display: flex;
  justify-content: flex-end;
}

.loading {
  text-align: center;
  padding: 3rem;
  color: var(--vp-c-text-2);
}

.data-table {
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
  background: var(--vp-c-bg);
  border-radius: 8px;
  overflow: hidden;
}

th, td {
  padding: 0.75rem 1rem;
  text-align: left;
  border-bottom: 1px solid var(--vp-c-divider);
}

th {
  background: var(--vp-c-bg-soft);
  font-weight: 600;
  color: var(--vp-c-text-1);
}

td {
  color: var(--vp-c-text-2);
}

tr:hover td {
  background: var(--vp-c-bg-soft);
}

.status-badge {
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 0.8rem;
}

.status-badge.ongoing {
  background: #e3f2fd;
  color: #1565c0;
}

.status-badge.completed {
  background: #e8f5e9;
  color: #2e7d32;
}

.actions {
  display: flex;
  gap: 0.5rem;
}

.empty {
  text-align: center;
  padding: 3rem;
  color: var(--vp-c-text-2);
}

.btn-primary {
  padding: 0.5rem 1rem;
  background: var(--vp-c-brand-1);
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.9rem;
}

.btn-secondary {
  padding: 0.5rem 1rem;
  background: var(--vp-c-bg-soft);
  color: var(--vp-c-text-1);
  border: 1px solid var(--vp-c-divider);
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.9rem;
}

.btn-small {
  padding: 0.25rem 0.5rem;
  background: var(--vp-c-brand-soft);
  color: var(--vp-c-brand-1);
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.8rem;
}

.btn-danger {
  background: #fee2e2;
  color: #dc2626;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal {
  background: var(--vp-c-bg);
  border-radius: 12px;
  padding: 1.5rem;
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal h3 {
  margin: 0 0 1rem;
  color: var(--vp-c-text-1);
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.form-item {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.form-item.full {
  grid-column: 1 / -1;
}

.form-item label {
  font-size: 0.85rem;
  color: var(--vp-c-text-2);
}

.form-item input,
.form-item select,
.form-item textarea {
  padding: 0.5rem;
  border: 1px solid var(--vp-c-divider);
  border-radius: 6px;
  background: var(--vp-c-bg);
  color: var(--vp-c-text-1);
  font-size: 0.9rem;
}

.form-item input:focus,
.form-item select:focus,
.form-item textarea:focus {
  outline: none;
  border-color: var(--vp-c-brand-1);
}

.modal-actions {
  margin-top: 1.5rem;
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
}
</style>
