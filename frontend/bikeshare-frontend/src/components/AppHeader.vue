<template>
    <div>
        <h1 class="title">{{ title }}</h1>
    </div>
    <div>
        <v-btn variant="outlined" class="btn" href="/stations">Station</v-btn>
        <v-btn variant="outlined" class="btn" href="/trips">Trips</v-btn>

    </div>
</template>

<script setup>
import { ref } from 'vue';

const title = ref("Bike Share Management and Analysis")
</script>

<style>
    .title {
        text-align: center;
    }

    .btn {
        margin: 10px;
    }
</style>