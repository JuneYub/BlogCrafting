<script setup lang="ts">
const posts = [
  {id : 1, title: "제목1", content : "내용1"},
  {id : 2, title: "제목2", content : "내용2"},
  {id : 3, title: "제목3", content : "내용3"},
  {id : 4, title: "제목4", content : "내용4"},
]

</script>

<template>
  <ul>
    <li v-for="post in posts" :key="post.id">
      <div>
        {{post.title}}
      </div>

      <div>
        {{post.content}}
      </div>
    </li>

  </ul>
</template>

<style>
  li {
    margin-bottom: 1rem;
  }

  li:last-child {
    margin-bottom: 0;
  }
</style>