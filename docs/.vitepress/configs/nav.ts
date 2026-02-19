import type { DefaultTheme } from 'vitepress'

export const nav: DefaultTheme.Config['nav'] = [
  { text: '首页', link: '/' },
  { text: '团队介绍', link: '/team/' },
  {
    text: '研究成果',
    items: [
      { text: '学术论文', link: '/research/papers/' },
      { text: '科研项目', link: '/research/projects/' },
      { text: '学术会议', link: '/research/conferences/' },
      { text: '专利软著', link: '/research/patents/' },
      { text: '竞赛获奖', link: '/research/competitions/' },
      { text: '系统研发', link: '/research/sysdev/' },
    ],
  },
  { text: '新闻动态', link: '/news/' },
  { text: '加入我们', link: '/join/' },
]
