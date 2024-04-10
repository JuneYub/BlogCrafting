<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <header>

      <nav>
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/wrtie">글 작성</RouterLink>
      </nav>

  </header>

  <RouterView />
</template>

<style>
</style>