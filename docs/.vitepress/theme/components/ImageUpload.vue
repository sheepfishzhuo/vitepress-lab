<template>
  <div class="image-upload">
    <div v-if="!modelValue" class="upload-area" @click="triggerUpload">
      <input
        ref="fileInput"
        type="file"
        accept="image/*"
        style="display: none"
        @change="handleFileChange"
      />
      <div v-if="uploading" class="uploading">
        <div class="spinner"></div>
        <span>上传中...</span>
      </div>
      <div v-else class="upload-placeholder">
        <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
          <circle cx="8.5" cy="8.5" r="1.5"/>
          <polyline points="21 15 16 10 5 21"/>
        </svg>
        <span>点击上传图片</span>
      </div>
    </div>
    <div v-else class="preview-area">
      <img :src="modelValue" alt="预览图" />
      <div class="preview-actions">
        <button type="button" class="btn-replace" @click="triggerUpload">更换</button>
        <button type="button" class="btn-remove" @click="removeImage">删除</button>
      </div>
      <input
        ref="fileInput"
        type="file"
        accept="image/*"
        style="display: none"
        @change="handleFileChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:modelValue'])

const fileInput = ref(null)
const uploading = ref(false)

function triggerUpload() {
  fileInput.value?.click()
}

async function handleFileChange(event) {
  const file = event.target.files?.[0]
  if (!file) return
  
  uploading.value = true
  
  try {
    const formData = new FormData()
    formData.append('file', file)
    
    const response = await fetch('http://localhost:8080/api/upload/image', {
      method: 'POST',
      body: formData
    })
    
    const data = await response.json()
    
    if (data.code === 200 && data.data) {
      emit('update:modelValue', data.data)
    } else {
      alert(data.message || '上传失败')
    }
  } catch (error) {
    console.error('Upload error:', error)
    alert('上传失败，请检查网络连接')
  } finally {
    uploading.value = false
    if (fileInput.value) {
      fileInput.value.value = ''
    }
  }
}

function removeImage() {
  emit('update:modelValue', '')
}
</script>

<style scoped>
.image-upload {
  width: 100%;
}

.upload-area {
  border: 2px dashed var(--vp-c-divider);
  border-radius: 8px;
  padding: 2rem;
  text-align: center;
  cursor: pointer;
  transition: all 0.2s ease;
  background: var(--vp-c-bg-soft);
}

.upload-area:hover {
  border-color: var(--vp-c-brand-1);
  background: var(--vp-c-bg);
}

.upload-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  color: var(--vp-c-text-2);
}

.upload-placeholder svg {
  color: var(--vp-c-text-3);
}

.uploading {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  color: var(--vp-c-brand-1);
}

.spinner {
  width: 24px;
  height: 24px;
  border: 2px solid var(--vp-c-divider);
  border-top-color: var(--vp-c-brand-1);
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.preview-area {
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid var(--vp-c-divider);
}

.preview-area img {
  width: 100%;
  max-height: 300px;
  object-fit: contain;
  display: block;
  background: #f5f5f5;
}

.preview-actions {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  background: rgba(0, 0, 0, 0.5);
  opacity: 0;
  transition: opacity 0.2s ease;
}

.preview-area:hover .preview-actions {
  opacity: 1;
}

.btn-replace,
.btn-remove {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.85rem;
  transition: all 0.2s ease;
}

.btn-replace {
  background: white;
  color: #333;
}

.btn-replace:hover {
  background: #f0f0f0;
}

.btn-remove {
  background: #ff4d4f;
  color: white;
}

.btn-remove:hover {
  background: #ff7875;
}
</style>
