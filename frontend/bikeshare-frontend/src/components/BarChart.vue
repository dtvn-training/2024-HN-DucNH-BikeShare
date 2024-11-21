<template>
    <div>
        <Bar :data="data1" :options="options" />
    </div>
</template>

<script setup>
import { Bar } from 'vue-chartjs'
import { Chart as ChartJS, BarElement, CategoryScale, LinearScale, Title, Tooltip, Legend } from 'chart.js'
import { computed } from 'vue';

ChartJS.register(BarElement, CategoryScale, LinearScale, Title, Tooltip, Legend)

const props = defineProps({
    names: Array,
    amounts: Array,
    legend: String,
    title: String
})


const data1 = computed(() => ({
    labels: props.names,
    datasets: [
        {
            label: props.legend,
            backgroundColor: '#f87979',
            data: props.amounts
        }
    ]
}))

const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
        legend: {position: 'top',},
        title: {display: true, text: props.title},
    }
}
</script>

<style scoped>
div {
  height: 700px;
  width: 100%;
}
</style>
