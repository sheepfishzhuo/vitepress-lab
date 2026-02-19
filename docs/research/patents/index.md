---
layout: doc
---

<script setup>
import { ref, onMounted, computed } from 'vue'

const patents = ref([])
const loading = ref(true)

const typeMap = {
  'invention': { label: '发明专利', class: 'invention' },
  'utility': { label: '实用新型', class: 'utility' },
  'software': { label: '软件著作权', class: 'software' }
}

const statusMap = {
  'granted': { label: '已授权', class: 'granted' },
  'pending': { label: '审查中', class: 'pending' }
}

function parseArray(str) {
  if (!str) return []
  if (Array.isArray(str)) return str
  return str.split(',').map(s => s.trim()).filter(Boolean)
}

onMounted(async () => {
  try {
    const response = await fetch('http://localhost:8080/api/patents')
    const data = await response.json()
    patents.value = (data.data || []).map(p => ({
      ...p,
      year: parseInt(p.year) || parseInt(p.date?.split('-')[0]) || p.year,
      inventors: parseArray(p.inventors)
    }))
  } catch (error) {
    console.error('Failed to fetch patents:', error)
  } finally {
    loading.value = false
  }
})

const totalPatents = computed(() => patents.value.length)
const inventionCount = computed(() => patents.value.filter(p => p.type === 'invention').length)
const softwareCount = computed(() => patents.value.filter(p => p.type === 'software').length)
const utilityCount = computed(() => patents.value.filter(p => p.type === 'utility').length)

const years = computed(() => {
  if (!patents.value || !Array.isArray(patents.value) || patents.value.length === 0) return []
  const yearSet = new Set(patents.value.filter(p => p && p.year).map(p => p.year))
  return Array.from(yearSet).sort((a, b) => b - a)
})

function getPatentsByYear(year) {
  return patents.value.filter(p => p && p.year === year)
}
</script>

<div class="page-header">
  <h1 class="page-title">专利软著</h1>
  <div class="stats-bar">
    <div class="stat-chip">
      <span class="chip-value">{{ totalPatents }}</span>
      <span class="chip-label">总数</span>
    </div>
    <div class="stat-chip">
      <span class="chip-value">{{ inventionCount }}</span>
      <span class="chip-label">发明专利</span>
    </div>
    <div class="stat-chip">
      <span class="chip-value">{{ softwareCount }}</span>
      <span class="chip-label">软著</span>
    </div>
    <div class="stat-chip">
      <span class="chip-value">{{ utilityCount }}</span>
      <span class="chip-label">实用新型</span>
    </div>
  </div>
</div>

<div v-if="loading" class="loading">
  <div class="loading-spinner"></div>
  <span>加载中...</span>
</div>

<div v-else-if="years.length > 0">
  <div v-for="year in years" :key="year" class="year-section">
    <div class="year-header">
      <h2 class="year-title">{{ year }}年</h2>
      <span class="year-count">{{ getPatentsByYear(year).length }} 项</span>
    </div>
    <div class="patents-grid">
      <div v-for="patent in getPatentsByYear(year)" :key="patent.id" class="patent-card">
        <div class="card-header">
          <span class="patent-type" :class="typeMap[patent.type] ? typeMap[patent.type].class : ''">
            {{ typeMap[patent.type] ? typeMap[patent.type].label : patent.type }}
          </span>
          <span class="patent-status" :class="statusMap[patent.status] ? statusMap[patent.status].class : ''">
            {{ statusMap[patent.status] ? statusMap[patent.status].label : patent.status }}
          </span>
        </div>
        <div class="card-body">
          <h3 class="patent-title">{{ patent.title }}</h3>
          <p class="patent-no" v-if="patent.patentNo">{{ patent.patentNo }}</p>
          <p class="patent-desc" v-if="patent.description">{{ patent.description }}</p>
        </div>
        <div class="card-footer">
          <div class="meta-row" v-if="patent.inventors && patent.inventors.length">
            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>
            <span>{{ patent.inventors.slice(0, 3).join('、') }}{{ patent.inventors.length > 3 ? ' 等' : '' }}</span>
          </div>
          <div class="meta-row" v-if="patent.date">
            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
            <span>{{ patent.date }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<div v-else class="empty-state">
  <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/><polyline points="10 9 9 9 8 9"/></svg>
  <p>暂无专利软著记录</p>
</div>

<style scoped>
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 1rem;
  margin-bottom: 1.5rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid var(--vp-c-divider);
}

.page-title {
  font-size: 1.5rem;
  font-weight: 700;
  margin: 0;
  color: var(--vp-c-text-1);
}

.stats-bar {
  display: flex;
  gap: 0.75rem;
}

.stat-chip {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.35rem 0.75rem;
  background: var(--vp-c-bg-soft);
  border-radius: 20px;
  border: 1px solid var(--vp-c-divider);
}

.chip-value {
  font-size: 1rem;
  font-weight: 700;
  color: var(--vp-c-brand-1);
}

.chip-label {
  font-size: 0.8rem;
  color: var(--vp-c-text-2);
}

.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem;
  color: var(--vp-c-text-2);
  gap: 1rem;
}

.loading-spinner {
  width: 32px;
  height: 32px;
  border: 3px solid var(--vp-c-divider);
  border-top-color: var(--vp-c-brand-1);
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.year-section {
  margin-bottom: 2.5rem;
}

.year-header {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1.5rem;
  padding-bottom: 0.75rem;
  border-bottom: 2px solid var(--vp-c-divider);
}

.year-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--vp-c-text-1);
  margin: 0;
}

.year-count {
  font-size: 0.85rem;
  padding: 4px 12px;
  background: var(--vp-c-brand-soft);
  color: var(--vp-c-brand-1);
  border-radius: 20px;
  font-weight: 500;
}

.patents-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: 1.25rem;
}

.patent-card {
  background: var(--vp-c-bg);
  border: 1px solid var(--vp-c-divider);
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
}

.patent-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.1);
  border-color: var(--vp-c-brand-1);
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 1.25rem;
  background: var(--vp-c-bg-soft);
  border-bottom: 1px solid var(--vp-c-divider);
}

.patent-type {
  font-size: 0.75rem;
  padding: 4px 10px;
  border-radius: 20px;
  font-weight: 500;
}

.patent-type.invention {
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
  color: #1565c0;
}

.patent-type.utility {
  background: linear-gradient(135deg, #fff3e0 0%, #ffe0b2 100%);
  color: #ef6c00;
}

.patent-type.software {
  background: linear-gradient(135deg, #f3e5f5 0%, #e1bee7 100%);
  color: #7b1fa2;
}

.patent-status {
  font-size: 0.7rem;
  padding: 3px 8px;
  border-radius: 12px;
  font-weight: 500;
}

.patent-status.granted {
  background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
  color: #2e7d32;
}

.patent-status.pending {
  background: linear-gradient(135deg, #fff8e1 0%, #ffecb3 100%);
  color: #f57c00;
}

.card-body {
  padding: 1.25rem;
  flex: 1;
}

.patent-title {
  font-size: 1.05rem;
  font-weight: 600;
  color: var(--vp-c-text-1);
  margin: 0 0 0.5rem;
  line-height: 1.4;
}

.patent-no {
  font-size: 0.85rem;
  color: var(--vp-c-text-2);
  margin: 0 0 0.5rem;
  font-family: monospace;
}

.patent-desc {
  font-size: 0.85rem;
  color: var(--vp-c-text-2);
  margin: 0;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-footer {
  padding: 1rem 1.25rem;
  background: var(--vp-c-bg-soft);
  border-top: 1px solid var(--vp-c-divider);
}

.meta-row {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.8rem;
  color: var(--vp-c-text-2);
  margin-bottom: 0.25rem;
}

.meta-row:last-child {
  margin-bottom: 0;
}

.meta-row svg {
  flex-shrink: 0;
  color: var(--vp-c-text-3);
}

.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  color: var(--vp-c-text-3);
}

.empty-state svg {
  margin-bottom: 1rem;
  opacity: 0.5;
}

.empty-state p {
  margin: 0;
  font-size: 1rem;
}

@media (max-width: 640px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .patents-grid {
    grid-template-columns: 1fr;
  }
}
</style>
