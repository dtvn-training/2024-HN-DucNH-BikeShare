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
    <div class="charts-container">
        <div class="chart-item">
            <PieChart :names="subscriber_type" :amounts="subscriber_type_amount" :legend="'Type'" :title="'Subscriber types of query trips'"></PieChart>
        </div>
        <div class="chart-item">
            <LineChart :names="duration" :amounts="duration_amount" :legend="'Duration (minutes)'" :title="'Duration of query trips (below 70 minutes)'"></LineChart>
        </div>
    </div>
</template>

<script setup>
import AppHeader from '@/components/AppHeader.vue';
import BarChart from '@/components/BarChart.vue';
import LineChart from '@/components/LineChart.vue';
import api from '@/config/api';
import { onBeforeMount, onMounted } from 'vue';
import { useRoute } from 'vue-router';

const chart_data = ref()
const names_top10_start = ref([]);
const amounts_top10_start = ref([]);
const names_top10_end = ref([]);
const amounts_top10_end = ref([]);
const subscriber_type = ref([])
const subscriber_type_amount = ref([])
const duration = ref([])
const duration_amount = ref([])

const route = useRoute();
const params = route.query;

async function getTop10Start() {
    await api.post('/chart/top10start', {
        limit: 0,
        offset: 0,
        trip_id: params.trip_id,
        subscriber_type: params.subscriber_type,
        bike_id: params.bike_id,
        bike_type: params.bike_type,
        start_station_name: params.start_station_name,
        end_station_name: params.end_station_name,
        min_duration: params.min_duration,
        max_duration: params.max_duration,
        min_start_time: params.min_start_time,
        max_start_time: params.max_start_time,
    }).then(response => {
        names_top10_start.value = response.data.charts.map(chart => chart.name);
        amounts_top10_start.value = response.data.charts.map(chart => chart.amount);
    }).catch(error => {
        console.error('Error fetching data:', error)
    })
}

onMounted(() => {
    getTop10Start()
    getTop10End()
    getSubscriberType()
    getDuration()
})

async function getTop10End() {
    await api.post('/chart/top10end', {
        limit: 0,
        offset: 0,
        trip_id: params.trip_id,
        subscriber_type: params.subscriber_type,
        bike_id: params.bike_id,
        bike_type: params.bike_type,
        start_station_name: params.start_station_name,
        end_station_name: params.end_station_name,
        min_duration: params.min_duration,
        max_duration: params.max_duration,
        min_start_time: params.min_start_time,
        max_start_time: params.max_start_time,
    }).then(response => {
        console.log(response.data)
        names_top10_end.value = response.data.charts.map(chart => chart.name);
        amounts_top10_end.value = response.data.charts.map(chart => chart.amount);
    }).catch(error => {
        console.error('Error fetching data:', error)
    })
}

async function getSubscriberType() {
    await api.post('/chart/subscriber_type', {
        limit: 0,
        offset: 0,
        trip_id: params.trip_id,
        subscriber_type: params.subscriber_type,
        bike_id: params.bike_id,
        bike_type: params.bike_type,
        start_station_name: params.start_station_name,
        end_station_name: params.end_station_name,
        min_duration: params.min_duration,
        max_duration: params.max_duration,
        min_start_time: params.min_start_time,
        max_start_time: params.max_start_time,
    }).then(response => {
        console.log(response.data)
        subscriber_type.value = response.data.charts.map(chart => chart.name);
        subscriber_type_amount.value = response.data.charts.map(chart => chart.amount);
    }).catch(error => {
        console.error('Error fetching data:', error)
    })
}

async function getDuration() {
    await api.post('/chart/duration', {
        limit: 0,
        offset: 0,
        trip_id: params.trip_id,
        subscriber_type: params.subscriber_type,
        bike_id: params.bike_id,
        bike_type: params.bike_type,
        start_station_name: params.start_station_name,
        end_station_name: params.end_station_name,
        min_duration: params.min_duration,
        max_duration: params.max_duration,
        min_start_time: params.min_start_time,
        max_start_time: params.max_start_time,
    }).then(response => {
        console.log(response.data)
        duration.value = response.data.charts.map(chart => chart.name);
        duration_amount.value = response.data.charts.map(chart => chart.amount);
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
</style>