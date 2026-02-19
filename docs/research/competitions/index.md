---
layout: doc
---

<script setup>
import { ref, onMounted, computed } from 'vue'

const competitions = ref([])
const loading = ref(true)

const levelMap = {
  'international': { label: '国际级', class: 'international' },
  'national': { label: '国家级', class: 'national' },
  'provincial': { label: '省级', class: 'provincial' }
}

const awardColors = {
  '一等奖': 'gold',
  '二等奖': 'silver', 
  '三等奖': 'bronze',
  '金奖': 'gold',
  '银奖': 'silver',
  '铜奖': 'bronze',
  '冠军': 'gold',
  '亚军': 'silver',
  '季军': 'bronze'
}

function getAwardClass(award) {
  if (!award) return 'default'
  for (const key in awardColors) {
    if (award.includes(key)) return awardColors[key]
  }
  return 'default'
}

function parseArray(str) {
  if (!str) return []
  if (Array.isArray(str)) return str
  return str.split(',').map(s => s.trim()).filter(Boolean)
}

onMounted(async () => {
  try {
    const response = await fetch('http://localhost:8080/api/competitions')
    const data = await response.json()
    competitions.value = (data.data || []).map(c => ({
      ...c,
      year: parseInt(c.year) || c.year,
      participants: parseArray(c.participants),
      advisors: parseArray(c.advisors)
    }))
  } catch (error) {
    console.error('Failed to fetch competitions:', error)
  } finally {
    loading.value = false
  }
})

const totalAwards = computed(() => competitions.value.length)
const internationalCount = computed(() => competitions.value.filter(c => c.level === 'international').length)
const nationalCount = computed(() => competitions.value.filter(c => c.level === 'national').length)
const provincialCount = computed(() => competitions.value.filter(c => c.level === 'provincial').length)

const years = computed(() => {
  if (!competitions.value || !Array.isArray(competitions.value) || competitions.value.length === 0) return []
  const yearSet = new Set(competitions.value.filter(c => c && c.year).map(c => c.year))
  return Array.from(yearSet).sort((a, b) => b - a)
})

function getCompetitionsByYear(year) {
  return competitions.value.filter(c => c && c.year === year)
}
</script>

<div class="page-header">
  <h1 class="page-title">竞赛获奖</h1>
  <div class="stats-bar">
    <div class="stat-chip">
      <span class="chip-value">{{ totalAwards }}</span>
      <span class="chip-label">获奖总数</span>
    </div>
    <div class="stat-chip">
      <span class="chip-value">{{ internationalCount }}</span>
      <span class="chip-label">国际级</span>
    </div>
    <div class="stat-chip">
      <span class="chip-value">{{ nationalCount }}</span>
      <span class="chip-label">国家级</span>
    </div>
    <div class="stat-chip">
      <span class="chip-value">{{ provincialCount }}</span>
      <span class="chip-label">省级</span>
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
      <span class="year-count">{{ getCompetitionsByYear(year).length }} 项获奖</span>
    </div>
    <div class="competitions-grid">
      <div v-for="comp in getCompetitionsByYear(year)" :key="comp.id" class="competition-card">
        <div class="card-header">
          <div class="award-badge" :class="getAwardClass(comp.award)">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="8" r="7"/><polyline points="8.21 13.89 7 23 12 20 17 23 15.79 13.88"/></svg>
          </div>
          <div class="level-badge" :class="levelMap[comp.level] ? levelMap[comp.level].class : ''">
            {{ levelMap[comp.level] ? levelMap[comp.level].label : comp.level }}
          </div>
        </div>
        <div class="card-body">
          <h3 class="comp-name">{{ comp.name }}</h3>
          <div class="comp-award">{{ comp.award }}</div>
          <p class="comp-desc" v-if="comp.description">{{ comp.description }}</p>
        </div>
        <div class="card-footer">
          <div class="meta-row" v-if="comp.participants && comp.participants.length">
            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>
            <span>{{ comp.participants.slice(0, 3).join('、') }}{{ comp.participants.length > 3 ? ' 等' : '' }}</span>
          </div>
          <div class="meta-row" v-if="comp.advisors && comp.advisors.length">
            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
            <span>{{ comp.advisors.join('、') }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<div v-else class="empty-state">
  <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="8" r="7"/><polyline points="8.21 13.89 7 23 12 20 17 23 15.79 13.88"/></svg>
  <p>暂无获奖记录</p>
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

.competitions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: 1.25rem;
}

.competition-card {
  background: var(--vp-c-bg);
  border: 1px solid var(--vp-c-divider);
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
}

.competition-card:hover {
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

.award-badge {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.award-badge.gold {
  background: linear-gradient(135deg, #ffd700 0%, #ffaa00 100%);
  color: #1a1a2e;
  box-shadow: 0 2px 8px rgba(255, 215, 0, 0.4);
}

.award-badge.silver {
  background: linear-gradient(135deg, #c0c0c0 0%, #a8a8a8 100%);
  color: #333;
  box-shadow: 0 2px 8px rgba(192, 192, 192, 0.4);
}

.award-badge.bronze {
  background: linear-gradient(135deg, #cd7f32 0%, #b87333 100%);
  color: white;
  box-shadow: 0 2px 8px rgba(205, 127, 50, 0.4);
}

.award-badge.default {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.level-badge {
  font-size: 0.75rem;
  padding: 4px 10px;
  border-radius: 20px;
  font-weight: 500;
}

.level-badge.international {
  background: linear-gradient(135deg, #fce4ec 0%, #f8bbd9 100%);
  color: #c2185b;
}

.level-badge.national {
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
  color: #1565c0;
}

.level-badge.provincial {
  background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
  color: #2e7d32;
}

.card-body {
  padding: 1.25rem;
  flex: 1;
}

.comp-name {
  font-size: 1.05rem;
  font-weight: 600;
  color: var(--vp-c-text-1);
  margin: 0 0 0.5rem;
  line-height: 1.4;
}

.comp-award {
  font-size: 0.95rem;
  font-weight: 600;
  color: var(--vp-c-brand-1);
  margin-bottom: 0.5rem;
}

.comp-desc {
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
  
  .competitions-grid {
    grid-template-columns: 1fr;
  }
}
</style>
