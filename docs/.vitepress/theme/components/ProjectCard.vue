<script setup lang="ts">
import type { Project } from '../../types'

defineProps<{
  project: Project
}>()

const statusMap: Record<string, { label: string; class: string }> = {
  'ongoing': { label: '进行中', class: 'ongoing' },
  'completed': { label: '已完成', class: 'completed' }
}
</script>

<template>
  <div class="project-card">
    <div class="project-header">
      <span class="project-status" :class="statusMap[project.status].class">
        {{ statusMap[project.status].label }}
      </span>
      <span class="project-date">{{ project.startDate }} ~ {{ project.endDate }}</span>
    </div>
    <h3 class="project-title">{{ project.title }}</h3>
    <p class="project-funding">
      <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><path d="M12 6v12"/><path d="M8 10h8"/><path d="M8 14h8"/></svg>
      {{ project.funding }}
      <span v-if="project.amount" class="amount">{{ project.amount }}</span>
    </p>
    <p class="project-pi" v-if="project.principalInvestigator">
      负责人：{{ project.principalInvestigator }}
    </p>
    <p class="project-desc" v-if="project.description">{{ project.description }}</p>
    <div class="project-outcomes" v-if="project.outcomes?.length">
      <h4>研究成果：</h4>
      <ul>
        <li v-for="outcome in project.outcomes" :key="outcome">{{ outcome }}</li>
      </ul>
    </div>
  </div>
</template>

<style scoped>
.project-card {
  padding: 1.25rem;
  background: var(--vp-c-bg-soft);
  border-radius: 10px;
  border: 1px solid var(--vp-c-divider);
  transition: all 0.3s ease;
}

.project-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.project-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.project-status {
  font-size: 0.75rem;
  padding: 2px 8px;
  border-radius: 4px;
  font-weight: 500;
}

.project-status.ongoing {
  background: #e3f2fd;
  color: #1565c0;
}

.project-status.completed {
  background: #e8f5e9;
  color: #2e7d32;
}

.project-date {
  font-size: 0.8rem;
  color: var(--vp-c-text-2);
}

.project-title {
  font-size: 1rem;
  margin: 0 0 0.5rem;
  color: var(--vp-c-text-1);
  line-height: 1.4;
}

.project-funding {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  font-size: 0.9rem;
  color: var(--vp-c-brand-1);
  margin: 0 0 0.25rem;
}

.project-funding .amount {
  color: #e65100;
  font-weight: 500;
}

.project-pi {
  font-size: 0.85rem;
  color: var(--vp-c-text-2);
  margin: 0 0 0.5rem;
}

.project-desc {
  font-size: 0.85rem;
  color: var(--vp-c-text-2);
  margin: 0 0 0.5rem;
  line-height: 1.5;
}

.project-outcomes {
  margin-top: 0.75rem;
  padding-top: 0.75rem;
  border-top: 1px solid var(--vp-c-divider);
}

.project-outcomes h4 {
  font-size: 0.85rem;
  margin: 0 0 0.25rem;
  color: var(--vp-c-text-1);
}

.project-outcomes ul {
  margin: 0;
  padding-left: 1rem;
  font-size: 0.8rem;
  color: var(--vp-c-text-2);
}

.project-outcomes li {
  margin-bottom: 0.25rem;
}
</style>
