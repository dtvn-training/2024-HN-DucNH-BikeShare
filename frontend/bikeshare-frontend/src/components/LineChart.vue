<template>
    <div class="chart-container">
        <Line :data="data1" :options="options" class="chart"/>
    </div>
</template>

<script setup>
import { Line } from 'vue-chartjs';
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend, scales } from 'chart.js'

ChartJS.register(CategoryScale, LinearScale, Title, Tooltip, Legend, PointElement, LineElement);

const props = defineProps({
    names: Array,
    amounts: Array,
    legend: String,
    title: String,
    x_label: String,
});

const data1 = computed(() => ({
    labels: props.names,
    datasets: [
        {
            label: props.legend,
            backgroundColor: '#dcb6f0',
            data: props.amounts,
            tension: 0
        }
    ]
}));

const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
        legend: {position: 'top',},
        title: {display: true, text: props.title},
    },
    scales: {
        x: {
            display: true,
            title: {
                display: true,
                text: props.x_label
            }
        }
    }
};
</script>

<style>
.chart-container {
    position: relative;
    width: 100%;
    height: 600px;
}
.chart {
    height: 100%;
}
</style>