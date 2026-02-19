---
layout: doc
---

<script setup>
import { ref } from 'vue'

const researchAreas = ref([
  {
    id: 1,
    name: '深度学习',
    icon: '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="3"/><path d="M12 1v6m0 6v10"/><path d="m4.93 4.93 4.24 4.24m5.66 5.66 4.24 4.24"/><path d="M1 12h6m6 0h10"/><path d="m4.93 19.07 4.24-4.24m5.66-5.66 4.24-4.24"/></svg>',
    description: '研究神经网络架构设计、优化算法和高效训练方法',
    topics: ['新型神经网络架构设计', '高效优化算法', '模型压缩与加速', '自监督学习'],
    color: 'blue'
  },
  {
    id: 2,
    name: '计算机视觉',
    icon: '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7Z"/><circle cx="12" cy="12" r="3"/></svg>',
    description: '研究图像识别、目标检测、视频理解等视觉智能问题',
    topics: ['图像分类与识别', '目标检测与跟踪', '语义分割', '视频理解与分析'],
    color: 'green'
  },
  {
    id: 3,
    name: '自然语言处理',
    icon: '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>',
    description: '研究大语言模型、文本理解、对话系统等语言智能问题',
    topics: ['大语言模型预训练与微调', '文本理解与生成', '知识图谱与推理', '对话系统'],
    color: 'purple'
  },
  {
    id: 4,
    name: '多模态学习',
    icon: '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="2" y="3" width="20" height="14" rx="2" ry="2"/><line x1="8" y1="21" x2="16" y2="21"/><line x1="12" y1="17" x2="12" y2="21"/></svg>',
    description: '研究跨模态表示学习、视觉-语言理解与生成',
    topics: ['视觉-语言预训练', '图文检索', '视觉问答', '图像描述生成'],
    color: 'orange'
  }
])

const quickLinks = ref([
  { name: '学术论文', path: '/research/papers/', icon: '📄', desc: '查看我们发表的论文' },
  { name: '科研项目', path: '/research/projects/', icon: '🔬', desc: '了解我们承担的科研项目' },
  { name: '系统研发', path: '/research/sysdev/', icon: '💻', desc: '查看我们开发的系统平台' },
  { name: '竞赛获奖', path: '/research/competitions/', icon: '🏆', desc: '查看竞赛获奖记录' }
])
</script>

<div class="page-header">
  <h1 class="page-title">研究方向</h1>
  <div class="stats-bar">
    <div class="stat-chip">
      <span class="chip-value">{{ researchAreas.length }}</span>
      <span class="chip-label">研究方向</span>
    </div>
  </div>
</div>

<p class="intro-text">人工智能与大数据实验室致力于人工智能与机器学习的前沿研究，探索智能计算的核心理论与关键技术。</p>

<div class="areas-grid">
  <div v-for="area in researchAreas" :key="area.id" class="area-card" :class="area.color">
    <div class="card-icon" v-html="area.icon"></div>
    <div class="card-content">
      <h3 class="area-name">{{ area.name }}</h3>
      <p class="area-desc">{{ area.description }}</p>
      <div class="topics-list">
        <span v-for="(topic, idx) in area.topics" :key="idx" class="topic-tag">{{ topic }}</span>
      </div>
    </div>
  </div>
</div>

<div class="links-section">
  <h2 class="section-title">了解更多</h2>
  <div class="links-grid">
    <a v-for="link in quickLinks" :key="link.path" :href="link.path" class="link-card">
      <span class="link-icon">{{ link.icon }}</span>
      <div class="link-content">
        <span class="link-name">{{ link.name }}</span>
        <span class="link-desc">{{ link.desc }}</span>
      </div>
      <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="9 18 15 12 9 6"/></svg>
    </a>
  </div>
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

.intro-text {
  font-size: 1rem;
  color: var(--vp-c-text-2);
  margin-bottom: 2rem;
  line-height: 1.6;
}

.areas-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 1.25rem;
  margin-bottom: 3rem;
}

.area-card {
  background: var(--vp-c-bg);
  border: 1px solid var(--vp-c-divider);
  border-radius: 12px;
  padding: 1.5rem;
  display: flex;
  gap: 1rem;
  transition: all 0.3s ease;
}

.area-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.1);
}

.area-card.blue:hover { border-color: #1565c0; }
.area-card.green:hover { border-color: #2e7d32; }
.area-card.purple:hover { border-color: #7b1fa2; }
.area-card.orange:hover { border-color: #ef6c00; }

.card-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.area-card.blue .card-icon {
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
  color: #1565c0;
}

.area-card.green .card-icon {
  background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
  color: #2e7d32;
}

.area-card.purple .card-icon {
  background: linear-gradient(135deg, #f3e5f5 0%, #e1bee7 100%);
  color: #7b1fa2;
}

.area-card.orange .card-icon {
  background: linear-gradient(135deg, #fff3e0 0%, #ffe0b2 100%);
  color: #ef6c00;
}

.card-content {
  flex: 1;
  min-width: 0;
}

.area-name {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--vp-c-text-1);
  margin: 0 0 0.5rem;
}

.area-desc {
  font-size: 0.9rem;
  color: var(--vp-c-text-2);
  margin: 0 0 0.75rem;
  line-height: 1.5;
}

.topics-list {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.topic-tag {
  font-size: 0.75rem;
  padding: 3px 8px;
  background: var(--vp-c-bg-soft);
  border-radius: 12px;
  color: var(--vp-c-text-2);
  border: 1px solid var(--vp-c-divider);
}

.links-section {
  margin-top: 2rem;
}

.section-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--vp-c-text-1);
  margin: 0 0 1rem;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid var(--vp-c-divider);
}

.links-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 1rem;
}

.link-card {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem 1.25rem;
  background: var(--vp-c-bg);
  border: 1px solid var(--vp-c-divider);
  border-radius: 10px;
  text-decoration: none;
  transition: all 0.3s ease;
}

.link-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  border-color: var(--vp-c-brand-1);
}

.link-icon {
  font-size: 1.5rem;
  flex-shrink: 0;
}

.link-content {
  flex: 1;
  min-width: 0;
}

.link-name {
  display: block;
  font-size: 0.95rem;
  font-weight: 600;
  color: var(--vp-c-text-1);
  margin-bottom: 0.25rem;
}

.link-desc {
  display: block;
  font-size: 0.8rem;
  color: var(--vp-c-text-2);
}

.link-card svg {
  color: var(--vp-c-text-3);
  flex-shrink: 0;
}

@media (max-width: 640px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .areas-grid {
    grid-template-columns: 1fr;
  }
  
  .links-grid {
    grid-template-columns: 1fr;
  }
}
</style>
