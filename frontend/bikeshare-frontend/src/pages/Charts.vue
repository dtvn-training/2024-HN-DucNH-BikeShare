<template>
    <AppHeader />
    <div class="charts-container">
        <div class="chart-item">
            <BarChart :names="names_top10_start" :amounts="amounts_top10_start" :legend="'Bike rented'" :title="'Top 10 Most Rented Stations'"></BarChart>
        </div>
        <div class="chart-item">
            <BarChart :names="names_top10_end" :amounts="amounts_top10_end" :legend="'Bike returned'" :title="'Top 10 Most Returned Stations'"></BarChart>
        </div>
    </div>
    <div class="buttons-container">
        <v-btn @click="getTop10Start">Get Top 10 Start Stations</v-btn>
        <v-btn @click="getTop10End">Get Top 10 End Stations</v-btn>
    </div>
</template>

<script setup>
import AppHeader from '@/components/AppHeader.vue';
import BarChart from '@/components/BarChart.vue';
import api from '@/config/api';

const chart_data = ref()
const names_top10_start = []
const amounts_top10_start = []
const names_top10_end = []
const amounts_top10_end = []

async function getTop10Start() {
    await api.post('/chart/top10start', {
        limit: 0,
        offset: 0,
        trip_id: "",
        subscriber_type: "",
        bike_id: "1",
        bike_type: "",
        start_station_name: "",
        end_station_name: "",
        min_duration: 22,
        max_duration: 45,
        min_start_time: "",
        max_start_time: "",
    }).then(response => {
        response.data.charts.map(chart => names_top10_start.push(chart.name));
        response.data.charts.map(chart => amounts_top10_start.push(chart.amount));
    }).catch(error => {
        console.error('Error fetching data:', error)
    })
}

async function getTop10End() {
    await api.post('/chart/top10end', {
        limit: 0,
        offset: 0,
        trip_id: "",
        subscriber_type: "",
        bike_id: "1",
        bike_type: "",
        start_station_name: "",
        end_station_name: "",
        min_duration: 11,
        max_duration: 75,
        min_start_time: "",
        max_start_time: "",
    }).then(response => {
        console.log(response.data)
        response.data.charts.map(chart => names_top10_end.push(chart.name));
        response.data.charts.map(chart => amounts_top10_end.push(chart.amount));
    }).catch(error => {
        console.error('Error fetching data:', error)
    })
}



</script>

<style scoped>
.pie {
    height: 20%;
    width: 50%;
}
.charts-container {
    display: flex;
    justify-content: space-around;
    gap: 20px;
    margin: 20px 0;
}

.chart-item {
    flex: 1;
    min-width: 700px;
    max-width: 1000px;
    height: 700px;
    display: flex;
    justify-content: center;
    align-items: center;
    border: 1px solid #ccc;
    border-radius: 8px;
    padding: 10px;
    background-color: #fff;
}

.buttons-container {
    display: flex;
    justify-content: center;
    gap: 10px;
    margin-top: 20px;
}
</style>