---
layout: doc
---

<script setup>
import PaperCard from '/.vitepress/theme/components/PaperCard.vue'
import { ref, onMounted, computed } from 'vue'

const papers = ref([])
const loading = ref(true)
const selectedType = ref('all')
const searchQuery = ref('')

function parseArray(str) {
  if (!str) return []
  if (Array.isArray(str)) return str
  return str.split(',').map(s => s.trim()).filter(Boolean)
}

onMounted(async () => {
  try {
    const response = await fetch('http://localhost:8080/api/papers')
    const data = await response.json()
    papers.value = (data.data || []).map(p => ({
      ...p,
      authors: parseArray(p.authors),
      keywords: parseArray(p.keywords)
    }))
  } catch (error) {
    console.error('Failed to fetch papers:', error)
  } finally {
    loading.value = false
  }
})

const sortedYears = computed(() => {
  const years = [...new Set(filteredPapers.value.map(p => p.year))].filter(Boolean)
  return years.sort((a, b) => b - a)
})

const typeStats = computed(() => {
  const stats = { all: papers.value.length }
  papers.value.forEach(p => {
    if (p.type) {
      stats[p.type] = (stats[p.type] || 0) + 1
    }
  })
  return stats
})

const filteredPapers = computed(() => {
  let result = papers.value
  
  if (selectedType.value !== 'all') {
    result = result.filter(p => p.type === selectedType.value)
  }
  
  if (searchQuery.value.trim()) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(p => 
      p.title?.toLowerCase().includes(query) ||
      p.venue?.toLowerCase().includes(query) ||
      p.authors?.some(a => a.toLowerCase().includes(query))
    )
  }
  
  return result
})

const getPapersByYear = (year) => {
  return filteredPapers.value.filter(p => p.year === year)
}

const typeOptions = [
  { value: 'all', label: '全部', icon: '📚' },
  { value: 'journal', label: '期刊', icon: '📖' },
  { value: 'conference', label: '会议', icon: '🎤' },
  { value: 'workshop', label: '研讨会', icon: '💡' },
  { value: 'preprint', label: '预印本', icon: '📄' }
]
</script>

<div class="page-header">
  <h1 class="page-title">学术论文</h1>
  <div class="stats-bar">
    <div class="stat-chip">
      <span class="chip-value">{{ papers.length }}</span>
      <span class="chip-label">篇论文</span>
    </div>
    <div class="stat-chip">
      <span class="chip-value">{{ sortedYears.length }}</span>
      <span class="chip-label">年份</span>
    </div>
  </div>
</div>

<p class="intro-text">实验室在人工智能领域顶级期刊和会议发表了大量高水平论文，以下是我们近年来的主要研究成果。</p>

<div class="filter-section">
  <div class="filter-tabs">
    <button 
      v-for="opt in typeOptions" 
      :key="opt.value"
      :class="['filter-tab', { active: selectedType === opt.value }]"
      @click="selectedType = opt.value"
    >
      <span class="tab-icon">{{ opt.icon }}</span>
      <span class="tab-label">{{ opt.label }}</span>
      <span class="tab-count" v-if="typeStats[opt.value]">{{ typeStats[opt.value] }}</span>
    </button>
  </div>
  <div class="search-box">
    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/></svg>
    <input 
      v-model="searchQuery" 
      type="text" 
      placeholder="搜索论文标题、作者、期刊..." 
      class="search-input"
    />
  </div>
</div>

<div v-if="loading" class="loading-state">
  <div class="spinner"></div>
  <span>加载中...</span>
</div>

<template v-else>
  <div v-if="filteredPapers.length === 0" class="empty-state">
    <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"><path d="M4 19.5v-15A2.5 2.5 0 0 1 6.5 2H20v20H6.5a2.5 2.5 0 0 1 0-5H20"/></svg>
    <p>暂无匹配的论文数据</p>
  </div>
  
  <template v-else>
    <div v-for="year in sortedYears" :key="year" class="year-section">
      <div class="year-header">
        <h2 class="year-title">{{ year }}年</h2>
        <span class="year-count">{{ getPapersByYear(year).length }} 篇</span>
      </div>
      <div class="papers-grid">
        <PaperCard v-for="paper in getPapersByYear(year)" :key="paper.id" :paper="paper" />
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

.papers-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: 1rem;
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
  
  .papers-grid {
    grid-template-columns: 1fr;
  }
}
</style>
