---
layout: doc
---

<script setup>
import { ref, onMounted, computed } from 'vue'

const conferences = ref([])
const loading = ref(true)

const typeMap = {
  'organize': { label: '主办会议', class: 'organize' },
  'present': { label: '学术报告', class: 'present' },
  'attend': { label: '参会交流', class: 'attend' }
}

function parseArray(str) {
  if (!str) return []
  if (Array.isArray(str)) return str
  return str.split(',').map(s => s.trim()).filter(Boolean)
}

onMounted(async () => {
  try {
    const response = await fetch('http://localhost:8080/api/conferences')
    const data = await response.json()
    conferences.value = (data.data || []).map(c => ({
      ...c,
      year: parseInt(c.year) || parseInt(c.date?.split('-')[0]) || c.year,
      participants: parseArray(c.participants)
    }))
  } catch (error) {
    console.error('Failed to fetch conferences:', error)
  } finally {
    loading.value = false
  }
})

const totalConferences = computed(() => conferences.value.length)
const organizeCount = computed(() => conferences.value.filter(c => c.type === 'organize').length)
const presentCount = computed(() => conferences.value.filter(c => c.type === 'present').length)
const attendCount = computed(() => conferences.value.filter(c => c.type === 'attend').length)

const years = computed(() => {
  if (!conferences.value || !Array.isArray(conferences.value) || conferences.value.length === 0) return []
  const yearSet = new Set(conferences.value.filter(c => c && c.year).map(c => c.year))
  return Array.from(yearSet).sort((a, b) => b - a)
})

function getConferencesByYear(year) {
  return conferences.value.filter(c => c && c.year === year)
}
</script>

<div class="page-header">
  <h1 class="page-title">学术会议</h1>
  <div class="stats-bar">
    <div class="stat-chip">
      <span class="chip-value">{{ totalConferences }}</span>
      <span class="chip-label">总数</span>
    </div>
    <div class="stat-chip">
      <span class="chip-value">{{ organizeCount }}</span>
      <span class="chip-label">主办会议</span>
    </div>
    <div class="stat-chip">
      <span class="chip-value">{{ presentCount }}</span>
      <span class="chip-label">学术报告</span>
    </div>
    <div class="stat-chip">
      <span class="chip-value">{{ attendCount }}</span>
      <span class="chip-label">参会交流</span>
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
      <span class="year-count">{{ getConferencesByYear(year).length }} 项</span>
    </div>
    <div class="conferences-grid">
      <div v-for="conf in getConferencesByYear(year)" :key="conf.id" class="conference-card">
        <div class="card-image" v-if="conf.image">
          <img :src="conf.image" :alt="conf.name" />
        </div>
        <div class="card-body">
          <h3 class="conf-name">{{ conf.name }}</h3>
          <p class="conf-desc" v-if="conf.description">{{ conf.description }}</p>
        </div>
        <div class="card-footer">
          <div class="footer-row">
            <span class="conf-type" :class="typeMap[conf.type] ? typeMap[conf.type].class : ''">
              {{ typeMap[conf.type] ? typeMap[conf.type].label : conf.type }}
            </span>
          </div>
          <div class="meta-row" v-if="conf.location">
            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/><circle cx="12" cy="10" r="3"/></svg>
            <span>{{ conf.location }}</span>
          </div>
          <div class="meta-row" v-if="conf.date">
            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
            <span>{{ conf.date }}</span>
          </div>
          <div class="meta-row" v-if="conf.participants && conf.participants.length">
            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>
            <span>{{ conf.participants.slice(0, 3).join('、') }}{{ conf.participants.length > 3 ? ' 等' : '' }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<div v-else class="empty-state">
  <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>
  <p>暂无会议记录</p>
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

.conferences-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: 1.25rem;
}

.conference-card {
  background: var(--vp-c-bg);
  border: 1px solid var(--vp-c-divider);
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
}

.conference-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.1);
  border-color: var(--vp-c-brand-1);
}

.card-image {
  width: 100%;
  height: 160px;
  overflow: hidden;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.conference-card:hover .card-image img {
  transform: scale(1.05);
}

.conf-type {
  font-size: 0.75rem;
  padding: 4px 10px;
  border-radius: 20px;
  font-weight: 500;
}

.conf-type.organize {
  background: linear-gradient(135deg, #fce4ec 0%, #f8bbd9 100%);
  color: #c2185b;
}

.conf-type.present {
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
  color: #1565c0;
}

.conf-type.attend {
  background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
  color: #2e7d32;
}

.card-body {
  padding: 1.25rem;
  flex: 1;
}

.conf-name {
  font-size: 1.05rem;
  font-weight: 600;
  color: var(--vp-c-text-1);
  margin: 0 0 0.5rem;
  line-height: 1.4;
}

.conf-desc {
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

.footer-row {
  margin-bottom: 0.75rem;
  padding-bottom: 0.5rem;
  border-bottom: 1px dashed var(--vp-c-divider);
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
  
  .conferences-grid {
    grid-template-columns: 1fr;
  }
}
</style>
