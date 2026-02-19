<script setup lang="ts">
import type { NewsItem } from '../../types'

const props = defineProps<{
  news: NewsItem
}>()

const categoryMap: Record<string, { label: string; class: string }> = {
  'research': { label: '科研成果', class: 'research' },
  'award': { label: '获奖喜报', class: 'award' },
  'event': { label: '学术活动', class: 'event' },
  'announcement': { label: '通知公告', class: 'announcement' }
}
</script>

<template>
  <div class="news-detail" v-if="news">
    <div class="news-header">
      <div class="news-meta">
        <span class="news-category" :class="categoryMap[news.category].class">
          {{ categoryMap[news.category].label }}
        </span>
        <span class="news-date">{{ news.date }}</span>
      </div>
      <h1 class="news-title">{{ news.title }}</h1>
    </div>
    
    <div class="news-image" v-if="news.image">
      <img :src="news.image" :alt="news.title" />
    </div>
    
    <div class="news-content" v-html="renderMarkdown(news.content)"></div>
    
    <div class="news-footer">
      <a href="/news/" class="back-link">← 返回新闻列表</a>
    </div>
  </div>
  <div v-else class="not-found">
    <p>新闻未找到</p>
    <a href="/news/">返回新闻列表</a>
  </div>
</template>

<script lang="ts">
function renderMarkdown(content: string | undefined): string {
  if (!content) return ''
  
  let html = content
    .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    .replace(/\n\n/g, '</p><p>')
    .replace(/\n- /g, '</p><ul><li>')
    .replace(/\n(\d+)\. /g, '</p><ol><li>')
    .replace(/<\/ul><ul>/g, '')
    .replace(/<\/ol><ol>/g, '')
    .replace(/<li>(.*?)<\/p>/g, '<li>$1</li></ul>')
  
  if (!html.startsWith('<')) {
    html = '<p>' + html
  }
  if (!html.endsWith('</p>')) {
    html = html + '</p>'
  }
  
  return html
}
</script>

<style scoped>
.news-detail {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem 1rem;
}

.news-header {
  margin-bottom: 2rem;
}

.news-meta {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1rem;
}

.news-category {
  font-size: 0.8rem;
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
  font-size: 2rem;
  font-weight: 700;
  line-height: 1.3;
  margin: 0;
  color: var(--vp-c-text-1);
}

.news-image {
  margin-bottom: 2rem;
  border-radius: 8px;
  overflow: hidden;
}

.news-image img {
  width: 100%;
  height: auto;
}

.news-content {
  font-size: 1rem;
  line-height: 1.8;
  color: var(--vp-c-text-1);
}

.news-content :deep(p) {
  margin: 0 0 1rem;
}

.news-content :deep(strong) {
  font-weight: 600;
}

.news-content :deep(ul),
.news-content :deep(ol) {
  margin: 1rem 0;
  padding-left: 1.5rem;
}

.news-content :deep(li) {
  margin: 0.5rem 0;
}

.news-footer {
  margin-top: 3rem;
  padding-top: 2rem;
  border-top: 1px solid var(--vp-c-divider);
}

.back-link {
  color: var(--vp-c-brand-1);
  text-decoration: none;
  font-weight: 500;
}

.back-link:hover {
  text-decoration: underline;
}

.not-found {
  text-align: center;
  padding: 4rem 1rem;
}

.not-found p {
  font-size: 1.25rem;
  color: var(--vp-c-text-2);
  margin-bottom: 1rem;
}

.not-found a {
  color: var(--vp-c-brand-1);
}
</style>
