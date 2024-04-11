<script setup lang="ts">
import {defineProps, onMounted, ref} from "vue";
import axios from "axios";
const props = defineProps({
  postId: {
    type: [Number, String],
    require: true,
  },
});

onMounted( () => {
  axios.get(`/api/posts/${props.postId}`).then((response) => {
    post.value = response.data;
  })
});

const post = ref( {
  id: 0,
  title: "",
  content: "",
});


</script>

<template>
  <h2>{{post.title}}</h2>
  <div>{{post.content}}</div>
</template>

<style scoped>

</style>