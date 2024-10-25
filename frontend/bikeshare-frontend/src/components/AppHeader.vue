<template>
    <div>
        <h1 class="title">{{ title }}</h1>
    </div>
    <div>
        <v-btn variant="outlined" class="btn" href="/stations">Stations</v-btn>
        <v-btn variant="outlined" class="btn" href="/trips">Trips</v-btn>
    </div>
    <div>
        <h2 class="title">{{ route.name.substring(1) }}</h2>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRoute } from 'vue-router';

const title = ref("Bike Share Management and Analysis")
const route = useRoute()
const currentScreen = ref("")

</script>

<style>
    .title {
        text-align: center;
    }

    .btn {
        margin: 10px;
    }
</style>