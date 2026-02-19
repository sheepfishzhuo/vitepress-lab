---
layout: doc
---

# 论文管理

<script setup>
import { ref, onMounted } from 'vue'
import AuthGuard from '../.vitepress/theme/components/AuthGuard.vue'

const API_URL = 'http://localhost:8080/api/papers'
const CONFIG_URL = 'http://localhost:8080/api/config'
const paperList = ref([])
const configOptions = ref({})
const loading = ref(true)
const showModal = ref(false)
const editingId = ref(null)
const form = ref({
  title: '',
  authors: '',
  venue: '',
  year: new Date().getFullYear(),
  type: '',
  doi: '',
  abstractText: '',
  keywords: '',
  pdf: '',
  citations: 0,
  impactFactor: null
})

const typeOptions = ref([])

async function fetchConfig() {
  try {
    const res = await fetch(CONFIG_URL)
    const data = await res.json()
    configOptions.value = data.data || {}
    typeOptions.value = (configOptions.value.paper_type || []).map(o => ({ value: o.optionValue, label: o.optionLabel }))
  } catch (e) {
    console.error('Failed to fetch config:', e)
    typeOptions.value = [
      { value: 'journal', label: '期刊' },
      { value: 'conference', label: '会议' },
      { value: 'workshop', label: '研讨会' },
      { value: 'preprint', label: '预印本' }
    ]
  }
}

async function fetchData() {
  loading.value = true
  try {
    const res = await fetch(API_URL)
    const data = await res.json()
    paperList.value = data.data || []
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
    authors: '',
    venue: '',
    year: new Date().getFullYear(),
    type: typeOptions.value[0]?.value || '',
    doi: '',
    abstractText: '',
    keywords: '',
    pdf: '',
    citations: 0,
    impactFactor: null
  }
  showModal.value = true
}

function openEditModal(item) {
  editingId.value = item.id
  form.value = {
    title: item.title || '',
    authors: item.authors || '',
    venue: item.venue || '',
    year: item.year || new Date().getFullYear(),
    type: item.type || 'journal',
    doi: item.doi || '',
    abstractText: item.abstractText || '',
    keywords: item.keywords || '',
    pdf: item.pdf || '',
    citations: item.citations || 0,
    impactFactor: item.impactFactor
  }
  showModal.value = true
}

async function saveItem() {
  if (!form.value.title.trim()) {
    alert('请输入论文标题')
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
  if (!confirm('确定要删除该论文吗？')) return
  
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
</script>

<AuthGuard password="icl2024">
<div class="toolbar">
  <button class="btn-primary" @click="openAddModal">+ 添加论文</button>
</div>

<div v-if="loading" class="loading">加载中...</div>

<div v-else class="data-table">
  <table>
    <thead>
      <tr>
        <th style="min-width: 200px;">标题</th>
        <th>作者</th>
        <th>期刊/会议</th>
        <th>年份</th>
        <th>类型</th>
        <th>引用数</th>
        <th>DOI</th>
        <th>操作</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="item in paperList" :key="item.id">
        <td><strong>{{ item.title }}</strong></td>
        <td>{{ item.authors }}</td>
        <td>{{ item.venue }}</td>
        <td>{{ item.year }}</td>
        <td>
          <span :class="['type-badge', item.type]">{{ getTypeLabel(item.type) }}</span>
        </td>
        <td>{{ item.citations || 0 }}</td>
        <td>
          <a v-if="item.doi" :href="'https://doi.org/' + item.doi" target="_blank" class="doi-link">链接</a>
          <span v-else>-</span>
        </td>
        <td class="actions">
          <button class="btn-small" @click="openEditModal(item)">编辑</button>
          <button class="btn-small btn-danger" @click="deleteItem(item.id)">删除</button>
        </td>
      </tr>
    </tbody>
  </table>
  
  <div v-if="paperList.length === 0" class="empty">暂无数据</div>
</div>

<div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
  <div class="modal">
    <h3>{{ editingId ? '编辑论文' : '添加论文' }}</h3>
    <div class="form-grid">
      <div class="form-item full">
        <label>论文标题 *</label>
        <input v-model="form.title" placeholder="请输入论文标题" />
      </div>
      <div class="form-item full">
        <label>作者 *</label>
        <input v-model="form.authors" placeholder="多个作者用逗号分隔" />
      </div>
      <div class="form-item">
        <label>期刊/会议 *</label>
        <input v-model="form.venue" placeholder="如：IEEE TPAMI, CVPR 2024" />
      </div>
      <div class="form-item">
        <label>年份</label>
        <input v-model.number="form.year" type="number" />
      </div>
      <div class="form-item">
        <label>类型</label>
        <select v-model="form.type">
          <option v-for="t in typeOptions" :key="t.value" :value="t.value">{{ t.label }}</option>
        </select>
      </div>
      <div class="form-item">
        <label>DOI</label>
        <input v-model="form.doi" placeholder="10.xxxx/xxxxx" />
      </div>
      <div class="form-item">
        <label>引用数</label>
        <input v-model.number="form.citations" type="number" />
      </div>
      <div class="form-item">
        <label>影响因子</label>
        <input v-model.number="form.impactFactor" type="number" step="0.001" placeholder="期刊填写" />
      </div>
      <div class="form-item full">
        <label>关键词</label>
        <input v-model="form.keywords" placeholder="多个关键词用逗号分隔" />
      </div>
      <div class="form-item full">
        <label>PDF链接</label>
        <input v-model="form.pdf" placeholder="PDF文件链接" />
      </div>
      <div class="form-item full">
        <label>摘要</label>
        <textarea v-model="form.abstractText" rows="4" placeholder="论文摘要"></textarea>
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

.type-badge {
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 0.8rem;
}

.type-badge.journal {
  background: #e8f5e9;
  color: #2e7d32;
}

.type-badge.conference {
  background: #e3f2fd;
  color: #1565c0;
}

.type-badge.workshop {
  background: #fff3e0;
  color: #ef6c00;
}

.type-badge.preprint {
  background: #f3e5f5;
  color: #7b1fa2;
}

.doi-link {
  color: var(--vp-c-brand-1);
  text-decoration: none;
}

.doi-link:hover {
  text-decoration: underline;
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
