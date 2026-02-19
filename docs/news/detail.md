---
layout: page
---

<script setup>
import { ref, onMounted, computed } from 'vue'

const news = ref(null)
const loading = ref(true)
const error = ref(null)

const newsId = computed(() => {
  if (typeof window === 'undefined') return null
  const url = new URL(window.location.href)
  return url.searchParams.get('id')
})

const categoryMap = {
  'research': { label: '科研成果', class: 'research' },
  'award': { label: '获奖喜报', class: 'award' },
  'event': { label: '学术活动', class: 'event' },
  'announcement': { label: '通知公告', class: 'announcement' }
}

function formatDateTime(dateStr) {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hour = String(date.getHours()).padStart(2, '0')
  const minute = String(date.getMinutes()).padStart(2, '0')
  return `${year}-${month}-${day} ${hour}:${minute}`
}

onMounted(async () => {
  if (!newsId.value) {
    error.value = '未指定新闻ID'
    loading.value = false
    return
  }
  
  try {
    const response = await fetch(`http://localhost:8080/api/news/${newsId.value}`)
    const data = await response.json()
    if (data.code === 200 && data.data) {
      news.value = data.data
    } else {
      error.value = data.message || '未找到该新闻'
    }
  } catch (e) {
    error.value = '加载失败，请稍后重试'
    console.error('Failed to fetch news:', e)
  } finally {
    loading.value = false
  }
})
</script>

<div v-if="loading" class="loading">
  <div class="loading-spinner"></div>
  <p>加载中...</p>
</div>

<div v-else-if="error" class="error-container">
  <div class="error-icon">⚠️</div>
  <h2>{{ error }}</h2>
  <a href="/news/" class="back-link">返回新闻列表</a>
</div>

<article v-else-if="news" class="news-detail">
  <header class="news-header">
    <div class="news-meta">
      <span class="news-category" :class="categoryMap[news.category]?.class || ''">
        {{ categoryMap[news.category]?.label || news.category }}
      </span>
    </div>
    <h1 class="news-title">{{ news.title }}</h1>
    <div class="news-info">
      <p v-if="news.author" class="news-author">作者：{{ news.author }}</p>
      <p v-if="news.createdAt" class="news-time">
        <span class="time-label">发布时间：</span>{{ formatDateTime(news.createdAt) }}
      </p>
      <p v-if="news.updatedAt && news.updatedAt !== news.createdAt" class="news-time updated">
        <span class="time-label">更新时间：</span>{{ formatDateTime(news.updatedAt) }}
      </p>
    </div>
  </header>
  
  <div class="news-content" v-html="news.content || news.summary"></div>
  
  <footer class="news-footer">
    <a href="/news/" class="back-link">
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m12 19-7-7 7-7"/><path d="M19 12H5"/></svg>
      返回新闻列表
    </a>
  </footer>
</article>

<style scoped>
.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem 2rem;
  color: var(--vp-c-text-2);
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid var(--vp-c-divider);
  border-top-color: var(--vp-c-brand-1);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.error-container {
  text-align: center;
  padding: 4rem 2rem;
}

.error-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.error-container h2 {
  color: var(--vp-c-text-1);
  margin-bottom: 1.5rem;
}

.news-detail {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem 1.5rem;
}

.news-header {
  margin-bottom: 2rem;
  padding-bottom: 1.5rem;
  border-bottom: 1px solid var(--vp-c-divider);
}

.news-meta {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1rem;
}

.news-category {
  font-size: 0.75rem;
  padding: 4px 12px;
  border-radius: 4px;
  font-weight: 500;
}

.news-category.research {
  background: #e8f5e9;
  color: #2e7d32;
}

.news-category.award {
  background: #fff3e0;
  color: #ef6c00;
}

.news-category.event {
  background: #e3f2fd;
  color: #1565c0;
}

.news-category.announcement {
  background: #f3e5f5;
  color: #7b1fa2;
}

.news-date {
  font-size: 0.9rem;
  color: var(--vp-c-text-3);
}

.news-title {
  font-size: 1.75rem;
  font-weight: 600;
  color: var(--vp-c-text-1);
  line-height: 1.4;
  margin: 0 0 0.75rem;
}

.news-info {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  margin-top: 0.5rem;
}

.news-author {
  font-size: 0.9rem;
  color: var(--vp-c-text-2);
  margin: 0;
}

.news-time {
  font-size: 0.85rem;
  color: var(--vp-c-text-3);
  margin: 0;
}

.news-time .time-label {
  color: var(--vp-c-text-2);
}

.news-time.updated {
  color: var(--vp-c-brand-1);
}

.news-cover {
  margin-bottom: 2rem;
  border-radius: 8px;
  overflow: hidden;
}

.news-cover img {
  width: 100%;
  height: auto;
  display: block;
}

.news-content {
  font-size: 1rem;
  line-height: 1.8;
  color: var(--vp-c-text-1);
}

.news-content :deep(p) {
  margin: 0 0 1rem;
}

.news-content :deep(h1) {
  font-size: 1.5rem;
  font-weight: 600;
  margin: 2rem 0 1rem;
  color: var(--vp-c-text-1);
}

.news-content :deep(h2) {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 2rem 0 1rem;
  color: var(--vp-c-text-1);
}

.news-content :deep(h3) {
  font-size: 1.1rem;
  font-weight: 600;
  margin: 1.5rem 0 0.75rem;
  color: var(--vp-c-text-1);
}

.news-content :deep(ul), .news-content :deep(ol) {
  padding-left: 1.5rem;
  margin: 0 0 1rem;
}

.news-content :deep(li) {
  margin: 0.25rem 0;
}

.news-content :deep(blockquote) {
  border-left: 4px solid var(--vp-c-brand-1);
  padding-left: 1rem;
  margin: 1rem 0;
  color: var(--vp-c-text-2);
}

.news-content :deep(img) {
  max-width: 100%;
  border-radius: 6px;
  margin: 1rem 0;
}

.news-content :deep(.ql-align-center) {
  text-align: center;
}

.news-content :deep(.ql-align-center img) {
  display: block;
  margin: 1rem auto;
}

.news-content :deep(.ql-align-right) {
  text-align: right;
}

.news-content :deep(.ql-align-right img) {
  display: block;
  margin: 1rem 0 1rem auto;
}

.news-content :deep(a) {
  color: var(--vp-c-brand-1);
  text-decoration: none;
}

.news-content :deep(a:hover) {
  text-decoration: underline;
}

.news-footer {
  margin-top: 3rem;
  padding-top: 1.5rem;
  border-top: 1px solid var(--vp-c-divider);
}

.back-link {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background: var(--vp-c-brand-soft);
  color: var(--vp-c-brand-1);
  border-radius: 6px;
  text-decoration: none;
  font-size: 0.9rem;
  transition: all 0.2s ease;
}

.back-link:hover {
  background: var(--vp-c-brand-1);
  color: white;
}
</style>
