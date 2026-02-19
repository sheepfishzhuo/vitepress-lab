---
layout: doc
---

<script setup>
import { ref } from 'vue'

const systems = ref([
  {
    id: 1,
    name: '智能计算实验管理平台',
    icon: '🔬',
    description: '一站式实验管理解决方案，支持实验设计、数据采集、结果分析全流程管理，提升科研效率。',
    features: ['实验设计', '数据采集', '结果分析', '团队协作'],
    status: 'running',
    tech: ['Vue.js', 'Spring Boot', 'MySQL'],
    year: '2024',
    link: 'https://github.com/icl-lab/exp-platform'
  },
  {
    id: 2,
    name: '学术论文智能推荐系统',
    icon: '📚',
    description: '基于深度学习的论文推荐引擎，根据研究兴趣智能推送相关领域最新研究成果。',
    features: ['智能推荐', '兴趣建模', '文献管理', '引用分析'],
    status: 'running',
    tech: ['Python', 'PyTorch', 'Elasticsearch'],
    year: '2024',
    link: 'https://github.com/icl-lab/paper-recommender'
  },
  {
    id: 3,
    name: '计算机视觉标注工具',
    icon: '👁️',
    description: '高效的图像标注平台，支持多种标注类型，内置AI辅助标注功能，大幅提升标注效率。',
    features: ['图像标注', 'AI辅助', '多人协作', '质量审核'],
    status: 'running',
    tech: ['React', 'FastAPI', 'PostgreSQL'],
    year: '2023',
    link: 'https://github.com/icl-lab/cv-annotator'
  },
  {
    id: 4,
    name: '大模型训练监控平台',
    icon: '🤖',
    description: '分布式训练监控与调优平台，实时追踪训练进度，可视化模型性能，支持多机多卡训练。',
    features: ['实时监控', '性能分析', '资源调度', '日志管理'],
    status: 'developing',
    tech: ['Python', 'Grafana', 'Kubernetes'],
    year: '2024',
    link: ''
  },
  {
    id: 5,
    name: '知识图谱构建系统',
    icon: '🕸️',
    description: '自动化知识图谱构建工具，支持多源数据融合、实体识别、关系抽取和图谱可视化。',
    features: ['实体识别', '关系抽取', '图谱可视化', '增量更新'],
    status: 'completed',
    tech: ['Neo4j', 'Python', 'D3.js'],
    year: '2023',
    link: 'https://github.com/icl-lab/kg-builder'
  },
  {
    id: 6,
    name: '智能问答系统',
    icon: '💬',
    description: '基于大语言模型的智能问答平台，支持多轮对话、知识检索和答案生成。',
    features: ['多轮对话', '知识检索', '答案生成', '意图识别'],
    status: 'completed',
    tech: ['LangChain', 'FastAPI', 'Redis'],
    year: '2023',
    link: 'https://github.com/icl-lab/smart-qa'
  }
])

const statusMap = {
  'running': { label: '运行中', class: 'running', icon: '🟢' },
  'developing': { label: '开发中', class: 'developing', icon: '🟡' },
  'completed': { label: '已完成', class: 'completed', icon: '✅' }
}

const selectedStatus = ref('all')

const filteredSystems = ref(systems.value)

const statusStats = {
  all: systems.value.length,
  running: systems.value.filter(s => s.status === 'running').length,
  developing: systems.value.filter(s => s.status === 'developing').length,
  completed: systems.value.filter(s => s.status === 'completed').length
}

const statusOptions = [
  { value: 'all', label: '全部', icon: '📋' },
  { value: 'running', label: '运行中', icon: '🟢' },
  { value: 'developing', label: '开发中', icon: '🟡' },
  { value: 'completed', label: '已完成', icon: '✅' }
]

function filterByStatus(status) {
  selectedStatus.value = status
  if (status === 'all') {
    filteredSystems.value = systems.value
  } else {
    filteredSystems.value = systems.value.filter(s => s.status === status)
  }
}
</script>

<div class="page-header">
  <h1 class="page-title">系统研发</h1>
  <div class="stats-bar">
    <div class="stat-chip">
      <span class="chip-value">{{ systems.length }}</span>
      <span class="chip-label">个系统</span>
    </div>
    <div class="stat-chip running">
      <span class="chip-value">{{ statusStats.running }}</span>
      <span class="chip-label">运行中</span>
    </div>
    <div class="stat-chip developing">
      <span class="chip-value">{{ statusStats.developing }}</span>
      <span class="chip-label">开发中</span>
    </div>
  </div>
</div>

<p class="intro-text">实验室自主研发多套科研支撑系统和智能应用平台，为科研工作提供高效便捷的工具支持。</p>

<div class="filter-section">
  <div class="filter-tabs">
    <button 
      v-for="opt in statusOptions" 
      :key="opt.value"
      :class="['filter-tab', { active: selectedStatus === opt.value }]"
      @click="filterByStatus(opt.value)"
    >
      <span class="tab-icon">{{ opt.icon }}</span>
      <span class="tab-label">{{ opt.label }}</span>
      <span class="tab-count" v-if="statusStats[opt.value]">{{ statusStats[opt.value] }}</span>
    </button>
  </div>
</div>

<div class="systems-grid">
  <div v-for="sys in filteredSystems" :key="sys.id" class="system-card">
    <div class="card-header">
      <div class="system-icon">{{ sys.icon }}</div>
      <div class="system-info">
        <h3 class="system-name">{{ sys.name }}</h3>
        <div class="system-meta">
          <span class="system-status" :class="statusMap[sys.status].class">
            {{ statusMap[sys.status].icon }} {{ statusMap[sys.status].label }}
          </span>
          <span class="system-year">{{ sys.year }}</span>
        </div>
      </div>
    </div>
    <p class="system-desc">{{ sys.description }}</p>
    <div class="system-features">
      <span v-for="feature in sys.features" :key="feature" class="feature-tag">{{ feature }}</span>
    </div>
    <div class="system-footer">
      <div class="tech-stack">
        <span class="tech-label">技术栈：</span>
        <span v-for="tech in sys.tech" :key="tech" class="tech-tag">{{ tech }}</span>
      </div>
      <a v-if="sys.link" :href="sys.link" target="_blank" class="system-link">
        <span>访问项目</span>
        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6"/><polyline points="15 3 21 3 21 9"/><line x1="10" y1="14" x2="21" y2="3"/></svg>
      </a>
    </div>
  </div>
</div>

<div v-if="filteredSystems.length === 0" class="empty-state">
  <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"><rect x="2" y="3" width="20" height="14" rx="2" ry="2"/><line x1="8" y1="21" x2="16" y2="21"/><line x1="12" y1="17" x2="12" y2="21"/></svg>
  <p>暂无匹配的系统</p>
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

.stat-chip.running {
  background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
  border-color: #a5d6a7;
}

.stat-chip.running .chip-value {
  color: #2e7d32;
}

.stat-chip.developing {
  background: linear-gradient(135deg, #fff3e0 0%, #ffe0b2 100%);
  border-color: #ffcc80;
}

.stat-chip.developing .chip-value {
  color: #ef6c00;
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

.systems-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(360px, 1fr));
  gap: 1.25rem;
}

.system-card {
  background: var(--vp-c-bg);
  border: 1px solid var(--vp-c-divider);
  border-radius: 12px;
  padding: 1.5rem;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
}

.system-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.1);
  border-color: var(--vp-c-brand-1);
}

.card-header {
  display: flex;
  align-items: flex-start;
  gap: 1rem;
  margin-bottom: 1rem;
}

.system-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  background: linear-gradient(135deg, var(--vp-c-brand-soft) 0%, var(--vp-c-bg-soft) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.75rem;
  flex-shrink: 0;
}

.system-info {
  flex: 1;
  min-width: 0;
}

.system-name {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--vp-c-text-1);
  margin: 0 0 0.5rem;
  line-height: 1.3;
}

.system-meta {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.system-status {
  font-size: 0.75rem;
  padding: 2px 8px;
  border-radius: 12px;
  font-weight: 500;
}

.system-status.running {
  background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
  color: #2e7d32;
}

.system-status.developing {
  background: linear-gradient(135deg, #fff3e0 0%, #ffe0b2 100%);
  color: #ef6c00;
}

.system-status.completed {
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
  color: #1565c0;
}

.system-year {
  font-size: 0.8rem;
  color: var(--vp-c-text-3);
}

.system-desc {
  font-size: 0.9rem;
  color: var(--vp-c-text-2);
  line-height: 1.6;
  margin: 0 0 1rem;
  flex: 1;
}

.system-features {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.feature-tag {
  font-size: 0.75rem;
  padding: 3px 10px;
  background: var(--vp-c-bg-soft);
  border-radius: 12px;
  color: var(--vp-c-text-2);
  border: 1px solid var(--vp-c-divider);
}

.system-footer {
  padding-top: 1rem;
  border-top: 1px dashed var(--vp-c-divider);
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 0.75rem;
}

.tech-stack {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.tech-label {
  font-size: 0.8rem;
  color: var(--vp-c-text-3);
}

.tech-tag {
  font-size: 0.7rem;
  padding: 2px 8px;
  background: linear-gradient(135deg, #667eea15 0%, #764ba215 100%);
  border-radius: 10px;
  color: #667eea;
  border: 1px solid #667eea30;
}

.system-link {
  display: inline-flex;
  align-items: center;
  gap: 0.35rem;
  padding: 0.4rem 0.8rem;
  background: var(--vp-c-brand-soft);
  color: var(--vp-c-brand-1);
  border-radius: 6px;
  text-decoration: none;
  font-size: 0.8rem;
  font-weight: 500;
  transition: all 0.2s ease;
}

.system-link:hover {
  background: var(--vp-c-brand-1);
  color: white;
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

@media (max-width: 640px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .systems-grid {
    grid-template-columns: 1fr;
  }
}
</style>
