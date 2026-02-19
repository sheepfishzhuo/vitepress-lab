---
layout: doc
---

<script setup>
import { ref, onMounted, computed } from 'vue'

const projects = ref([])
const loading = ref(true)
const selectedStatus = ref('all')
const searchQuery = ref('')

function parseArray(str) {
  if (!str) return []
  if (Array.isArray(str)) return str
  return str.split(',').map(s => s.trim()).filter(Boolean)
}

onMounted(async () => {
  try {
    const response = await fetch('http://localhost:8080/api/projects')
    const data = await response.json()
    projects.value = (data.data || []).map(p => ({
      ...p,
      members: parseArray(p.members),
      outcomes: parseArray(p.outcomes)
    }))
  } catch (error) {
    console.error('Failed to fetch projects:', error)
  } finally {
    loading.value = false
  }
})

const filteredProjects = computed(() => {
  let result = projects.value
  
  if (selectedStatus.value !== 'all') {
    result = result.filter(p => p.status === selectedStatus.value)
  }
  
  if (searchQuery.value.trim()) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(p => 
      p.title?.toLowerCase().includes(query) ||
      p.funding?.toLowerCase().includes(query) ||
      p.principalInvestigator?.toLowerCase().includes(query)
    )
  }
  
  return result
})

const statusStats = computed(() => {
  const stats = { all: projects.value.length }
  projects.value.forEach(p => {
    if (p.status) {
      stats[p.status] = (stats[p.status] || 0) + 1
    }
  })
  return stats
})

const sortedYears = computed(() => {
  const years = new Set()
  filteredProjects.value.forEach(p => {
    if (p.startDate) {
      const year = p.startDate.split('-')[0]
      if (year) years.add(year)
    }
  })
  return [...years].sort((a, b) => b - a)
})

const getProjectsByYear = (year) => {
  return filteredProjects.value.filter(p => p.startDate?.startsWith(year))
}

const statusMap = {
  'ongoing': { label: '进行中', class: 'ongoing' },
  'completed': { label: '已完成', class: 'completed' }
}

const statusOptions = [
  { value: 'all', label: '全部', icon: '📋' },
  { value: 'ongoing', label: '进行中', icon: '🔄' },
  { value: 'completed', label: '已完成', icon: '✅' }
]

function getYear(project) {
  return project.startDate?.split('-')[0] || ''
}
</script>

<div class="page-header">
  <h1 class="page-title">科研项目</h1>
  <div class="stats-bar">
    <div class="stat-chip">
      <span class="chip-value">{{ projects.length }}</span>
      <span class="chip-label">个项目</span>
    </div>
    <div class="stat-chip ongoing">
      <span class="chip-value">{{ statusStats.ongoing || 0 }}</span>
      <span class="chip-label">进行中</span>
    </div>
    <div class="stat-chip completed">
      <span class="chip-value">{{ statusStats.completed || 0 }}</span>
      <span class="chip-label">已完成</span>
    </div>
  </div>
</div>

<p class="intro-text">实验室承担多项国家级和省部级科研项目，研究方向涵盖人工智能、机器学习、计算机视觉等领域。</p>

<div class="filter-section">
  <div class="filter-tabs">
    <button 
      v-for="opt in statusOptions" 
      :key="opt.value"
      :class="['filter-tab', { active: selectedStatus === opt.value }]"
      @click="selectedStatus = opt.value"
    >
      <span class="tab-icon">{{ opt.icon }}</span>
      <span class="tab-label">{{ opt.label }}</span>
      <span class="tab-count" v-if="statusStats[opt.value]">{{ statusStats[opt.value] }}</span>
    </button>
  </div>
  <div class="search-box">
    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/></svg>
    <input 
      v-model="searchQuery" 
      type="text" 
      placeholder="搜索项目名称、来源、负责人..." 
      class="search-input"
    />
  </div>
</div>

<div v-if="loading" class="loading-state">
  <div class="spinner"></div>
  <span>加载中...</span>
</div>

<template v-else>
  <div v-if="filteredProjects.length === 0" class="empty-state">
    <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/></svg>
    <p>暂无匹配的项目数据</p>
  </div>
  
  <template v-else>
    <div v-for="year in sortedYears" :key="year" class="year-section">
      <div class="year-header">
        <h2 class="year-title">{{ year }}年</h2>
        <span class="year-count">{{ getProjectsByYear(year).length }} 项</span>
      </div>
      <div class="projects-grid">
        <div v-for="project in getProjectsByYear(year)" :key="project.id" class="project-card">
          <div class="project-header">
            <span class="project-status" :class="statusMap[project.status]?.class || ''">
              {{ statusMap[project.status]?.label || project.status }}
            </span>
            <span class="project-date">{{ project.startDate }} ~ {{ project.endDate || '至今' }}</span>
          </div>
          <h3 class="project-title">{{ project.title }}</h3>
          <div class="project-info">
            <div class="info-row" v-if="project.funding">
              <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><path d="M12 6v12"/><path d="M8 10h8"/><path d="M8 14h8"/></svg>
              <span>{{ project.funding }}</span>
              <span v-if="project.amount" class="amount">{{ project.amount }}</span>
            </div>
            <div class="info-row" v-if="project.principalInvestigator">
              <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
              <span>负责人：{{ project.principalInvestigator }}</span>
            </div>
            <div class="info-row" v-if="project.members?.length">
              <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>
              <span>{{ project.members.slice(0, 3).join('、') }}{{ project.members.length > 3 ? ' 等' : '' }}</span>
            </div>
          </div>
          <p class="project-desc" v-if="project.description">{{ project.description }}</p>
          <div class="project-footer" v-if="project.outcomes?.length">
            <div class="outcomes-label">研究成果</div>
            <div class="outcomes-tags">
              <span v-for="outcome in project.outcomes.slice(0, 3)" :key="outcome" class="outcome-tag">{{ outcome }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
</template>

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

.stat-chip.ongoing {
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
  border-color: #90caf9;
}

.stat-chip.ongoing .chip-value {
  color: #1565c0;
}

.stat-chip.completed {
  background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
  border-color: #a5d6a7;
}

.stat-chip.completed .chip-value {
  color: #2e7d32;
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

.intro-text {
  font-size: 1rem;
  color: var(--vp-c-text-2);
  margin-bottom: 1.5rem;
  line-height: 1.6;
}

.filter-section {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  margin-bottom: 2rem;
}

.filter-tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.filter-tab {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  padding: 0.5rem 1rem;
  background: var(--vp-c-bg);
  border: 1px solid var(--vp-c-divider);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 0.9rem;
}

.filter-tab:hover {
  border-color: var(--vp-c-brand-1);
  background: var(--vp-c-bg-soft);
}

.filter-tab.active {
  background: var(--vp-c-brand-1);
  border-color: var(--vp-c-brand-1);
  color: white;
}

.tab-icon {
  font-size: 1rem;
}

.tab-label {
  font-weight: 500;
}

.tab-count {
  font-size: 0.75rem;
  padding: 1px 6px;
  background: rgba(0, 0, 0, 0.1);
  border-radius: 10px;
}

.filter-tab.active .tab-count {
  background: rgba(255, 255, 255, 0.2);
}

.search-box {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background: var(--vp-c-bg);
  border: 1px solid var(--vp-c-divider);
  border-radius: 8px;
  flex: 1;
  min-width: 200px;
  max-width: 300px;
}

.search-box svg {
  color: var(--vp-c-text-3);
  flex-shrink: 0;
}

.search-input {
  border: none;
  background: transparent;
  outline: none;
  font-size: 0.9rem;
  color: var(--vp-c-text-1);
  width: 100%;
}

.search-input::placeholder {
  color: var(--vp-c-text-3);
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem;
  color: var(--vp-c-text-2);
  gap: 1rem;
}

.spinner {
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

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem;
  color: var(--vp-c-text-3);
  gap: 1rem;
}

.year-section {
  margin-bottom: 2rem;
}

.year-header {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1rem;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid var(--vp-c-divider);
}

.year-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--vp-c-text-1);
  margin: 0;
}

.year-count {
  font-size: 0.85rem;
  color: var(--vp-c-text-2);
  background: var(--vp-c-bg-soft);
  padding: 2px 10px;
  border-radius: 12px;
}

.projects-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: 1rem;
}

.project-card {
  padding: 1.25rem;
  background: var(--vp-c-bg);
  border-radius: 12px;
  border: 1px solid var(--vp-c-divider);
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
}

.project-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.1);
  border-color: var(--vp-c-brand-1);
}

.project-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.75rem;
}

.project-status {
  font-size: 0.7rem;
  padding: 3px 10px;
  border-radius: 20px;
  font-weight: 600;
  letter-spacing: 0.5px;
}

.project-status.ongoing {
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
  color: #1565c0;
}

.project-status.completed {
  background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
  color: #2e7d32;
}

.project-date {
  font-size: 0.8rem;
  color: var(--vp-c-text-2);
}

.project-title {
  font-size: 1rem;
  margin: 0 0 0.75rem;
  color: var(--vp-c-text-1);
  line-height: 1.5;
  font-weight: 600;
}

.project-info {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-bottom: 0.75rem;
}

.info-row {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  font-size: 0.85rem;
  color: var(--vp-c-text-2);
}

.info-row svg {
  color: var(--vp-c-brand-1);
  flex-shrink: 0;
}

.info-row .amount {
  color: #e65100;
  font-weight: 600;
  margin-left: 0.25rem;
}

.project-desc {
  font-size: 0.85rem;
  color: var(--vp-c-text-2);
  margin: 0 0 0.75rem;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.project-footer {
  margin-top: auto;
  padding-top: 0.75rem;
  border-top: 1px dashed var(--vp-c-divider);
}

.outcomes-label {
  font-size: 0.75rem;
  color: var(--vp-c-text-3);
  margin-bottom: 0.5rem;
}

.outcomes-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.35rem;
}

.outcome-tag {
  font-size: 0.7rem;
  padding: 2px 8px;
  background: var(--vp-c-bg-soft);
  border-radius: 10px;
  color: var(--vp-c-text-2);
  border: 1px solid var(--vp-c-divider);
}

@media (max-width: 640px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .filter-section {
    flex-direction: column;
  }
  
  .search-box {
    max-width: none;
  }
  
  .projects-grid {
    grid-template-columns: 1fr;
  }
}
</style>
