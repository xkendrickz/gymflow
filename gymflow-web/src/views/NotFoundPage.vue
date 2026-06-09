<template>
    <div class="d-flex flex-column align-center justify-center" style="height: 100vh;">
        <h1 class="text-h1 font-weight-bold text-indigo">404</h1>
        <p class="text-h6 text-medium-emphasis mb-6">Page not found</p>
        <v-btn color="indigo" :to="{ name: 'login' }">Go Back Home</v-btn>
    </div>
</template>

<script setup lang="ts">
</script>