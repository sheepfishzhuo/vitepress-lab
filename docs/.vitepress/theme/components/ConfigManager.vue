<template>
  <div class="config-container">
    <div class="type-tabs">
      <button 
        v-for="type in configTypes" 
        :key="type.value"
        :class="['type-tab', { active: activeType === type.value }]"
        @click="activeType = type.value"
      >
        {{ type.label }}
      </button>
    </div>

    <div v-if="loading" class="loading">加载中...</div>

    <div v-else class="config-content">
      <div class="content-header">
        <h3>{{ getTypeLabel(activeType) }}</h3>
        <button class="btn-primary" @click="openAddModal">+ 添加选项</button>
      </div>

      <div class="options-list">
        <div v-for="item in currentOptions" :key="item.id" class="option-item">
          <div class="option-info">
            <span class="option-value">{{ item.optionValue }}</span>
            <span class="option-label">{{ item.optionLabel }}</span>
          </div>
          <div class="option-actions">
            <button class="btn-small" @click="openEditModal(item)">编辑</button>
            <button class="btn-small btn-danger" @click="deleteItem(item.id)">删除</button>
          </div>
        </div>
        <div v-if="currentOptions.length === 0" class="empty">暂无选项</div>
      </div>
    </div>

    <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
      <div class="modal">
        <h3>{{ editingId ? '编辑选项' : '添加选项' }}</h3>
        <div class="form-grid">
          <div class="form-item full">
            <label>配置类型</label>
            <select v-model="form.configType">
              <option v-for="t in configTypes" :key="t.value" :value="t.value">{{ t.label }}</option>
            </select>
          </div>
          <div class="form-item">
            <label>选项值 *</label>
            <input v-model="form.optionValue" placeholder="英文标识" />
          </div>
          <div class="form-item">
            <label>显示名称 *</label>
            <input v-model="form.optionLabel" placeholder="中文显示" />
          </div>
          <div class="form-item full">
            <label>排序</label>
            <input v-model.number="form.sortOrder" type="number" />
          </div>
        </div>
        <div class="modal-actions">
          <button class="btn-secondary" @click="showModal = false">取消</button>
          <button class="btn-primary" @click="saveItem">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'

const API_URL = 'http://localhost:8080/api/config'
const configData = ref({})
const loading = ref(true)
const showModal = ref(false)
const editingId = ref(null)
const form = ref({
  configType: 'faculty_title',
  optionValue: '',
  optionLabel: '',
  sortOrder: 0
})

const configTypes = [
  { value: 'faculty_title', label: '教师职称' },
  { value: 'student_degree', label: '学生学位' },
  { value: 'student_status', label: '学生状态' },
  { value: 'paper_type', label: '论文类型' },
  { value: 'project_status', label: '项目状态' },
  { value: 'news_category', label: '新闻分类' },
  { value: 'competition_level', label: '竞赛级别' },
  { value: 'conference_type', label: '会议类型' },
  { value: 'patent_type', label: '专利类型' },
  { value: 'patent_status', label: '专利状态' }
]

const activeType = ref('faculty_title')

async function fetchData() {
  loading.value = true
  try {
    const res = await fetch(API_URL)
    const data = await res.json()
    if (data && data.data) {
      configData.value = data.data
    }
  } catch (e) {
    console.error('Failed to fetch:', e)
  } finally {
    loading.value = false
  }
}

onMounted(fetchData)

const currentOptions = computed(() => {
  const typeKey = activeType.value
  const opts = configData.value[typeKey]
  if (!opts || !Array.isArray(opts)) {
    return []
  }
  return opts.filter(item => item && typeof item === 'object' && item.id && item.optionValue && item.optionLabel)
})

function getTypeLabel(type) {
  const found = configTypes.find(t => t.value === type)
  return found ? found.label : type
}

function openAddModal() {
  editingId.value = null
  form.value = {
    configType: activeType.value,
    optionValue: '',
    optionLabel: '',
    sortOrder: currentOptions.value.length + 1
  }
  showModal.value = true
}

function openEditModal(item) {
  if (!item) return
  editingId.value = item.id
  form.value = {
    configType: item.configType,
    optionValue: item.optionValue,
    optionLabel: item.optionLabel,
    sortOrder: item.sortOrder
  }
  showModal.value = true
}

async function saveItem() {
  if (!form.value.optionValue || !form.value.optionLabel) {
    alert('请填写完整信息')
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
  }
}

async function deleteItem(id) {
  if (!confirm('确定要删除该选项吗？')) return
  
  try {
    const res = await fetch(`${API_URL}/${id}`, { method: 'DELETE' })
    const data = await res.json()
    if (data.code === 200) {
      fetchData()
    }
  } catch (e) {
    alert('删除失败')
  }
}
</script>

<style scoped>
.config-container {
  margin-top: 1rem;
}
.type-tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid var(--vp-c-divider);
}
.type-tab {
  padding: 0.5rem 1rem;
  background: var(--vp-c-bg-soft);
  border: 1px solid var(--vp-c-divider);
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.9rem;
  color: var(--vp-c-text-2);
  transition: all 0.2s;
}
.type-tab:hover {
  border-color: var(--vp-c-brand-1);
}
.type-tab.active {
  background: var(--vp-c-brand-1);
  border-color: var(--vp-c-brand-1);
  color: white;
}
.loading {
  text-align: center;
  padding: 3rem;
  color: var(--vp-c-text-2);
}
.config-content {
  background: var(--vp-c-bg);
  border: 1px solid var(--vp-c-divider);
  border-radius: 12px;
  padding: 1.5rem;
}
.content-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}
.content-header h3 {
  margin: 0;
  color: var(--vp-c-text-1);
}
.options-list {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}
.option-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem 1rem;
  background: var(--vp-c-bg-soft);
  border-radius: 8px;
}
.option-info {
  display: flex;
  gap: 1rem;
}
.option-value {
  font-family: monospace;
  color: var(--vp-c-brand-1);
  background: var(--vp-c-brand-soft);
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 0.85rem;
}
.option-label {
  color: var(--vp-c-text-1);
}
.option-actions {
  display: flex;
  gap: 0.5rem;
}
.empty {
  text-align: center;
  padding: 2rem;
  color: var(--vp-c-text-2);
}
.btn-primary {
  padding: 0.5rem 1rem;
  background: var(--vp-c-brand-1);
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
.btn-secondary {
  padding: 0.5rem 1rem;
  background: var(--vp-c-bg-soft);
  color: var(--vp-c-text-1);
  border: 1px solid var(--vp-c-divider);
  border-radius: 6px;
  cursor: pointer;
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
  max-width: 450px;
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
  margin-bottom: 0.5rem;
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
.form-item select {
  padding: 0.5rem;
  border: 1px solid var(--vp-c-divider);
  border-radius: 6px;
  background: var(--vp-c-bg);
  color: var(--vp-c-text-1);
  width: 100%;
  box-sizing: border-box;
}
.form-item input:focus,
.form-item select:focus {
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
