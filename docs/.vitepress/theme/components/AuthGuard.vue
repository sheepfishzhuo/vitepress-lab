<script setup>
import { ref, onMounted } from 'vue'

const props = defineProps({
  password: {
    type: String,
    default: 'admin123'
  }
})

const isAuthenticated = ref(false)
const inputPassword = ref('')
const showError = ref(false)
const checking = ref(true)

onMounted(() => {
  const auth = sessionStorage.getItem('admin_auth')
  if (auth === 'authenticated') {
    isAuthenticated.value = true
  }
  checking.value = false
})

function handleLogin() {
  if (inputPassword.value === props.password) {
    isAuthenticated.value = true
    sessionStorage.setItem('admin_auth', 'authenticated')
    showError.value = false
  } else {
    showError.value = true
    inputPassword.value = ''
  }
}

function handleLogout() {
  isAuthenticated.value = false
  sessionStorage.removeItem('admin_auth')
}

function handleKeydown(e) {
  if (e.key === 'Enter') {
    handleLogin()
  }
}
</script>

<template>
  <div v-if="checking" class="auth-checking">
    <div class="spinner"></div>
  </div>
  
  <div v-else-if="!isAuthenticated" class="auth-container">
    <div class="auth-card">
      <div class="auth-icon">
        <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
      </div>
      <h2 class="auth-title">管理后台</h2>
      <p class="auth-desc">请输入管理密码以访问此页面</p>
      
      <div class="auth-form">
        <input 
          v-model="inputPassword"
          type="password"
          placeholder="请输入密码"
          class="auth-input"
          :class="{ error: showError }"
          @keydown="handleKeydown"
        />
        <button class="auth-btn" @click="handleLogin">
          验证
        </button>
      </div>
      
      <p v-if="showError" class="auth-error">密码错误，请重试</p>
    </div>
  </div>
  
  <template v-else>
    <div class="auth-header">
      <span class="auth-badge">
        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>
        已认证
      </span>
      <button class="logout-btn" @click="handleLogout">
        退出登录
      </button>
    </div>
    <slot></slot>
  </template>
</template>

<style scoped>
.auth-checking {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 300px;
}

.spinner {
  width: 32px;
  height: 32px;
  border: 3px solid var(--vp-c-divider);
  border-top-color: var(--vp-c-brand-1);
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.auth-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  padding: 2rem;
}

.auth-card {
  background: var(--vp-c-bg);
  border: 1px solid var(--vp-c-divider);
  border-radius: 16px;
  padding: 2.5rem;
  text-align: center;
  max-width: 360px;
  width: 100%;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.auth-icon {
  width: 64px;
  height: 64px;
  margin: 0 auto 1.5rem;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--vp-c-brand-soft) 0%, var(--vp-c-bg-soft) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--vp-c-brand-1);
}

.auth-title {
  font-size: 1.5rem;
  font-weight: 700;
  margin: 0 0 0.5rem;
  color: var(--vp-c-text-1);
}

.auth-desc {
  font-size: 0.9rem;
  color: var(--vp-c-text-2);
  margin: 0 0 1.5rem;
}

.auth-form {
  display: flex;
  gap: 0.5rem;
}

.auth-input {
  flex: 1;
  padding: 0.75rem 1rem;
  border: 1px solid var(--vp-c-divider);
  border-radius: 8px;
  font-size: 0.95rem;
  background: var(--vp-c-bg-soft);
  color: var(--vp-c-text-1);
  transition: all 0.2s ease;
}

.auth-input:focus {
  outline: none;
  border-color: var(--vp-c-brand-1);
}

.auth-input.error {
  border-color: #ef4444;
  animation: shake 0.3s ease;
}

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  25% { transform: translateX(-5px); }
  75% { transform: translateX(5px); }
}

.auth-btn {
  padding: 0.75rem 1.5rem;
  background: var(--vp-c-brand-1);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 0.95rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.auth-btn:hover {
  opacity: 0.9;
}

.auth-error {
  color: #ef4444;
  font-size: 0.85rem;
  margin: 0.75rem 0 0;
}

.auth-header {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 1rem;
  margin-bottom: 1rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid var(--vp-c-divider);
}

.auth-badge {
  display: inline-flex;
  align-items: center;
  gap: 0.35rem;
  font-size: 0.8rem;
  color: #22c55e;
  background: #dcfce7;
  padding: 0.35rem 0.75rem;
  border-radius: 20px;
}

.logout-btn {
  padding: 0.35rem 0.75rem;
  background: transparent;
  color: var(--vp-c-text-2);
  border: 1px solid var(--vp-c-divider);
  border-radius: 6px;
  font-size: 0.8rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.logout-btn:hover {
  border-color: #ef4444;
  color: #ef4444;
}
</style>
