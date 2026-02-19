---
layout: home

hero:
  name: "人工智能"
  text: "与大数据实验室"
  tagline: "探索人工智能前沿，创造智能未来"
  image:
    src: /logo.svg
    alt: 实验室标志

features:
  - icon: 🧠
    title: 深度学习
    details: 研究神经网络架构设计、优化算法和高效训练方法
  - icon: 🔍
    title: 计算机视觉
    details: 图像识别、目标检测、视频理解等视觉智能研究
  - icon: 💬
    title: 自然语言处理
    details: 大语言模型、文本理解、对话系统等语言智能研究
  - icon: 🤖
    title: 多模态学习
    details: 跨模态表示学习、视觉-语言理解与生成
---

<HomeUnderline />

<script setup>
import StatCard from '/.vitepress/theme/components/StatCard.vue'
import NewsCard from '/.vitepress/theme/components/NewsCard.vue'
import FacultyCard from '/.vitepress/theme/components/FacultyCard.vue'
import PaperCard from '/.vitepress/theme/components/PaperCard.vue'
import { ref, onMounted, computed } from 'vue'

const facultyData = ref([])
const studentData = ref([])
const paperData = ref([])
const newsData = ref([])
const competitionData = ref([])
const loading = ref(true)

function parseArray(str) {
  if (!str) return []
  if (Array.isArray(str)) return str
  return str.split(',').map(s => s.trim()).filter(Boolean)
}

onMounted(async () => {
  try {
    const [facultyRes, studentsRes, papersRes, newsRes, competitionsRes] = await Promise.all([
      fetch('http://localhost:8080/api/faculty'),
      fetch('http://localhost:8080/api/students'),
      fetch('http://localhost:8080/api/papers'),
      fetch('http://localhost:8080/api/news'),
      fetch('http://localhost:8080/api/competitions')
    ])
    
    const facultyJson = await facultyRes.json()
    const studentsJson = await studentsRes.json()
    const papersJson = await papersRes.json()
    const newsJson = await newsRes.json()
    const competitionsJson = await competitionsRes.json()
    
    facultyData.value = (facultyJson.data || []).map(f => ({
      ...f,
      researchInterests: parseArray(f.researchInterests)
    }))
    studentData.value = studentsJson.data || []
    paperData.value = (papersJson.data || []).map(p => ({
      ...p,
      authors: parseArray(p.authors),
      keywords: parseArray(p.keywords)
    }))
    newsData.value = newsJson.data || []
    competitionData.value = competitionsJson.data || []
  } catch (error) {
    console.error('Failed to fetch data:', error)
  } finally {
    loading.value = false
  }
})

const stats = computed(() => [
  { icon: '👨‍🏫', value: facultyData.value.length, label: '指导教师', suffix: '人' },
  { icon: '👨‍🎓', value: studentData.value.filter(s => s.status === 'enrolled').length, label: '在读学生', suffix: '人' },
  { icon: '📚', value: paperData.value.length, label: '发表论文', suffix: '篇' },
  { icon: '🏆', value: competitionData.value.length, label: '竞赛获奖', suffix: '项' }
])

const recentNews = computed(() => newsData.value.slice(0, 3))
const allFaculty = computed(() => facultyData.value)
const recentPapers = computed(() => paperData.value.slice(0, 3))
</script>

<div class="lab-intro">
  <h2>实验室简介</h2>
  <p>人工智能与大数据实验室成立于2018年，隶属于计算机科学与技术系。实验室致力于人工智能与机器学习的前沿研究，在深度学习、计算机视觉、自然语言处理等领域取得了丰硕的研究成果。实验室现有教授1人、副教授2人，博士研究生2人、硕士研究生5人、本科生5人。</p>
  <p>实验室承担多项国家级和省部级科研项目，在TPAMI、CVPR、ICML等顶级期刊和会议发表论文40余篇，指导学生在各类学科竞赛中屡获佳绩。</p>
</div>

<div v-if="loading" class="loading">加载中...</div>
<template v-else>
  <div class="home-section">
    <h2 class="section-title">实验室概况</h2>
    <div class="stats-grid">
      <StatCard v-for="stat in stats" :key="stat.label" v-bind="stat" />
    </div>
  </div>

  <div class="home-section">
    <h2 class="section-title">指导教师</h2>
    <div class="faculty-grid">
      <FacultyCard v-for="faculty in allFaculty" :key="faculty.id" :faculty="faculty" />
    </div>
  </div>

  <div class="home-section">
    <h2 class="section-title">新闻动态</h2>
    <div class="news-grid">
      <NewsCard v-for="news in recentNews" :key="news.id" :news="news" />
      <!-- <NewsCard v-for="news in recentNews" :key="news.id" :news="news" compact /> -->
    </div>
    <div class="section-more">
      <a href="/news/" class="more-link">查看全部新闻 →</a>
    </div>
  </div>

  <div class="home-section">
    <h2 class="section-title">最新研究成果</h2>
    <div class="papers-grid">
      <PaperCard v-for="paper in recentPapers" :key="paper.id" :paper="paper" />
    </div>
    <div class="section-more">
      <a href="/research/papers/" class="more-link">查看全部论文 →</a>
    </div>
  </div>
</template>

<style>
.lab-intro {
  max-width: 900px;
  margin: 2rem auto;
  padding: 0 1.5rem;
  text-align: center;
}

.lab-intro h2 {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 1rem;
  color: var(--vp-c-text-1);
}

.lab-intro p {
  font-size: 1rem;
  line-height: 1.8;
  color: var(--vp-c-text-2);
  margin: 0.5rem 0;
}

.home-section {
  margin: 3rem auto;
  max-width: 1200px;
  padding: 0 1.5rem;
}

.section-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 1.5rem;
  color: var(--vp-c-text-1);
  border-left: 4px solid var(--vp-c-brand-1);
  padding-left: 1rem;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
}

.news-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1rem;
}

.faculty-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 1rem;
}

.papers-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 1rem;
}

.section-more {
  text-align: center;
  margin-top: 1.5rem;
}

.more-link {
  display: inline-block;
  padding: 0.5rem 1.5rem;
  background: var(--vp-c-brand-soft);
  color: var(--vp-c-brand-1);
  border-radius: 20px;
  text-decoration: none;
  font-size: 0.9rem;
  transition: all 0.3s ease;
}

.more-link:hover {
  background: var(--vp-c-brand-1);
  color: white;
}

.loading {
  text-align: center;
  padding: 2rem;
  color: var(--vp-c-text-2);
}

@media (max-width: 768px) {
  .news-grid {
    grid-template-columns: 1fr;
  }
}
</style>
