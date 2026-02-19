---
layout: doc
---

# 团队介绍

<script setup>
import FacultyCard from '/.vitepress/theme/components/FacultyCard.vue'
import StudentCard from '/.vitepress/theme/components/StudentCard.vue'
import { ref, onMounted } from 'vue'

const facultyData = ref([])
const currentStudents = ref([])
const alumni = ref([])
const loading = ref(true)

function parseArray(str) {
  if (!str) return []
  if (Array.isArray(str)) return str
  return str.split(',').map(s => s.trim()).filter(Boolean)
}

onMounted(async () => {
  try {
    const response = await fetch('http://localhost:8080/api/faculty')
    const data = await response.json()
    facultyData.value = (data.data || []).map(f => ({
      ...f,
      researchInterests: parseArray(f.researchInterests)
    }))
    
    const studentsResponse = await fetch('http://localhost:8080/api/students')
    const studentsData = await studentsResponse.json()
    const allStudents = (studentsData.data || []).map(s => ({
      ...s,
      researchInterests: parseArray(s.researchInterests)
    }))
    
    currentStudents.value = allStudents.filter(s => s.status === 'enrolled')
    alumni.value = allStudents.filter(s => s.status === 'graduated')
  } catch (error) {
    console.error('Failed to fetch data:', error)
  } finally {
    loading.value = false
  }
})
</script>

## 实验室简介

人工智能与大数据实验室（Artificial Intelligence and Big Data Laboratory）成立于2018年，隶属于计算机科学与技术系。实验室致力于人工智能与机器学习的前沿研究，主要研究方向包括：

- **深度学习**：神经网络架构设计、优化算法、高效训练方法
- **计算机视觉**：图像识别、目标检测、视频理解
- **自然语言处理**：大语言模型、文本理解、对话系统
- **多模态学习**：跨模态表示学习、视觉-语言理解与生成

实验室秉承"追求卓越、勇于创新"的理念，致力于培养具有国际视野和创新能力的高层次人才。

## 指导教师

<div v-if="loading" class="loading">加载中...</div>
<div v-else class="faculty-grid">
  <FacultyCard v-for="faculty in facultyData" :key="faculty.id" :faculty="faculty" />
</div>

## 在读学生

<div v-if="loading" class="loading">加载中...</div>
<div v-else class="students-grid">
  <StudentCard v-for="student in currentStudents" :key="student.id" :student="student" />
</div>

## 已毕业学生

<div v-if="loading" class="loading">加载中...</div>
<div v-else class="students-grid">
  <StudentCard v-for="student in alumni" :key="student.id" :student="student" />
</div>

<style>
.faculty-grid, .students-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 1rem;
  margin: 1.5rem 0;
}

.loading {
  text-align: center;
  padding: 2rem;
  color: var(--vp-c-text-2);
}
</style>
