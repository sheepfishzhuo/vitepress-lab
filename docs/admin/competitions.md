---
layout: doc
---

# 竞赛管理

<script setup>
import { ref, onMounted } from 'vue'
import AuthGuard from '../.vitepress/theme/components/AuthGuard.vue'

const API_URL = 'http://localhost:8080/api/competitions'
const CONFIG_URL = 'http://localhost:8080/api/config'
const competitionList = ref([])
const configOptions = ref({})
const loading = ref(true)
const showModal = ref(false)
const editingId = ref(null)
const form = ref({
  name: '',
  level: '',
  award: '',
  year: new Date().getFullYear(),
  participants: '',
  advisors: '',
  description: ''
})

const levelOptions = ref([])

async function fetchConfig() {
  try {
    const res = await fetch(CONFIG_URL)
    const data = await res.json()
    configOptions.value = data.data || {}
    levelOptions.value = (configOptions.value.competition_level || []).map(o => ({ value: o.optionValue, label: o.optionLabel }))
  } catch (e) {
    console.error('Failed to fetch config:', e)
    levelOptions.value = [
      { value: 'international', label: '国际级' },
      { value: 'national', label: '国家级' },
      { value: 'provincial', label: '省级' }
    ]
  }
}

async function fetchData() {
  loading.value = true
  try {
    const res = await fetch(API_URL)
    const data = await res.json()
    competitionList.value = data.data || []
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
    name: '',
    level: levelOptions.value[0]?.value || '',
    award: '',
    year: new Date().getFullYear(),
    participants: '',
    advisors: '',
    description: ''
  }
  showModal.value = true
}

function openEditModal(item) {
  editingId.value = item.id
  form.value = {
    name: item.name || '',
    level: item.level || 'national',
    award: item.award || '',
    year: item.year || new Date().getFullYear(),
    participants: item.participants || '',
    advisors: item.advisors || '',
    description: item.description || ''
  }
  showModal.value = true
}

async function saveItem() {
  if (!form.value.name.trim()) {
    alert('请输入竞赛名称')
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
  if (!confirm('确定要删除该竞赛记录吗？')) return
  
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

function getLevelLabel(val) {
  return levelOptions.value.find(l => l.value === val)?.label || val
}
</script>
docs/admin/*.md
<AuthGuard password="icl2024">
<div class="toolbar">
  <button class="btn-primary" @click="openAddModal">+ 添加竞赛</button>
</div>

<div v-if="loading" class="loading">加载中...</div>

<div v-else class="data-table">
  <table>
    <thead>
      <tr>
        <th>竞赛名称</th>
        <th>级别</th>
        <th>获奖等级</th>
        <th>年份</th>
        <th>参赛学生</th>
        <th>操作</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="item in competitionList" :key="item.id">
        <td><strong>{{ item.name }}</strong></td>
        <td>
          <span :class="['level-badge', item.level]">{{ getLevelLabel(item.level) }}</span>
        </td>
        <td>{{ item.award }}</td>
        <td>{{ item.year }}</td>
        <td>{{ item.participants || '-' }}</td>
        <td class="actions">
          <button class="btn-small" @click="openEditModal(item)">编辑</button>
          <button class="btn-small btn-danger" @click="deleteItem(item.id)">删除</button>
        </td>
      </tr>
    </tbody>
  </table>
  
  <div v-if="competitionList.length === 0" class="empty">暂无数据</div>
</div>

<div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
  <div class="modal">
    <h3>{{ editingId ? '编辑竞赛' : '添加竞赛' }}</h3>
    <div class="form-grid">
      <div class="form-item full">
        <label>竞赛名称 *</label>
        <input v-model="form.name" placeholder="请输入竞赛名称" />
      </div>
      <div class="form-item">
        <label>级别</label>
        <select v-model="form.level">
          <option v-for="l in levelOptions" :key="l.value" :value="l.value">{{ l.label }}</option>
        </select>
      </div>
      <div class="form-item">
        <label>获奖等级</label>
        <input v-model="form.award" placeholder="如：一等奖" />
      </div>
      <div class="form-item">
        <label>年份</label>
        <input v-model.number="form.year" type="number" />
      </div>
      <div class="form-item">
        <label>参赛学生</label>
        <input v-model="form.participants" placeholder="多人用逗号分隔" />
      </div>
      <div class="form-item full">
        <label>指导教师</label>
        <input v-model="form.advisors" placeholder="多人用逗号分隔" />
      </div>
      <div class="form-item full">
        <label>描述</label>
        <textarea v-model="form.description" rows="3" placeholder="竞赛描述"></textarea>
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
.level-badge { padding: 2px 8px; border-radius: 4px; font-size: 0.8rem; }
.level-badge.international { background: #fce4ec; color: #c2185b; }
.level-badge.national { background: #e3f2fd; color: #1565c0; }
.level-badge.provincial { background: #e8f5e9; color: #2e7d32; }
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
