import DefaultTheme from 'vitepress/theme'
import { watch } from 'vue'
import './style/index.css'
import 'virtual:group-icons.css'
import "vitepress-markdown-timeline/dist/theme/index.css"

import Linkcard from './components/Linkcard.vue'
import HomeUnderline from './components/HomeUnderline.vue'
import ArticleMetadata from "./components/ArticleMetadata.vue"
import MyLayout from './components/MyLayout.vue'

import FacultyCard from './components/FacultyCard.vue'
import StudentCard from './components/StudentCard.vue'
import PaperCard from './components/PaperCard.vue'
import ProjectCard from './components/ProjectCard.vue'
import NewsCard from './components/NewsCard.vue'
import NewsDetail from './components/NewsDetail.vue'
import StatCard from './components/StatCard.vue'

export default {
  extends: DefaultTheme,
  Layout: MyLayout,
  enhanceApp({ app, router }: { app: any; router: any }) {
    app.component('Linkcard', Linkcard)
    app.component('HomeUnderline', HomeUnderline)
    app.component('ArticleMetadata', ArticleMetadata)
    
    app.component('FacultyCard', FacultyCard)
    app.component('StudentCard', StudentCard)
    app.component('PaperCard', PaperCard)
    app.component('ProjectCard', ProjectCard)
    app.component('NewsCard', NewsCard)
    app.component('NewsDetail', NewsDetail)
    app.component('StatCard', StatCard)

    if (typeof window !== 'undefined') {
      watch(
        () => router.route.data.relativePath,
        () => updateHomePageStyle(location.pathname === '/'),
        { immediate: true },
      )
    }
  },
}

function updateHomePageStyle(value: boolean) {
  let homePageStyle: HTMLStyleElement | undefined
  
  if (value) {
    if (homePageStyle) return

    homePageStyle = document.createElement('style')
    homePageStyle.innerHTML = `
    :root {
      animation: rainbow 12s linear infinite;
    }`
    document.body.appendChild(homePageStyle)
  } else {
    if (!homePageStyle) return

    homePageStyle.remove()
    homePageStyle = undefined
  }
}
