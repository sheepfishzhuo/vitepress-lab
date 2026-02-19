<template>
  <div class="rich-editor-wrapper">
    <div ref="editorContainer" class="editor-container"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch, nextTick } from 'vue'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:modelValue'])

const editorContainer = ref(null)
const isReady = ref(false)
let quillInstance = null
let isInternalChange = false
let pendingContent = null

function getGlobalQuillState() {
  if (!window.__quillState) {
    window.__quillState = {
      loaded: false,
      loading: false,
      callbacks: []
    }
  }
  return window.__quillState
}

async function ensureQuillLoaded() {
  const state = getGlobalQuillState()
  
  if (state.loaded && window.Quill) {
    return
  }
  
  if (state.loading) {
    return new Promise((resolve) => {
      state.callbacks.push(resolve)
    })
  }
  
  state.loading = true
  
  try {
    await loadStyle('https://cdn.quilljs.com/1.3.7/quill.snow.css', 'quill-css')
    await loadScript('https://cdn.quilljs.com/1.3.7/quill.min.js', 'quill-js')
    state.loaded = true
    state.callbacks.forEach(cb => cb())
    state.callbacks = []
  } catch (e) {
    console.error('Failed to load Quill:', e)
    state.loading = false
    throw e
  }
}

function loadScript(src, id) {
  return new Promise((resolve, reject) => {
    if (document.getElementById(id)) {
      resolve()
      return
    }
    const script = document.createElement('script')
    script.id = id
    script.src = src
    script.onload = resolve
    script.onerror = reject
    document.head.appendChild(script)
  })
}

function loadStyle(href, id) {
  return new Promise((resolve, reject) => {
    if (document.getElementById(id)) {
      resolve()
      return
    }
    const link = document.createElement('link')
    link.id = id
    link.rel = 'stylesheet'
    link.href = href
    link.onload = resolve
    link.onerror = reject
    document.head.appendChild(link)
  })
}

async function uploadImage(file) {
  const formData = new FormData()
  formData.append('file', file)
  
  const response = await fetch('http://localhost:8080/api/upload/image', {
    method: 'POST',
    body: formData
  })
  
  const data = await response.json()
  if (data.code === 200 && data.data) {
    return data.data
  }
  throw new Error(data.message || '上传失败')
}

function imageHandler() {
  const input = document.createElement('input')
  input.setAttribute('type', 'file')
  input.setAttribute('accept', 'image/*')
  input.click()
  
  input.onchange = async () => {
    const file = input.files[0]
    if (!file) return
    
    const editor = quillInstance
    const range = editor.getSelection(true)
    
    editor.insertEmbed(range.index, 'image', '上传中...')
    editor.setSelection(range.index + 1)
    
    try {
      const url = await uploadImage(file)
      editor.deleteText(range.index, 1)
      editor.insertEmbed(range.index, 'image', url)
      editor.setSelection(range.index + 1)
    } catch (error) {
      console.error('Image upload failed:', error)
      editor.deleteText(range.index, 1)
      alert('图片上传失败: ' + error.message)
    }
  }
}

async function initEditor() {
  try {
    await ensureQuillLoaded()
    await nextTick()
    
    createEditor()
    
    isReady.value = true
    
    if (pendingContent) {
      setContent(pendingContent)
      pendingContent = null
    } else if (props.modelValue) {
      setContent(props.modelValue)
    }
  } catch (e) {
    console.error('Failed to init editor:', e)
  }
}

function setContent(html) {
  if (!quillInstance) {
    pendingContent = html
    return
  }
  if (!html) return
  
  isInternalChange = true
  const delta = quillInstance.clipboard.convert(html)
  quillInstance.setContents(delta, 'silent')
  isInternalChange = false
}

function createEditor() {
  if (!editorContainer.value || !window.Quill) return

  try {
    const Quill = window.Quill
    
    quillInstance = new Quill(editorContainer.value, {
      theme: 'snow',
      placeholder: '请输入正文内容...',
      modules: {
        toolbar: {
          container: [
            [{ 'header': [1, 2, 3, false] }],
            ['bold', 'italic', 'underline', 'strike'],
            [{ 'color': [] }, { 'background': [] }],
            [{ 'list': 'ordered'}, { 'list': 'bullet' }],
            [{ 'align': [] }],
            ['link', 'image'],
            ['clean']
          ],
          handlers: {
            image: imageHandler
          }
        }
      }
    })

    quillInstance.on('text-change', () => {
      if (!isInternalChange && editorContainer.value) {
        const html = editorContainer.value.querySelector('.ql-editor').innerHTML
        emit('update:modelValue', html)
      }
    })
  } catch (e) {
    console.error('Failed to create Quill:', e)
  }
}

watch(() => props.modelValue, (newVal) => {
  if (isReady.value && quillInstance && editorContainer.value) {
    const currentHtml = editorContainer.value.querySelector('.ql-editor')?.innerHTML || ''
    const isEmpty = currentHtml === '<p><br></p>' || currentHtml === ''
    if ((newVal && newVal !== currentHtml && newVal !== '<p><br></p>') || (isEmpty && newVal)) {
      setContent(newVal)
    }
  }
})

onMounted(() => {
  initEditor()
})

onBeforeUnmount(() => {
  if (quillInstance) {
    quillInstance = null
  }
  isReady.value = false
  pendingContent = null
})
</script>

<style scoped>
.rich-editor-wrapper {
  border: 1px solid var(--vp-c-divider);
  border-radius: 6px;
  overflow: hidden;
}

.editor-container {
  min-height: 300px;
  background: #fff;
}

.editor-container :deep(.ql-toolbar) {
  border: none;
  border-bottom: 1px solid var(--vp-c-divider);
  background: #fafafa;
}

.editor-container :deep(.ql-container) {
  border: none;
  min-height: 260px;
  font-size: 14px;
}

.editor-container :deep(.ql-editor) {
  min-height: 260px;
  padding: 12px 15px;
}

.editor-container :deep(.ql-editor.ql-blank::before) {
  font-style: normal;
  color: #999;
}

.editor-container :deep(.ql-editor img) {
  max-width: 100%;
  height: auto;
  display: block;
}

.editor-container :deep(.ql-editor p img) {
  display: inline-block;
}

.editor-container :deep(.ql-align-center) {
  text-align: center;
}

.editor-container :deep(.ql-align-center img) {
  display: block;
  margin: 0 auto;
}

.editor-container :deep(.ql-align-right) {
  text-align: right;
}

.editor-container :deep(.ql-align-right img) {
  display: block;
  margin-left: auto;
}
</style>
