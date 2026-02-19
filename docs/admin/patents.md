---
layout: doc
---

# 专利软著管理

<script setup>
import { ref, onMounted } from 'vue'
import AuthGuard from '../.vitepress/theme/components/AuthGuard.vue'

const API_URL = 'http://localhost:8080/api/patents'
const CONFIG_URL = 'http://localhost:8080/api/config'
const patentList = ref([])
const configOptions = ref({})
const loading = ref(true)
const showModal = ref(false)
const editingId = ref(null)
const form = ref({
  title: '',
  type: '',
  inventors: '',
  patentNo: '',
  status: '',
  date: '',
  description: ''
})

const typeOptions = ref([])
const statusOptions = ref([])

async function fetchConfig() {
  try {
    const res = await fetch(CONFIG_URL)
    const data = await res.json()
    configOptions.value = data.data || {}
    typeOptions.value = (configOptions.value.patent_type || []).map(o => ({ value: o.optionValue, label: o.optionLabel }))
    statusOptions.value = (configOptions.value.patent_status || []).map(o => ({ value: o.optionValue, label: o.optionLabel }))
  } catch (e) {
    console.error('Failed to fetch config:', e)
    typeOptions.value = [
      { value: 'invention', label: '发明专利' },
      { value: 'utility', label: '实用新型' },
      { value: 'software', label: '软件著作权' }
    ]
    statusOptions.value = [
      { value: 'pending', label: '申请中' },
      { value: 'granted', label: '已授权' }
    ]
  }
}

async function fetchData() {
  loading.value = true
  try {
    const res = await fetch(API_URL)
    const data = await res.json()
    patentList.value = data.data || []
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
    type: typeOptions.value[0]?.value || '',
    inventors: '',
    patentNo: '',
    status: statusOptions.value[0]?.value || '',
    date: '',
    description: ''
  }
  showModal.value = true
}

function openEditModal(item) {
  editingId.value = item.id
  form.value = {
    title: item.title || '',
    type: item.type || 'invention',
    inventors: item.inventors || '',
    patentNo: item.patentNo || '',
    status: item.status || 'pending',
    date: item.date || '',
    description: item.description || ''
  }
  showModal.value = true
}

async function saveItem() {
  if (!form.value.title.trim()) {
    alert('请输入专利名称')
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
  if (!confirm('确定要删除该专利记录吗？')) return
  
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

function getTypeLabel(val) {
  return typeOptions.value.find(t => t.value === val)?.label || val
}

function getStatusLabel(val) {
  return statusOptions.value.find(s => s.value === val)?.label || val
}
</script>

<AuthGuard password="icl2024">
<div class="toolbar">
  <button class="btn-primary" @click="openAddModal">+ 添加专利</button>
</div>

<div v-if="loading" class="loading">加载中...</div>

<div v-else class="data-table">
  <table>
    <thead>
      <tr>
        <th>名称</th>
        <th>类型</th>
        <th>发明人</th>
        <th>专利号</th>
        <th>状态</th>
        <th>日期</th>
        <th>操作</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="item in patentList" :key="item.id">
        <td><strong>{{ item.title }}</strong></td>
        <td>
          <span :class="['type-badge', item.type]">{{ getTypeLabel(item.type) }}</span>
        </td>
        <td>{{ item.inventors || '-' }}</td>
        <td>{{ item.patentNo || '-' }}</td>
        <td>
          <span :class="['status-badge', item.status]">{{ getStatusLabel(item.status) }}</span>
        </td>
        <td>{{ item.date || '-' }}</td>
        <td class="actions">
          <button class="btn-small" @click="openEditModal(item)">编辑</button>
          <button class="btn-small btn-danger" @click="deleteItem(item.id)">删除</button>
        </td>
      </tr>
    </tbody>
  </table>
  
  <div v-if="patentList.length === 0" class="empty">暂无数据</div>
</div>

<div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
  <div class="modal">
    <h3>{{ editingId ? '编辑专利' : '添加专利' }}</h3>
    <div class="form-grid">
      <div class="form-item full">
        <label>专利名称 *</label>
        <input v-model="form.title" placeholder="请输入专利名称" />
      </div>
      <div class="form-item">
        <label>类型</label>
        <select v-model="form.type">
          <option v-for="t in typeOptions" :key="t.value" :value="t.value">{{ t.label }}</option>
        </select>
      </div>
      <div class="form-item">
        <label>状态</label>
        <select v-model="form.status">
          <option v-for="s in statusOptions" :key="s.value" :value="s.value">{{ s.label }}</option>
        </select>
      </div>
      <div class="form-item full">
        <label>发明人</label>
        <input v-model="form.inventors" placeholder="多人用逗号分隔" />
      </div>
      <div class="form-item">
        <label>专利号</label>
        <input v-model="form.patentNo" placeholder="专利号/登记号" />
      </div>
      <div class="form-item">
        <label>日期</label>
        <input v-model="form.date" placeholder="授权/登记日期" />
      </div>
      <div class="form-item full">
        <label>描述</label>
        <textarea v-model="form.description" rows="3" placeholder="专利描述"></textarea>
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
.toolbar { margin-bottom: 1rem; display: flex; justify-content: flex-end; }
.loading { text-align: center; padding: 3rem; color: var(--vp-c-text-2); }
.data-table { overflow-x: auto; }
table { width: 100%; border-collapse: collapse; background: var(--vp-c-bg); border-radius: 8px; overflow: hidden; }
th, td { padding: 0.75rem 1rem; text-align: left; border-bottom: 1px solid var(--vp-c-divider); }
th { background: var(--vp-c-bg-soft); font-weight: 600; color: var(--vp-c-text-1); }
td { color: var(--vp-c-text-2); }
tr:hover td { background: var(--vp-c-bg-soft); }
.type-badge { padding: 2px 8px; border-radius: 4px; font-size: 0.8rem; }
.type-badge.invention { background: #e8f5e9; color: #2e7d32; }
.type-badge.utility { background: #e3f2fd; color: #1565c0; }
.type-badge.software { background: #fff3e0; color: #ef6c00; }
.status-badge { padding: 2px 8px; border-radius: 4px; font-size: 0.8rem; }
.status-badge.pending { background: #fff3e0; color: #ef6c00; }
.status-badge.granted { background: #e8f5e9; color: #2e7d32; }
.actions { display: flex; gap: 0.5rem; }
.empty { text-align: center; padding: 3rem; color: var(--vp-c-text-2); }
.btn-primary { padding: 0.5rem 1rem; background: var(--vp-c-brand-1); color: white; border: none; border-radius: 6px; cursor: pointer; font-size: 0.9rem; }
.btn-secondary { padding: 0.5rem 1rem; background: var(--vp-c-bg-soft); color: var(--vp-c-text-1); border: 1px solid var(--vp-c-divider); border-radius: 6px; cursor: pointer; font-size: 0.9rem; }
.btn-small { padding: 0.25rem 0.5rem; background: var(--vp-c-brand-soft); color: var(--vp-c-brand-1); border: none; border-radius: 4px; cursor: pointer; font-size: 0.8rem; }
.btn-danger { background: #fee2e2; color: #dc2626; }
.modal-overlay { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0, 0, 0, 0.5); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal { background: var(--vp-c-bg); border-radius: 12px; padding: 1.5rem; width: 90%; max-width: 500px; max-height: 90vh; overflow-y: auto; }
.modal h3 { margin: 0 0 1rem; color: var(--vp-c-text-1); }
.form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
.form-item { display: flex; flex-direction: column; gap: 0.25rem; }
.form-item.full { grid-column: 1 / -1; }
.form-item label { font-size: 0.85rem; color: var(--vp-c-text-2); }
.form-item input, .form-item select, .form-item textarea { padding: 0.5rem; border: 1px solid var(--vp-c-divider); border-radius: 6px; background: var(--vp-c-bg); color: var(--vp-c-text-1); font-size: 0.9rem; }
.form-item input:focus, .form-item select:focus, .form-item textarea:focus { outline: none; border-color: var(--vp-c-brand-1); }
.modal-actions { margin-top: 1.5rem; display: flex; justify-content: flex-end; gap: 0.75rem; }
</style>
