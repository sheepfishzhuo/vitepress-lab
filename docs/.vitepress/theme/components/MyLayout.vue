<script setup lang="ts">
import DefaultTheme from 'vitepress/theme'
// 自定义返回顶部组件
import backtotop from "./backtotop.vue";
</script>

<template>
  <DefaultTheme.Layout v-bind="$attrs">
    <template #doc-footer-before>
      <backtotop />
    </template>
  </DefaultTheme.Layout>
</template>

<style scoped>
</style>