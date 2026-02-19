<script setup lang="ts">
import type { Faculty } from '../../types'

const props = defineProps<{
  faculty: Faculty
  compact?: boolean
}>()

const titleMap: Record<string, string> = {
  '教授': 'professor',
  '副教授': 'associate-professor',
  '讲师': 'lecturer',
  '助理教授': 'assistant-professor'
}

function goToDetail() {
  window.location.href = `/team/faculty?id=${props.faculty.id}`
}
</script>

<template>
  <div class="faculty-card" :class="{ compact }" @click="goToDetail">
    <div class="faculty-avatar">
      <div class="avatar-placeholder">
        {{ faculty.name.charAt(0) }}
      </div>
    </div>
    <div class="faculty-info">
      <h3 class="faculty-name">
        {{ faculty.name }}
        <span class="faculty-title" :class="titleMap[faculty.title]">{{ faculty.title }}</span>
        <span v-if="faculty.role" class="faculty-role">{{ faculty.role }}</span>
      </h3>
      <div class="faculty-interests" v-if="!compact && faculty.researchInterests?.length">
        <span 
          v-for="interest in faculty.researchInterests.slice(0, 3)" 
          :key="interest" 
          class="interest-tag"
        >
          {{ interest }}
        </span>
      </div>
      <div class="faculty-contact" v-if="!compact">
        <span v-if="faculty.email" class="contact-item">
          <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="20" height="16" x="2" y="4" rx="2"/><path d="m22 7-8.97 5.7a1.94 1.94 0 0 1-2.06 0L2 7"/></svg>
          {{ faculty.email }}
        </span>
      </div>
    </div>
    <div class="card-arrow">
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m9 18 6-6-6-6"/></svg>
    </div>
  </div>
</template>

<style scoped>
.faculty-card {
  display: flex;
  gap: 1rem;
  padding: 1.25rem;
  background: var(--vp-c-bg-soft);
  border-radius: 12px;
  transition: all 0.3s ease;
  border: 1px solid var(--vp-c-divider);
  cursor: pointer;
  position: relative;
}

.faculty-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
  border-color: var(--vp-c-brand-1);
}

.faculty-avatar {
  flex-shrink: 0;
  width: 80px;
  height: 80px;
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--vp-c-brand-1), var(--vp-c-brand-2));
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.5rem;
  font-weight: 600;
}

.faculty-info {
  flex: 1;
  min-width: 0;
}

.faculty-name {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin: 0 0 0.25rem;
  font-size: 1.1rem;
  flex-wrap: wrap;
  color: var(--vp-c-text-1);
}

.faculty-title {
  font-size: 0.75rem;
  padding: 2px 8px;
  border-radius: 4px;
  font-weight: 500;
}

.faculty-title.professor {
  background: #e8f5e9;
  color: #2e7d32;
}

.faculty-title.associate-professor {
  background: #e3f2fd;
  color: #1565c0;
}

.faculty-title.lecturer {
  background: #fff3e0;
  color: #ef6c00;
}

.faculty-role {
  font-size: 0.7rem;
  padding: 2px 8px;
  border-radius: 4px;
  background: #fce4ec;
  color: #c2185b;
  font-weight: 500;
}

.faculty-interests {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
}

.interest-tag {
  font-size: 0.75rem;
  padding: 2px 8px;
  background: var(--vp-c-default-soft);
  border-radius: 4px;
  color: var(--vp-c-text-2);
}

.faculty-contact {
  font-size: 0.85rem;
  color: var(--vp-c-text-2);
}

.contact-item {
  display: inline-flex;
  align-items: center;
  gap: 0.25rem;
}

.card-arrow {
  position: absolute;
  right: 1rem;
  top: 50%;
  transform: translateY(-50%);
  color: var(--vp-c-text-3);
  opacity: 0;
  transition: all 0.3s ease;
}

.faculty-card:hover .card-arrow {
  opacity: 1;
  right: 0.75rem;
  color: var(--vp-c-brand-1);
}

.compact {
  padding: 1rem;
}

.compact .faculty-avatar {
  width: 60px;
  height: 60px;
}

.compact .faculty-name {
  font-size: 1rem;
}
</style>
