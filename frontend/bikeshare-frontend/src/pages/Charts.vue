<template>
    <AppHeader />
    <div class="charts-container">
        <div class="chart-item">
            <BarChart :names="names_top10_start" :amounts="amounts_top10_start" :legend="'Bike rented'" :title="'Top 10 Most Rented Stations'"></BarChart>
            <v-btn @click="loadTop10Start">Load</v-btn>
        </div>
        <div class="chart-item">
            <BarChart :names="names_top10_end" :amounts="amounts_top10_end" :legend="'Bike returned'" :title="'Top 10 Most Returned Stations'"></BarChart>
            <v-btn @click="loadTop10End">Load</v-btn>
        </div>
    </div>
    <div class="charts-container">
        <div class="chart-item">
            <PieChart :names="subscriber_type" :amounts="subscriber_type_amount" :legend="'Type'" :title="'Subscriber types of query trips'"></PieChart>
            <v-btn @click="loadSubscriber">Load</v-btn>
        </div>
        <div class="chart-item">
            <LineChart :names="duration" :amounts="duration_amount" :legend="'Amount'" :title="'Duration of query trips (below 70 minutes)'" :x_label="'Duration (minutes)'"></LineChart>
            <v-btn @click="loadDuration">Load</v-btn>
        </div>
    </div>
    <div class="charts-container">
        <div class="chart-item">
            <LineChart :names="time_period" :amounts="time_period_amount" :legend="'Amount'" :title="'Time period of query trips'" :x_label="'Time started in hour'"></LineChart>
            <v-btn @click="loadPeriod">Load</v-btn>
        </div>
    </div>
    <v-snackbar v-model="snackbar" :timeout="timeout">{{ text }}</v-snackbar>
</template>

<script setup>
import AppHeader from '@/components/AppHeader.vue';
import BarChart from '@/components/BarChart.vue';
import LineChart from '@/components/LineChart.vue';
import api from '@/config/api';
import { onMounted } from 'vue';
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
const time_period = ref([])
const time_period_amount = ref([])

const route = useRoute();
const params = route.query;

const snackbar = ref(false)
const text = ref('')
const timeout = ref(3000)
function openSnackbar(content) {
    text.value = content
    snackbar.value = true
}

function loadTop10Start() {
    getTop10Start()
}

function loadTop10End() {
    getTop10End()
}

function loadDuration() {
    getDuration()
}

function loadSubscriber() {
    getSubscriberType()
}

function loadPeriod() {
    getTimePeriod()
}

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
        if (response.data.charts.length === 0) {
            openSnackbar("No data found")
            return
        }
        names_top10_start.value = response.data.charts.map(chart => chart.name);
        amounts_top10_start.value = response.data.charts.map(chart => chart.amount);
    }).catch(error => {
        console.error('Error fetching data:', error)
    })
}

// Reserved for automatically loading the data:

// onMounted(() => {
//     getTop10Start()
//     getTop10End()
//     getSubscriberType()
//     getDuration()
//     getTimePeriod()
// })

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
        if (response.data.charts.length === 0) {
            openSnackbar("No data found")
            return
        }
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
        if (response.data.charts.length === 0) {
            openSnackbar("No data found")
            return
        }
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
        if (response.data.charts.length === 0) {
            openSnackbar("No data found")
            return
        }
        duration.value = response.data.charts.map(chart => chart.name);
        duration_amount.value = response.data.charts.map(chart => chart.amount);
    }).catch(error => {
        console.error('Error fetching data:', error)
    })
}

async function getTimePeriod() {
    await api.post('/chart/time_period', {
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
        if (response.data.charts.length === 0) {
            openSnackbar("No data found")
            return
        }
        time_period.value = response.data.charts.map(chart => chart.name);
        time_period_amount.value = response.data.charts.map(chart => chart.amount);
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
    min-width: 300px;
    max-width: 1000px;
    height: 750px;
    /* display: flex; */
    justify-content: center;
    align-items: center;
    border: 1px solid #ccc;
    border-radius: 8px;
    padding: 10px;
    background-color: #fff;
}
</style>