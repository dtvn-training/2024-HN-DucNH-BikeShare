<template>
    <div id="map">
        <iframe :src="source" width="650" height="450"></iframe>
    </div>
</template>

<script setup>
import { ref } from 'vue';

const props = defineProps({
    latitude: Number,
    longitude: Number
})

const source = ref()

onMounted(() => {
    source.value = `https://maps.google.com/maps?q=${props.latitude},${props.longitude}&hl=en;z=14&output=embed`
    console.log(source.value)
})

</script>

<style scoped>
    #map {
        width: 100%;
    }
</style>