<script setup lang="ts">
import type { Student } from '../../types'

const props = defineProps<{
  student: Student
}>()

const degreeMap: Record<string, { label: string; class: string }> = {
  'phd': { label: '博士', class: 'phd' },
  'master': { label: '硕士', class: 'master' },
  'bachelor': { label: '本科', class: 'bachelor' }
}

const statusMap: Record<string, { label: string; class: string }> = {
  'enrolled': { label: '在读', class: 'enrolled' },
  'graduated': { label: '已毕业', class: 'graduated' }
}

function goToDetail() {
  window.location.href = `/team/student?id=${props.student.id}`
}
</script>

<template>
  <div class="student-card" @click="goToDetail">
    <div class="student-avatar">
      <div class="avatar-placeholder">
        {{ student.name.charAt(0) }}
      </div>
    </div>
    <div class="student-info">
      <h3 class="student-name">
        {{ student.name }}
        <span class="student-degree" :class="degreeMap[student.degree].class">
          {{ degreeMap[student.degree].label }}
        </span>
        <span class="student-status" :class="statusMap[student.status].class">
          {{ statusMap[student.status].label }}
        </span>
      </h3>
      <p class="student-year">{{ student.year }}级</p>
      <div class="student-interests" v-if="student.researchInterests?.length">
        <span 
          v-for="interest in student.researchInterests.slice(0, 2)" 
          :key="interest" 
          class="interest-tag"
        >
          {{ interest }}
        </span>
      </div>
      <p class="student-position" v-if="student.currentPosition">
        现任职：{{ student.currentPosition }}
      </p>
    </div>
    <div class="card-arrow">
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m9 18 6-6-6-6"/></svg>
    </div>
  </div>
</template>

<style scoped>
.student-card {
  display: flex;
  gap: 1rem;
  padding: 1rem;
  background: var(--vp-c-bg-soft);
  border-radius: 10px;
  transition: all 0.3s ease;
  border: 1px solid var(--vp-c-divider);
  cursor: pointer;
  position: relative;
}

.student-card:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  border-color: var(--vp-c-brand-1);
}

.student-avatar {
  flex-shrink: 0;
  width: 60px;
  height: 60px;
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea, #764ba2);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.25rem;
  font-weight: 600;
}

.student-info {
  flex: 1;
  min-width: 0;
}

.student-name {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin: 0 0 0.25rem;
  font-size: 1rem;
  flex-wrap: wrap;
}

.student-degree {
  font-size: 0.7rem;
  padding: 2px 6px;
  border-radius: 4px;
  font-weight: 500;
}

.student-degree.phd {
  background: #fce4ec;
  color: #c2185b;
}

.student-degree.master {
  background: #e8eaf6;
  color: #3949ab;
}

.student-degree.bachelor {
  background: #e8f5e9;
  color: #43a047;
}

.student-status {
  font-size: 0.7rem;
  padding: 2px 6px;
  border-radius: 4px;
  font-weight: 500;
}

.student-status.enrolled {
  background: #e3f2fd;
  color: #1976d2;
}

.student-status.graduated {
  background: #f3e5f5;
  color: #7b1fa2;
}

.student-year {
  color: var(--vp-c-text-2);
  font-size: 0.85rem;
  margin: 0 0 0.25rem;
}

.student-interests {
  display: flex;
  flex-wrap: wrap;
  gap: 0.25rem;
  margin-bottom: 0.25rem;
}

.interest-tag {
  font-size: 0.7rem;
  padding: 1px 6px;
  background: var(--vp-c-default-soft);
  border-radius: 3px;
  color: var(--vp-c-text-2);
}

.student-position {
  font-size: 0.8rem;
  color: var(--vp-c-text-2);
  margin: 0;
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

.student-card:hover .card-arrow {
  opacity: 1;
  right: 0.75rem;
  color: var(--vp-c-brand-1);
}
</style>
