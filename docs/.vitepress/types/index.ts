export interface Faculty {
  id: string
  name: string
  title: string
  role?: string
  avatar?: string
  email?: string
  phone?: string
  office?: string
  researchInterests: string[]
  education: Education[]
  publications?: string[]
  projects?: string[]
  awards?: string[]
  biography?: string
  links?: {
    googleScholar?: string
    researchGate?: string
    orcid?: string
    personal?: string
    github?: string
  }
}

export interface Education {
  degree: string
  institution: string
  year: string
  field?: string
}

export interface Student {
  id: string
  name: string
  avatar?: string
  degree: 'bachelor' | 'master' | 'phd'
  advisor?: string
  year: number
  researchInterests?: string[]
  projects?: string[]
  awards?: string[]
  email?: string
  biography?: string
  status: 'enrolled' | 'graduated'
  graduationYear?: number
  currentPosition?: string
  linkedin?: string
  github?: string
}

export interface Paper {
  id: string
  title: string
  authors: string[]
  venue: string
  year: number
  type: 'journal' | 'conference' | 'workshop' | 'preprint'
  doi?: string
  abstract?: string
  keywords?: string[]
  pdf?: string
  code?: string
  citations?: number
  impactFactor?: number
}

export interface Project {
  id: string
  title: string
  principalInvestigator: string
  members?: string[]
  funding: string
  amount?: string
  startDate: string
  endDate?: string
  status: 'ongoing' | 'completed'
  description?: string
  outcomes?: string[]
}

export interface Competition {
  id: string
  name: string
  level: 'national' | 'provincial' | 'international'
  award: string
  year: number
  participants: string[]
  advisors?: string[]
  description?: string
}

export interface Conference {
  id: string
  name: string
  location: string
  date: string
  type: 'organize' | 'present' | 'attend'
  participants?: string[]
  description?: string
  link?: string
}

export interface Patent {
  id: string
  title: string
  type: 'invention' | 'utility' | 'software'
  inventors: string[]
  patentNo?: string
  status: 'pending' | 'granted'
  date?: string
  description?: string
}

export interface NewsItem {
  id: string
  title: string
  date: string
  category: 'research' | 'award' | 'event' | 'announcement'
  summary?: string
  content?: string
  image?: string
  author?: string
}
