---
layout: doc
---

# 新闻管理

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import RichTextEditor from '../.vitepress/theme/components/RichTextEditor.vue'
import ImageUpload from '../.vitepress/theme/components/ImageUpload.vue'
import AuthGuard from '../.vitepress/theme/components/AuthGuard.vue'

const API_URL = 'http://localhost:8080/api/news'
const CONFIG_URL = 'http://localhost:8080/api/config'
const newsList = ref([])
const configOptions = ref({})
const loading = ref(true)
const showModal = ref(false)
const editingId = ref(null)
const editorReady = ref(false)
const form = ref({
  title: '',
  date: '',
  category: '',
  summary: '',
  content: '',
  image: '',
  author: ''
})

const categoryOptions = ref([])

async function fetchConfig() {
  try {
    const res = await fetch(CONFIG_URL)
    const data = await res.json()
    configOptions.value = data.data || {}
    categoryOptions.value = (configOptions.value.news_category || []).map(o => ({ value: o.optionValue, label: o.optionLabel }))
  } catch (e) {
    console.error('Failed to fetch config:', e)
    categoryOptions.value = [
      { value: 'research', label: '科研成果' },
      { value: 'award', label: '获奖喜报' },
      { value: 'event', label: '学术活动' },
      { value: 'announcement', label: '通知公告' }
    ]
  }
}

async function fetchData() {
  loading.value = true
  try {
    const res = await fetch(API_URL)
    const data = await res.json()
    newsList.value = data.data || []
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
    date: new Date().toISOString().split('T')[0],
    category: categoryOptions.value[0]?.value || '',
    summary: '',
    content: '',
    image: '',
    author: ''
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
    title: item.title || '',
    date: item.date || '',
    category: item.category || 'announcement',
    summary: item.summary || '',
    content: item.content || '',
    image: item.image || '',
    author: item.author || ''
  }
  editorReady.value = false
  showModal.value = true
  await nextTick()
  editorReady.value = true
}

async function saveItem() {
  if (!form.value.title.trim()) {
    alert('请输入新闻标题')
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
  if (!confirm('确定要删除该新闻吗？')) return
  
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

function getCategoryLabel(val) {
  return categoryOptions.value.find(c => c.value === val)?.label || val
}

function truncate(str, len) {
  if (!str) return '-'
  return str.length > len ? str.slice(0, len) + '...' : str
}
</script>

<AuthGuard password="icl2024">
<div class="toolbar">
  <button class="btn-primary" @click="openAddModal">+ 添加新闻</button>
</div>

<div v-if="loading" class="loading">加载中...</div>

<div v-else class="data-table">
  <table>
    <thead>
      <tr>
        <th>标题</th>
        <th>分类</th>
        <th>日期</th>
        <th>作者</th>
        <th>摘要</th>
        <th>操作</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="item in newsList" :key="item.id">
        <td><strong>{{ truncate(item.title, 20) }}</strong></td>
        <td>
          <span :class="['category-badge', item.category]">{{ getCategoryLabel(item.category) }}</span>
        </td>
        <td>{{ item.date }}</td>
        <td>{{ item.author || '-' }}</td>
        <td>{{ truncate(item.summary, 10) }}</td>
        <td class="actions">
          <button class="btn-small" @click="openEditModal(item)">编辑</button>
          <button class="btn-small btn-danger" @click="deleteItem(item.id)">删除</button>
        </td>
      </tr>
    </tbody>
  </table>
  
  <div v-if="newsList.length === 0" class="empty">暂无数据</div>
</div>

<div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
  <div class="modal modal-large">
    <h3>{{ editingId ? '编辑新闻' : '添加新闻' }}</h3>
    <div class="form-grid">
      <div class="form-item full">
        <label>新闻标题 *</label>
        <input v-model="form.title" placeholder="请输入新闻标题" />
      </div>
      <div class="form-item">
        <label>分类</label>
        <select v-model="form.category">
          <option v-for="c in categoryOptions" :key="c.value" :value="c.value">{{ c.label }}</option>
        </select>
      </div>
      <div class="form-item">
        <label>发布日期</label>
        <input v-model="form.date" type="date" />
      </div>
      <div class="form-item">
        <label>作者</label>
        <input v-model="form.author" placeholder="如：实验室办公室" />
      </div>
      <div class="form-item">
        <label>封面图片</label>
        <ImageUpload v-model="form.image" />
      </div>
      <div class="form-item full">
        <label>摘要</label>
        <textarea v-model="form.summary" rows="2" placeholder="新闻摘要"></textarea>
      </div>
      <div class="form-item full">
        <label>正文内容</label>
        <RichTextEditor v-if="editorReady" v-model="form.content" />
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

.category-badge {
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 0.8rem;
}

.category-badge.research {
  background: #e8f5e9;
  color: #2e7d32;
}

.category-badge.award {
  background: #fff3e0;
  color: #ef6c00;
}

.category-badge.event {
  background: #e3f2fd;
  color: #1565c0;
}

.category-badge.announcement {
  background: #f3e5f5;
  color: #7b1fa2;
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
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.modal-large {
  max-width: 800px;
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
  min-height: 0;
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

.form-item textarea {
  font-family: inherit;
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
