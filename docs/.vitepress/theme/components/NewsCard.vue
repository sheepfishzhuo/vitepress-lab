<script setup lang="ts">
import { useRouter } from 'vitepress'

const props = defineProps<{
  news: {
    id: number | string
    title: string
    date: string
    category: string
    summary?: string
    image?: string
  }
  compact?: boolean
}>()

const router = useRouter()

const categoryMap: Record<string, { label: string; class: string }> = {
  'research': { label: '科研成果', class: 'research' },
  'award': { label: '获奖喜报', class: 'award' },
  'event': { label: '学术活动', class: 'event' },
  'announcement': { label: '通知公告', class: 'announcement' }
}

function goToDetail() {
  router.go(`/news/detail?id=${props.news.id}`)
}
</script>

<template>
  <div class="news-card" :class="{ compact }" @click="goToDetail">
    <div class="news-image" v-if="news.image && !compact">
      <img :src="news.image" :alt="news.title" />
    </div>
    <div class="news-body">
      <div class="news-header">
        <span class="news-category" :class="categoryMap[news.category]?.class || ''">
          {{ categoryMap[news.category]?.label || news.category }}
        </span>
        <span class="news-date">{{ news.date }}</span>
      </div>
      <h3 class="news-title">{{ news.title }}</h3>
      <p class="news-summary" v-if="news.summary && !compact">{{ news.summary }}</p>
      <div class="news-footer">
        <span class="read-more">阅读全文 →</span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.news-card {
  display: flex;
  flex-direction: column;
  background: var(--vp-c-bg);
  border-radius: 12px;
  border: 1px solid var(--vp-c-divider);
  transition: all 0.3s ease;
  cursor: pointer;
  overflow: hidden;
  height: 100%;
}

.news-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  transform: translateY(-4px);
  border-color: var(--vp-c-brand-1);
}

.news-image {
  width: 100%;
  height: 160px;
  overflow: hidden;
}

.news-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.news-card:hover .news-image img {
  transform: scale(1.05);
}

.news-body {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 1rem;
}

.news-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 0.5rem;
}

.news-category {
  font-size: 0.75rem;
  padding: 2px 10px;
  border-radius: 20px;
  font-weight: 500;
}

.news-category.research {
  background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
  color: #2e7d32;
}

.news-category.award {
  background: linear-gradient(135deg, #fff3e0 0%, #ffe0b2 100%);
  color: #ef6c00;
}

.news-category.event {
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
  color: #1565c0;
}

.news-category.announcement {
  background: linear-gradient(135deg, #f3e5f5 0%, #e1bee7 100%);
  color: #7b1fa2;
}

.news-date {
  font-size: 0.8rem;
  color: var(--vp-c-text-3);
}

.news-title {
  font-size: 1.05rem;
  margin: 0 0 0.5rem;
  line-height: 1.5;
  color: var(--vp-c-text-1);
  font-weight: 600;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.news-summary {
  font-size: 0.85rem;
  color: var(--vp-c-text-2);
  margin: 0 0 auto;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.news-footer {
  margin-top: 0.75rem;
  padding-top: 0.75rem;
  border-top: 1px solid var(--vp-c-divider);
}

.read-more {
  font-size: 0.85rem;
  color: var(--vp-c-brand-1);
  font-weight: 500;
  transition: all 0.3s ease;
}

.news-card:hover .read-more {
  color: var(--vp-c-brand-2);
}

.news-card.compact {
  flex-direction: row;
  align-items: center;
}

.news-card.compact .news-body {
  padding: 0.75rem 1rem;
}

.news-card.compact .news-header {
  margin-bottom: 0.25rem;
}

.news-card.compact .news-title {
  font-size: 0.95rem;
  margin-bottom: 0;
  -webkit-line-clamp: 1;
}

.news-card.compact .news-footer {
  margin-top: 0.5rem;
  padding-top: 0.5rem;
}
</style>
