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

  <div v-else-if="faculty" class="faculty-detail">
    <div class="faculty-header">
      <div class="header-bg"></div>
      <div class="header-content">
        <div class="faculty-avatar">
          <img v-if="faculty.avatar" :src="faculty.avatar" :alt="faculty.name" class="avatar-img" />
          <div v-else class="avatar-placeholder">{{ faculty.name?.charAt(0) || '?' }}</div>
        </div>
        <div class="faculty-info">
          <div class="faculty-name-wrapper">
            <h1 class="faculty-name">{{ faculty.name }}</h1>
            <span v-if="faculty.title" class="faculty-title">{{ faculty.title }}</span>
            <span v-if="faculty.role" class="faculty-role">{{ faculty.role }}</span>
          </div>
          <div class="faculty-contact">
            <span v-if="faculty.email" class="contact-item">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="20" height="16" x="2" y="4" rx="2"/><path d="m22 7-8.97 5.7a1.94 1.94 0 0 1-2.06 0L2 7"/></svg>
              <a :href="'mailto:' + faculty.email">{{ faculty.email }}</a>
            </span>
            <span v-if="faculty.phone" class="contact-item">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"/></svg>
              {{ faculty.phone }}
            </span>
            <span v-if="faculty.office" class="contact-item">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M20 10c0 6-8 12-8 12s-8-6-8-12a8 8 0 0 1 16 0Z"/><circle cx="12" cy="10" r="3"/></svg>
              {{ faculty.office }}
            </span>
          </div>
          <div v-if="hasLinks" class="faculty-links">
            <a v-if="faculty.googleScholar" :href="faculty.googleScholar" target="_blank" class="link-btn scholar">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="currentColor"><path d="M12 24a7 7 0 1 1 0-14 7 7 0 0 1 0 14zm0-24L0 9.5l4.838 3.94A8 8 0 0 1 12 9a8 8 0 0 1 7.162 4.44L24 9.5z"/></svg>
              Google Scholar
            </a>
            <a v-if="faculty.researchGate" :href="faculty.researchGate" target="_blank" class="link-btn researchgate">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="currentColor"><path d="M19.586 0c-.818 0-1.508.19-2.073.565-.563.377-.97.936-1.213 1.68a3.193 3.193 0 0 0-.112.437 8.365 8.365 0 0 0-.078.53 9 9 0 0 0-.05.727c-.01.282-.013.621-.013 1.016a31.121 31.121 0 0 0 .014 1.017 9 9 0 0 0 .05.727 7.946 7.946 0 0 0 .078.53 3.193 3.193 0 0 0 .112.437c.244.744.65 1.303 1.213 1.68.565.376 1.256.564 2.073.564.271 0 .546-.021.822-.063.277-.043.556-.113.836-.207v-.92a3.24 3.24 0 0 1-.748.237 4.068 4.068 0 0 1-.748.07c-.547 0-1.012-.18-1.394-.538-.382-.359-.6-.878-.656-1.557a9.452 9.452 0 0 1-.028-.727v-.85h3.574V6.18h-3.574v-.85c0-.254.01-.5.028-.738.056-.68.274-1.198.656-1.557.382-.358.847-.537 1.394-.537.24 0 .492.024.748.07.257.047.508.12.748.22V1.87a4.02 4.02 0 0 0-.836-.207A5.57 5.57 0 0 0 19.586 0zM0 1.635v1.408h2.89v8.394h1.486V3.043h2.89V1.635H0zm7.82 1.408v1.408h2.89v6.986h1.486V4.451h2.89V3.043H7.82z"/></svg>
              ResearchGate
            </a>
            <a v-if="faculty.orcid" :href="faculty.orcid" target="_blank" class="link-btn orcid">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="currentColor"><path d="M12 0C5.372 0 0 5.372 0 12s5.372 12 12 12 12-5.372 12-12S18.628 0 12 0zM7.369 4.378c.525 0 .947.431.947.947s-.422.947-.947.947a.95.95 0 0 1-.947-.947c0-.525.422-.947.947-.947zm-.722 3.038h1.444v10.041H6.647V7.416zm3.562 0h3.9c3.712 0 5.344 2.653 5.344 5.025 0 2.578-2.016 5.025-5.325 5.025h-3.919V7.416zm1.444 1.303v7.444h2.297c3.272 0 4.022-2.484 4.022-3.722 0-2.016-1.284-3.722-4.097-3.722h-2.222z"/></svg>
              ORCID
            </a>
            <a v-if="faculty.personal" :href="faculty.personal" target="_blank" class="link-btn personal">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="2" y1="12" x2="22" y2="12"/><path d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z"/></svg>
              个人主页
            </a>
            <a v-if="faculty.github" :href="faculty.github" target="_blank" class="link-btn github">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="currentColor"><path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z"/></svg>
              GitHub
            </a>
          </div>
        </div>
      </div>
    </div>

    <div class="faculty-content">
      <div v-if="faculty.researchInterests && faculty.researchInterests.length" class="section">
        <div class="section-header">
          <div class="section-icon">🎯</div>
          <h2 class="section-title">研究方向</h2>
        </div>
        <div class="interest-tags">
          <span v-for="interest in faculty.researchInterests" :key="interest" class="interest-tag">
            {{ interest }}
          </span>
        </div>
      </div>

      <div v-if="faculty.biography" class="section">
        <div class="section-header">
          <div class="section-icon">👤</div>
          <h2 class="section-title">个人简介</h2>
        </div>
        <div class="rich-content" v-html="faculty.biography"></div>
      </div>

      <div v-if="faculty.education" class="section">
        <div class="section-header">
          <div class="section-icon">🎓</div>
          <h2 class="section-title">教育经历</h2>
        </div>
        <div class="rich-content" v-html="faculty.education"></div>
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
import { ref, onMounted, computed } from 'vue'

const faculty = ref(null)
const loading = ref(true)
const error = ref(null)

function parseArray(str) {
  if (!str) return []
  if (Array.isArray(str)) return str
  return str.split(',').map(s => s.trim()).filter(Boolean)
}

const hasLinks = computed(() => {
  if (!faculty.value) return false
  return faculty.value.googleScholar || faculty.value.researchGate || 
         faculty.value.orcid || faculty.value.personal || faculty.value.github
})

onMounted(async () => {
  const params = new URLSearchParams(window.location.search)
  const facultyId = params.get('id')
  
  if (!facultyId) {
    error.value = '缺少教师ID参数'
    loading.value = false
    return
  }
  
  try {
    const response = await fetch(`http://localhost:8080/api/faculty/${facultyId}`)
    const data = await response.json()
    
    if (data.code === 200 && data.data) {
      faculty.value = {
        ...data.data,
        researchInterests: parseArray(data.data.researchInterests)
      }
    } else {
      error.value = data.message || '未找到该教师信息'
    }
  } catch (e) {
    console.error('Failed to fetch faculty:', e)
    error.value = '加载失败，请检查网络连接'
  } finally {
    loading.value = false
  }
})
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

.faculty-detail {
  margin-top: 1rem;
}

.faculty-header {
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

.faculty-avatar {
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
  background: linear-gradient(135deg, var(--vp-c-brand-1), var(--vp-c-brand-2));
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 3rem;
  font-weight: 600;
  border: 4px solid var(--vp-c-bg);
  box-shadow: 0 4px 20px rgba(0,0,0,0.15);
}

.faculty-info {
  flex: 1;
  padding-top: 1rem;
}

.faculty-name-wrapper {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin: 0 0 1rem;
  flex-wrap: wrap;
}

.faculty-name {
  margin: 0;
  font-size: 2rem;
  color: var(--vp-c-text-1) !important;
  text-shadow: none;
}

.faculty-title {
  display: inline-block;
  font-size: 0.85rem;
  padding: 6px 14px;
  border-radius: 20px;
  font-weight: 500;
  background: linear-gradient(135deg, #e8f5e9, #c8e6c9);
  color: #2e7d32;
}

.faculty-role {
  display: inline-block;
  font-size: 0.85rem;
  padding: 6px 14px;
  border-radius: 20px;
  background: linear-gradient(135deg, #fce4ec, #f8bbd9);
  color: #c2185b;
  font-weight: 500;
}

.faculty-contact {
  display: flex;
  flex-wrap: wrap;
  gap: 1.25rem;
  margin-bottom: 1rem;
}

.contact-item {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  color: var(--vp-c-text-2);
  font-size: 0.95rem;
}

.contact-item svg {
  opacity: 0.6;
}

.contact-item a {
  color: var(--vp-c-brand-1);
  text-decoration: none;
}

.contact-item a:hover {
  text-decoration: underline;
}

.faculty-links {
  display: flex;
  flex-wrap: wrap;
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
  border-color: var(--vp-c-brand-1);
  color: var(--vp-c-brand-1);
  transform: translateY(-1px);
}

.link-btn.scholar:hover { border-color: #4285f4; color: #4285f4; }
.link-btn.researchgate:hover { border-color: #00CCBB; color: #00CCBB; }
.link-btn.orcid:hover { border-color: #A6CE39; color: #A6CE39; }
.link-btn.github:hover { border-color: #333; color: #333; }

.faculty-content {
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
  background: linear-gradient(135deg, var(--vp-c-brand-soft), var(--vp-c-brand-1));
  color: white;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 500;
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
  
  .faculty-contact {
    justify-content: center;
  }
  
  .faculty-links {
    justify-content: center;
  }
  
  .faculty-name {
    font-size: 1.5rem;
  }
}
</style>
