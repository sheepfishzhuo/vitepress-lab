import { defineConfig } from 'vitepress'
import { groupIconMdPlugin, groupIconVitePlugin } from 'vitepress-plugin-group-icons'
import { nav } from './configs'
import timeline from "vitepress-markdown-timeline"

const sidebar = [
  {
    text: '首页',
    link: '/'
  },
  {
    text: '团队介绍',
    link: '/team/'
  },
  {
    text: '研究成果',
    collapsed: false,
    items: [
      { text: '研究方向', link: '/research/' },
      { text: '学术论文', link: '/research/papers/' },
      { text: '科研项目', link: '/research/projects/' },
      { text: '学术会议', link: '/research/conferences/' },
      { text: '专利软著', link: '/research/patents/' },
      { text: '竞赛获奖', link: '/research/competitions/' },
      { text: '系统研发', link: '/research/sysdev/' },
    ]
  },
  {
    text: '新闻动态',
    link: '/news/'
  },
  {
    text: '加入我们',
    link: '/join/'
  },
  {
    text: '管理后台',
    collapsed: true,
    items: [
      { text: '概览', link: '/admin/' },
      { text: '配置管理', link: '/admin/config' },
      { text: '教师管理', link: '/admin/faculty' },
      { text: '学生管理', link: '/admin/students' },
      { text: '论文管理', link: '/admin/papers' },
      { text: '项目管理', link: '/admin/projects' },
      { text: '新闻管理', link: '/admin/news' },
      { text: '竞赛管理', link: '/admin/competitions' },
      { text: '会议管理', link: '/admin/conferences' },
      { text: '专利管理', link: '/admin/patents' }
    ]
  }
]

export default defineConfig({
  cleanUrls: true,
  lang: 'zh-CN',
  title: "人工智能与大数据实验室",
  description: "探索人工智能前沿，创造智能未来",
  head: [
    ['link', { rel: 'icon', href: '/logo.svg' }],
  ],
  lastUpdated: true,
  appearance: false,
  markdown: {
    lineNumbers: true,
    config(md) {
      md.use(groupIconMdPlugin)
      md.use(timeline)
      md.renderer.rules.heading_close = (tokens, idx, options, env, slf) => {
        let htmlResult = slf.renderToken(tokens, idx, options)
        if (tokens[idx].tag === 'h1') htmlResult += `<ArticleMetadata />`
        return htmlResult
      }
    },
  },
  vite: {
    plugins: [groupIconVitePlugin()]
  },
  themeConfig: {
    lastUpdated: {
      text: '上次更新时间',
      formatOptions: {
        dateStyle: 'short',
        timeStyle: 'medium'
      },
    },
    logo: '/logo.svg',
    nav: nav,
    sidebar: sidebar,
    outline: {
      level: [2, 4],
      label: '页面导航'
    },
    docFooter: {
      prev: '上一页',
      next: '下一页',
    },
    search: {
      provider: 'local',
      options: {
        locales: {
          root: {
            translations: {
              button: { buttonText: '搜索', buttonAriaLabel: '搜索' },
              modal: {
                displayDetails: '显示详细列表',
                resetButtonTitle: '重置搜索',
                backButtonTitle: '关闭搜索',
                noResultsText: '没有结果',
                footer: {
                  selectText: '选择',
                  selectKeyAriaLabel: '输入',
                  navigateText: '导航',
                  navigateUpKeyAriaLabel: '上箭头',
                  navigateDownKeyAriaLabel: '下箭头',
                  closeText: '关闭',
                  closeKeyAriaLabel: 'Esc'
                }
              }
            }
          }
        }
      }
    },
    socialLinks: [
      { icon: 'github', link: 'https://github.com/' },
    ],
    footer: {
      message: '人工智能与大数据实验室 | Artificial Intelligence and Big Data Laboratory',
      copyright: `Copyright © 2018-${new Date().getFullYear()} 人工智能与大数据实验室`,
    },
    darkModeSwitchLabel: '深浅模式',
    lightModeSwitchTitle: '切换到浅色模式',
    darkModeSwitchTitle: '切换到深色模式',
    sidebarMenuLabel: '导航',
    returnToTopLabel: '返回顶部',
    langMenuLabel: '切换语言',
    externalLinkIcon: true,
  }
})
