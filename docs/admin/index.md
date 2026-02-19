---
layout: doc
---

# 管理后台

<script setup>
import { ref, onMounted } from 'vue'
import AuthGuard from '../.vitepress/theme/components/AuthGuard.vue'

const stats = ref({
  faculty: 0,
  students: 0,
  papers: 0,
  projects: 0,
  news: 0,
  competitions: 0,
  conferences: 0,
  patents: 0
})

const loading = ref(true)

onMounted(async () => {
  try {
    const endpoints = [
      { key: 'faculty', url: 'http://localhost:8080/api/faculty' },
      { key: 'students', url: 'http://localhost:8080/api/students' },
      { key: 'papers', url: 'http://localhost:8080/api/papers' },
      { key: 'projects', url: 'http://localhost:8080/api/projects' },
      { key: 'news', url: 'http://localhost:8080/api/news' },
      { key: 'competitions', url: 'http://localhost:8080/api/competitions' },
      { key: 'conferences', url: 'http://localhost:8080/api/conferences' },
      { key: 'patents', url: 'http://localhost:8080/api/patents' }
    ]
    
    await Promise.all(endpoints.map(async ({ key, url }) => {
      try {
        const res = await fetch(url)
        const data = await res.json()
        stats.value[key] = data.data?.length || 0
      } catch (e) {
        console.error(`Failed to fetch ${key}:`, e)
      }
    }))
  } finally {
    loading.value = false
  }
})

const menuItems = [
  { title: '教师管理', desc: '管理实验室教师信息', icon: '👨‍🏫', link: '/admin/faculty', count: 'faculty' },
  { title: '学生管理', desc: '管理在校学生及校友', icon: '🎓', link: '/admin/students', count: 'students' },
  { title: '论文管理', desc: '管理学术论文发表', icon: '📄', link: '/admin/papers', count: 'papers' },
  { title: '项目管理', desc: '管理科研项目信息', icon: '🔬', link: '/admin/projects', count: 'projects' },
  { title: '新闻管理', desc: '管理实验室动态', icon: '📰', link: '/admin/news', count: 'news' },
  { title: '竞赛管理', desc: '管理竞赛获奖记录', icon: '🏆', link: '/admin/competitions', count: 'competitions' },
  { title: '会议管理', desc: '管理学术会议活动', icon: '🎤', link: '/admin/conferences', count: 'conferences' },
  { title: '专利管理', desc: '管理专利软著', icon: '💡', link: '/admin/patents', count: 'patents' }
]
</script>

<AuthGuard password="icl2024">
  <div v-if="loading" class="loading">加载中...</div>

  <div v-else class="admin-dashboard">
    <div class="stats-grid">
      <div v-for="item in menuItems" :key="item.link" class="stat-card" @click="window.location.href = item.link">
        <div class="stat-icon">{{ item.icon }}</div>
        <div class="stat-info">
          <h3>{{ item.title }}</h3>
          <p>{{ item.desc }}</p>
        </div>
        <div class="stat-count">{{ stats[item.count] }}</div>
      </div>
    </div>
  </div>
</AuthGuard>

<style scoped>
.loading {
  text-align: center;
  padding: 3rem;
  color: var(--vp-c-text-2);
}

.admin-dashboard {
  margin-top: 1rem;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 1rem;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1.25rem;
  background: var(--vp-c-bg-soft);
  border: 1px solid var(--vp-c-divider);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.stat-card:hover {
  border-color: var(--vp-c-brand-1);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.stat-icon {
  font-size: 2.5rem;
  line-height: 1;
}

.stat-info {
  flex: 1;
}

.stat-info h3 {
  margin: 0 0 0.25rem;
  font-size: 1rem;
  color: var(--vp-c-text-1);
}

.stat-info p {
  margin: 0;
  font-size: 0.85rem;
  color: var(--vp-c-text-2);
}

.stat-count {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--vp-c-brand-1);
}
</style>
