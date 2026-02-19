---
layout: doc
---

# 教师管理

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import RichTextEditor from '../.vitepress/theme/components/RichTextEditor.vue'
import AuthGuard from '../.vitepress/theme/components/AuthGuard.vue'

const API_URL = 'http://localhost:8080/api/faculty'
const CONFIG_URL = 'http://localhost:8080/api/config'
const facultyList = ref([])
const configOptions = ref({})
const loading = ref(true)
const showModal = ref(false)
const editingId = ref(null)
const editorReady = ref(false)
const form = ref({
  name: '',
  title: '',
  role: '',
  avatar: '',
  email: '',
  phone: '',
  office: '',
  researchInterests: '',
  education: '',
  biography: '',
  googleScholar: '',
  researchGate: '',
  orcid: '',
  personal: '',
  github: ''
})

const titleOptions = ref([])

async function fetchConfig() {
  try {
    const res = await fetch(CONFIG_URL)
    const data = await res.json()
    configOptions.value = data.data || {}
    titleOptions.value = (configOptions.value.faculty_title || []).map(o => o.optionLabel)
  } catch (e) {
    console.error('Failed to fetch config:', e)
    titleOptions.value = ['教授', '副教授', '讲师', '研究员', '副研究员']
  }
}

async function fetchData() {
  loading.value = true
  try {
    const res = await fetch(API_URL)
    const data = await res.json()
    facultyList.value = data.data || []
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
    title: titleOptions.value[0] || '',
    role: '',
    avatar: '',
    email: '',
    phone: '',
    office: '',
    researchInterests: '',
    education: '',
    biography: '',
    googleScholar: '',
    researchGate: '',
    orcid: '',
    personal: '',
    github: ''
  }
  editorReady.value = false
  showModal.value = true
  nextTick(() => {
    editorReady.value = true
  })
}

async function openEditModal(item) {
  editingId.value = item.id
  form.value = {
    name: item.name || '',
    title: item.title || '讲师',
    role: item.role || '',
    avatar: item.avatar || '',
    email: item.email || '',
    phone: item.phone || '',
    office: item.office || '',
    researchInterests: item.researchInterests || '',
    education: item.education || '',
    biography: item.biography || '',
    googleScholar: item.googleScholar || '',
    researchGate: item.researchGate || '',
    orcid: item.orcid || '',
    personal: item.personal || '',
    github: item.github || ''
  }
  editorReady.value = false
  showModal.value = true
  await nextTick()
  editorReady.value = true
}

async function saveItem() {
  if (!form.value.name.trim()) {
    alert('请输入姓名')
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
  if (!confirm('确定要删除该教师吗？')) return
  
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
</script>

<AuthGuard password="icl2024">
<div class="toolbar">
  <button class="btn-primary" @click="openAddModal">+ 添加教师</button>
</div>

<div v-if="loading" class="loading">加载中...</div>

<div v-else class="data-table">
  <table>
    <thead>
      <tr>
        <th>姓名</th>
        <th>职称</th>
        <th>职务</th>
        <th>邮箱</th>
        <th>研究方向</th>
        <th>操作</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="item in facultyList" :key="item.id">
        <td><strong>{{ item.name }}</strong></td>
        <td>{{ item.title }}</td>
        <td>{{ item.role || '-' }}</td>
        <td>{{ item.email || '-' }}</td>
        <td>{{ item.researchInterests || '-' }}</td>
        <td class="actions">
          <button class="btn-small" @click="openEditModal(item)">编辑</button>
          <button class="btn-small btn-danger" @click="deleteItem(item.id)">删除</button>
        </td>
      </tr>
    </tbody>
  </table>
  
  <div v-if="facultyList.length === 0" class="empty">暂无数据</div>
</div>

<div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
  <div class="modal modal-large">
    <h3>{{ editingId ? '编辑教师' : '添加教师' }}</h3>
    <div class="form-grid">
      <div class="form-item">
        <label>姓名 *</label>
        <input v-model="form.name" placeholder="请输入姓名" />
      </div>
      <div class="form-item">
        <label>职称 *</label>
        <select v-model="form.title">
          <option v-for="t in titleOptions" :key="t" :value="t">{{ t }}</option>
        </select>
      </div>
      <div class="form-item">
        <label>头像URL</label>
        <input v-model="form.avatar" placeholder="头像图片地址" />
      </div>
      <div class="form-item">
        <label>职务</label>
        <input v-model="form.role" placeholder="如：实验室主任" />
      </div>
      <div class="form-item">
        <label>邮箱</label>
        <input v-model="form.email" type="email" placeholder="email@example.com" />
      </div>
      <div class="form-item">
        <label>电话</label>
        <input v-model="form.phone" placeholder="联系电话" />
      </div>
      <div class="form-item">
        <label>办公室</label>
        <input v-model="form.office" placeholder="如：计算机楼 A301" />
      </div>
      <div class="form-item full">
        <label>研究方向</label>
        <input v-model="form.researchInterests" placeholder="多个方向用逗号分隔" />
      </div>
      <div class="form-item full">
        <label>教育经历</label>
        <RichTextEditor v-if="editorReady" v-model="form.education" />
      </div>
      <div class="form-item full">
        <label>个人简介</label>
        <RichTextEditor v-if="editorReady" v-model="form.biography" />
      </div>
      <div class="form-item">
        <label>Google Scholar</label>
        <input v-model="form.googleScholar" placeholder="Google Scholar主页链接" />
      </div>
      <div class="form-item">
        <label>ResearchGate</label>
        <input v-model="form.researchGate" placeholder="ResearchGate主页链接" />
      </div>
      <div class="form-item">
        <label>ORCID</label>
        <input v-model="form.orcid" placeholder="ORCID主页链接" />
      </div>
      <div class="form-item">
        <label>个人主页</label>
        <input v-model="form.personal" placeholder="个人主页链接" />
      </div>
      <div class="form-item">
        <label>GitHub</label>
        <input v-model="form.github" placeholder="GitHub主页链接" />
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

.btn-primary:hover {
  opacity: 0.9;
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
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.modal-large {
  max-width: 700px;
}

.modal h3 {
  margin: 0 0 1rem;
  color: var(--vp-c-text-1);
  flex-shrink: 0;
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  overflow-y: auto;
  flex: 1;
  padding-right: 0.5rem;
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
  font-family: inherit;
}

.form-item textarea {
  resize: vertical;
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
  flex-shrink: 0;
  padding-top: 1rem;
  border-top: 1px solid var(--vp-c-divider);
}
</style>
