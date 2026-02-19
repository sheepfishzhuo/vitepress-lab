const API_BASE_URL = 'http://localhost:8080/api'

export async function fetchApi(endpoint: string) {
  const response = await fetch(`${API_BASE_URL}${endpoint}`)
  if (!response.ok) {
    throw new Error(`API Error: ${response.status}`)
  }
  const data = await response.json()
  return data.data
}

export const api = {
  faculty: {
    getAll: () => fetchApi('/faculty'),
    getById: (id: string) => fetchApi(`/faculty/${id}`)
  },
  students: {
    getAll: () => fetchApi('/students'),
    getByStatus: (status: string) => fetchApi(`/students/status/${status}`)
  },
  papers: {
    getAll: () => fetchApi('/papers'),
    getByYear: (year: number) => fetchApi(`/papers/year/${year}`)
  },
  projects: {
    getAll: () => fetchApi('/projects')
  },
  news: {
    getAll: () => fetchApi('/news'),
    getById: (id: string) => fetchApi(`/news/${id}`)
  },
  competitions: {
    getAll: () => fetchApi('/competitions')
  },
  conferences: {
    getAll: () => fetchApi('/conferences')
  },
  patents: {
    getAll: () => fetchApi('/patents')
  }
}
