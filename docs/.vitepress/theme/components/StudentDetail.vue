<template>
  <div v-if="loading" class="loading">
    <div class="loading-spinner"></div>
    <p>加载中...</p>
  </div>

  <div v-else-if="error" class="not-found">
    <div class="not-found-icon">😔</div>
    <p>{{ error }}</p>
    <a href="/team/" class="back-btn">返回团队介绍</a>
  </div>

  <div v-else-if="student" class="student-detail">
    <div class="student-header">
      <div class="header-bg"></div>
      <div class="header-content">
        <div class="student-avatar">
          <img v-if="student.avatar" :src="student.avatar" :alt="student.name" class="avatar-img" />
          <div v-else class="avatar-placeholder">{{ student.name?.charAt(0) || '?' }}</div>
        </div>
        <div class="student-info">
          <div class="student-name-wrapper">
            <h1 class="student-name">{{ student.name }}</h1>
            <span v-if="student.degree" class="student-degree">{{ degreeLabel[student.degree] || student.degree }}</span>
            <span v-if="student.status" class="student-status" :class="student.status">{{ statusLabel[student.status] || student.status }}</span>
          </div>
          <div class="student-meta">
            <span class="meta-item">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 10v6M2 10l10-5 10 5-10 5z"/><path d="M6 12v5c3 3 9 3 12 0v-5"/></svg>
              {{ student.year }}级
            </span>
            <span v-if="student.advisor" class="meta-item">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
              导师：{{ student.advisor }}
            </span>
            <span v-if="student.graduationYear" class="meta-item">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M8 2v4"/><path d="M16 2v4"/><rect width="18" height="18" x="3" y="4" rx="2"/><path d="M3 10h18"/></svg>
              {{ student.graduationYear }}年毕业
            </span>
            <span v-if="student.email" class="meta-item">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="20" height="16" x="2" y="4" rx="2"/><path d="m22 7-8.97 5.7a1.94 1.94 0 0 1-2.06 0L2 7"/></svg>
              <a :href="'mailto:' + student.email">{{ student.email }}</a>
            </span>
          </div>
          <div v-if="student.github || student.linkedin" class="student-links">
            <a v-if="student.github" :href="student.github" target="_blank" class="link-btn github">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="currentColor"><path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z"/></svg>
              GitHub
            </a>
            <a v-if="student.linkedin" :href="student.linkedin" target="_blank" class="link-btn linkedin">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="currentColor"><path d="M19 0h-14c-2.761 0-5 2.239-5 5v14c0 2.761 2.239 5 5 5h14c2.762 0 5-2.239 5-5v-14c0-2.761-2.238-5-5-5zm-11 19h-3v-11h3v11zm-1.5-12.268c-.966 0-1.75-.79-1.75-1.764s.784-1.764 1.75-1.764 1.75.79 1.75 1.764-.783 1.764-1.75 1.764zm13.5 12.268h-3v-5.604c0-3.368-4-3.113-4 0v5.604h-3v-11h3v1.765c1.396-2.586 7-2.777 7 2.476v6.759z"/></svg>
              LinkedIn
            </a>
          </div>
        </div>
      </div>
    </div>

    <div class="student-content">
      <div v-if="student.researchInterests && student.researchInterests.length" class="section">
        <div class="section-header">
          <div class="section-icon">🎯</div>
          <h2 class="section-title">研究方向</h2>
        </div>
        <div class="interest-tags">
          <span v-for="interest in student.researchInterests" :key="interest" class="interest-tag">
            {{ interest }}
          </span>
        </div>
      </div>

      <div v-if="student.currentPosition" class="section">
        <div class="section-header">
          <div class="section-icon">💼</div>
          <h2 class="section-title">当前职位</h2>
        </div>
        <p class="section-text">{{ student.currentPosition }}</p>
      </div>

      <div v-if="student.biography" class="section">
        <div class="section-header">
          <div class="section-icon">👤</div>
          <h2 class="section-title">个人简介</h2>
        </div>
        <div class="rich-content" v-html="student.biography"></div>
      </div>

      <div v-if="student.projects" class="section">
        <div class="section-header">
          <div class="section-icon">🔬</div>
          <h2 class="section-title">参与项目</h2>
        </div>
        <div class="rich-content" v-html="student.projects"></div>
      </div>

      <div v-if="student.awards" class="section">
        <div class="section-header">
          <div class="section-icon">🏆</div>
          <h2 class="section-title">获奖情况</h2>
        </div>
        <div class="rich-content" v-html="student.awards"></div>
      </div>
    </div>

    <div class="back-section">
      <a href="/team/" class="back-btn">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m12 19-7-7 7-7"/><path d="M19 12H5"/></svg>
        返回团队介绍
      </a>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const student = ref(null)
const loading = ref(true)
const error = ref(null)

function parseArray(str) {
  if (!str) return []
  if (Array.isArray(str)) return str
  return str.split(',').map(s => s.trim()).filter(Boolean)
}

onMounted(async () => {
  const params = new URLSearchParams(window.location.search)
  const studentId = params.get('id')
  
  if (!studentId) {
    error.value = '缺少学生ID参数'
    loading.value = false
    return
  }
  
  try {
    const response = await fetch(`http://localhost:8080/api/students/${studentId}`)
    const data = await response.json()
    
    if (data.code === 200 && data.data) {
      student.value = {
        ...data.data,
        researchInterests: parseArray(data.data.researchInterests)
      }
    } else {
      error.value = data.message || '未找到该学生信息'
    }
  } catch (e) {
    console.error('Failed to fetch student:', e)
    error.value = '加载失败，请检查网络连接'
  } finally {
    loading.value = false
  }
})

const degreeLabel = {
  'phd': '博士研究生',
  'master': '硕士研究生',
  'bachelor': '本科生'
}

const statusLabel = {
  'enrolled': '在读',
  'graduated': '已毕业'
}
</script>

<style scoped>
.loading {
  text-align: center;
  padding: 6rem 2rem;
  color: var(--vp-c-text-2);
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid var(--vp-c-divider);
  border-top-color: var(--vp-c-brand-1);
  border-radius: 50%;
  margin: 0 auto 1rem;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.not-found {
  text-align: center;
  padding: 6rem 2rem;
  color: var(--vp-c-text-2);
}

.not-found-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
}

.not-found p {
  font-size: 1.2rem;
  margin-bottom: 1.5rem;
}

.back-btn {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.5rem;
  background: var(--vp-c-brand-1);
  color: white;
  text-decoration: none;
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.2s;
}

.back-btn:hover {
  background: var(--vp-c-brand-2);
  transform: translateY(-2px);
}

.student-detail {
  margin-top: 1rem;
}

.student-header {
  position: relative;
  border-radius: 20px;
  overflow: hidden;
  margin-bottom: 2rem;
}

.header-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 120px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e8ecf1 100%);
}

.header-content {
  position: relative;
  display: flex;
  gap: 2rem;
  padding: 2rem;
  padding-top: 3rem;
}

.student-avatar {
  flex-shrink: 0;
}

.avatar-img {
  width: 140px;
  height: 140px;
  border-radius: 16px;
  object-fit: cover;
  border: 4px solid var(--vp-c-bg);
  box-shadow: 0 4px 20px rgba(0,0,0,0.15);
}

.avatar-placeholder {
  width: 140px;
  height: 140px;
  border-radius: 16px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 3rem;
  font-weight: 600;
  border: 4px solid var(--vp-c-bg);
  box-shadow: 0 4px 20px rgba(0,0,0,0.15);
}

.student-info {
  flex: 1;
  padding-top: 1rem;
}

.student-name-wrapper {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin: 0 0 1rem;
  flex-wrap: wrap;
}

.student-name-wrapper .student-name {
  margin: 0;
  font-size: 2rem;
  color: var(--vp-c-text-1) !important;
  text-shadow: none;
}

.student-degree {
  display: inline-block;
  font-size: 0.85rem;
  padding: 6px 14px;
  border-radius: 20px;
  font-weight: 500;
  background: linear-gradient(135deg, #e8eaf6, #c5cae9);
  color: #3949ab;
}

.student-status {
  display: inline-block;
  font-size: 0.85rem;
  padding: 6px 14px;
  border-radius: 20px;
  font-weight: 500;
}

.student-status.enrolled {
  background: linear-gradient(135deg, #e3f2fd, #bbdefb);
  color: #1976d2;
}

.student-status.graduated {
  background: linear-gradient(135deg, #e8f5e9, #c8e6c9);
  color: #388e3c;
}

.student-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 1.25rem;
  color: var(--vp-c-text-2);
  margin-bottom: 1rem;
}

.meta-item {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.95rem;
}

.meta-item svg {
  opacity: 0.6;
}

.meta-item a {
  color: var(--vp-c-brand-1);
  text-decoration: none;
}

.meta-item a:hover {
  text-decoration: underline;
}

.student-links {
  display: flex;
  gap: 0.5rem;
}

.link-btn {
  display: inline-flex;
  align-items: center;
  gap: 0.4rem;
  padding: 0.4rem 0.8rem;
  background: var(--vp-c-bg);
  border: 1px solid var(--vp-c-divider);
  border-radius: 6px;
  color: var(--vp-c-text-1);
  text-decoration: none;
  font-size: 0.85rem;
  transition: all 0.2s;
}

.link-btn:hover {
  transform: translateY(-1px);
}

.link-btn.github:hover { border-color: #333; color: #333; }
.link-btn.linkedin:hover { border-color: #0077b5; color: #0077b5; }

.student-content {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.section {
  background: var(--vp-c-bg-soft);
  border-radius: 16px;
  padding: 1.5rem;
  border: 1px solid var(--vp-c-divider);
  transition: all 0.2s;
}

.section:hover {
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
}

.section-header {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1rem;
  padding-bottom: 0.75rem;
  border-bottom: 1px solid var(--vp-c-divider);
}

.section-icon {
  font-size: 1.5rem;
}

.section-title {
  font-size: 1.2rem;
  margin: 0;
  color: var(--vp-c-text-1);
}

.interest-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.interest-tag {
  padding: 0.5rem 1rem;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 500;
}

.section-text {
  line-height: 1.8;
  color: var(--vp-c-text-2);
  margin: 0;
}

.rich-content {
  line-height: 1.8;
  color: var(--vp-c-text-2);
}

.rich-content :deep(p) {
  margin: 0 0 1rem;
}

.rich-content :deep(ul),
.rich-content :deep(ol) {
  margin: 0 0 1rem;
  padding-left: 1.5rem;
}

.rich-content :deep(li) {
  margin-bottom: 0.5rem;
}

.rich-content :deep(a) {
  color: var(--vp-c-brand-1);
  text-decoration: none;
}

.rich-content :deep(a:hover) {
  text-decoration: underline;
}

.rich-content :deep(strong) {
  color: var(--vp-c-text-1);
}

.back-section {
  margin-top: 2rem;
  padding-top: 2rem;
  border-top: 1px solid var(--vp-c-divider);
}

@media (max-width: 640px) {
  .header-content {
    flex-direction: column;
    align-items: center;
    text-align: center;
    padding-top: 4rem;
  }
  
  .avatar-img, .avatar-placeholder {
    width: 120px;
    height: 120px;
  }
  
  .student-meta {
    justify-content: center;
  }
  
  .student-links {
    justify-content: center;
  }
  
  .student-name {
    font-size: 1.5rem;
  }
}
</style>
