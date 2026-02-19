---
layout: doc
---

# 新闻动态

<script setup>
import NewsCard from '/.vitepress/theme/components/NewsCard.vue'
import { ref, onMounted } from 'vue'

const newsData = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    const response = await fetch('http://localhost:8080/api/news')
    const data = await response.json()
    newsData.value = data.data || []
  } catch (error) {
    console.error('Failed to fetch news:', error)
  } finally {
    loading.value = false
  }
})
</script>

<div v-if="loading" class="loading">加载中...</div>
<div v-else class="news-grid">
  <NewsCard v-for="news in newsData" :key="news.id" :news="news" />
</div>

<style>
.news-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: 1.5rem;
  margin: 1.5rem 0;
}

.loading {
  text-align: center;
  padding: 2rem;
  color: var(--vp-c-text-2);
}
</style>
