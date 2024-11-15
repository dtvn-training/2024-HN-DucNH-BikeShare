<template>
    <AppHeader></AppHeader>
    <p> This is charts page</p>
    <div class="pie">
        <BarChart aria-label="Sales figures for the years 2022 to 2024"></BarChart>
    </div>
    <div class="pie">
        <PieChart></PieChart>
    </div>
</template>

<script>
import AppHeader from '@/components/AppHeader.vue';
import BarChart from '@/components/BarChart.vue';
import PieChart from '@/components/PieChart.vue';

</script>

<style>
    .pie {
        height: 20%;
        width: 50%;
    }
</style>