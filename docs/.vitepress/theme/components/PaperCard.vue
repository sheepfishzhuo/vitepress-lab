<script setup lang="ts">
defineProps<{
  paper: {
    id: number | string
    title: string
    authors: string[]
    venue: string
    year: number
    type: string
    doi?: string
    pdf?: string
    citations?: number
    impactFactor?: number
    keywords?: string[]
  }
}>()

const typeMap: Record<string, { label: string; class: string }> = {
  'journal': { label: '期刊', class: 'journal' },
  'conference': { label: '会议', class: 'conference' },
  'workshop': { label: '研讨会', class: 'workshop' },
  'preprint': { label: '预印本', class: 'preprint' }
}
</script>

<template>
  <div class="paper-card">
    <div class="paper-header">
      <span class="paper-type" :class="typeMap[paper.type]?.class || ''">
        {{ typeMap[paper.type]?.label || paper.type }}
      </span>
      <span class="paper-year">{{ paper.year }}</span>
    </div>
    <h3 class="paper-title">{{ paper.title }}</h3>
    <p class="paper-authors" v-if="paper.authors?.length">
      <span v-for="(author, index) in paper.authors.slice(0, 4)" :key="author">
        {{ author }}<span v-if="index < Math.min(paper.authors.length, 4) - 1">, </span>
      </span>
      <span v-if="paper.authors.length > 4" class="more-authors">等 {{ paper.authors.length }} 人</span>
    </p>
    <p class="paper-venue">
      <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M4 19.5v-15A2.5 2.5 0 0 1 6.5 2H20v20H6.5a2.5 2.5 0 0 1 0-5H20"/></svg>
      {{ paper.venue }}
    </p>
    <div class="paper-footer">
      <div class="paper-meta" v-if="paper.citations || paper.impactFactor">
        <span v-if="paper.citations" class="meta-item">
          <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
          {{ paper.citations }} 引用
        </span>
        <span v-if="paper.impactFactor" class="meta-item">
          IF: {{ paper.impactFactor }}
        </span>
      </div>
      <div class="paper-actions">
        <a v-if="paper.doi" :href="`https://doi.org/${paper.doi}`" target="_blank" class="action-link doi">
          <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M10 13a5 5 0 0 0 7.54.54l3-3a5 5 0 0 0-7.07-7.07l-1.72 1.71"/><path d="M14 11a5 5 0 0 0-7.54-.54l-3 3a5 5 0 0 0 7.07 7.07l1.71-1.71"/></svg>
          DOI
        </a>
        <a v-if="paper.pdf" :href="paper.pdf" target="_blank" class="action-link pdf">
          <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/><polyline points="10 9 9 9 8 9"/></svg>
          PDF
        </a>
      </div>
    </div>
    <div class="paper-keywords" v-if="paper.keywords?.length">
      <span v-for="keyword in paper.keywords.slice(0, 3)" :key="keyword" class="keyword-tag">
        {{ keyword }}
      </span>
    </div>
  </div>
</template>

<style scoped>
.paper-card {
  padding: 1.25rem;
  background: var(--vp-c-bg);
  border-radius: 12px;
  border: 1px solid var(--vp-c-divider);
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
}

.paper-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.1);
  border-color: var(--vp-c-brand-1);
}

.paper-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.75rem;
}

.paper-type {
  font-size: 0.7rem;
  padding: 3px 10px;
  border-radius: 20px;
  font-weight: 600;
  letter-spacing: 0.5px;
}

.paper-type.journal {
  background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
  color: #2e7d32;
}

.paper-type.conference {
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
  color: #1565c0;
}

.paper-type.workshop {
  background: linear-gradient(135deg, #fff3e0 0%, #ffe0b2 100%);
  color: #ef6c00;
}

.paper-type.preprint {
  background: linear-gradient(135deg, #f3e5f5 0%, #e1bee7 100%);
  color: #7b1fa2;
}

.paper-year {
  font-size: 0.85rem;
  color: var(--vp-c-text-2);
  font-weight: 600;
}

.paper-title {
  font-size: 1rem;
  margin: 0 0 0.5rem;
  color: var(--vp-c-text-1);
  line-height: 1.5;
  font-weight: 600;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.paper-authors {
  font-size: 0.85rem;
  color: var(--vp-c-text-2);
  margin: 0 0 0.5rem;
  line-height: 1.4;
}

.more-authors {
  color: var(--vp-c-brand-1);
  font-weight: 500;
}

.paper-venue {
  display: flex;
  align-items: center;
  gap: 0.35rem;
  font-size: 0.85rem;
  color: var(--vp-c-brand-1);
  margin: 0 0 0.75rem;
  font-style: italic;
  font-weight: 500;
}

.paper-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
  padding-top: 0.75rem;
  border-top: 1px dashed var(--vp-c-divider);
}

.paper-meta {
  display: flex;
  gap: 0.75rem;
}

.meta-item {
  display: inline-flex;
  align-items: center;
  gap: 0.25rem;
  font-size: 0.75rem;
  color: var(--vp-c-text-2);
  background: var(--vp-c-bg-soft);
  padding: 2px 8px;
  border-radius: 10px;
}

.paper-actions {
  display: flex;
  gap: 0.5rem;
}

.action-link {
  display: inline-flex;
  align-items: center;
  gap: 0.3rem;
  font-size: 0.75rem;
  padding: 4px 10px;
  border-radius: 6px;
  text-decoration: none;
  transition: all 0.2s ease;
  font-weight: 500;
}

.action-link.doi {
  background: var(--vp-c-bg-soft);
  color: var(--vp-c-brand-1);
  border: 1px solid var(--vp-c-divider);
}

.action-link.doi:hover {
  background: var(--vp-c-brand-1);
  color: white;
  border-color: var(--vp-c-brand-1);
}

.action-link.pdf {
  background: linear-gradient(135deg, #ef5350 0%, #e53935 100%);
  color: white;
  border: none;
}

.action-link.pdf:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(239, 83, 80, 0.3);
}

.paper-keywords {
  display: flex;
  flex-wrap: wrap;
  gap: 0.35rem;
  margin-top: 0.75rem;
}

.keyword-tag {
  font-size: 0.7rem;
  padding: 2px 8px;
  background: var(--vp-c-bg-soft);
  border-radius: 10px;
  color: var(--vp-c-text-2);
  border: 1px solid var(--vp-c-divider);
}
</style>
