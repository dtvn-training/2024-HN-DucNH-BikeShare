<template>
    <AppHeader></AppHeader>

    <div class="container">
        <div class="col w-1">
            <div class="search-content">
                <div>
                    <h3 class="title">Search</h3>
                </div>
                <div class="search-section">
                    <v-number-input 
                        label="Trip ID"
                        control-variant="stacked"
                        v-model="param_trip_id">
                    </v-number-input>
                    <v-text-field
                        label="Subscriber type"
                        v-model="param_subscriber_type">
                    </v-text-field>
                    <v-number-input
                        label="Bike ID"
                        control-variant="stacked"
                        v-model="param_bike_id">
                    </v-number-input>
                    <v-text-field
                        label="Start station"
                        v-model="param_start_station_name">
                    </v-text-field>
                    <v-text-field
                        label="End station"
                        v-model="param_end_station_name">
                    </v-text-field>
                    
                    <p>Duration (in minutes)</p>
                    <div class="flex">
                        <v-number-input
                            label="" 
                            control-variant="stacked" 
                            max-width="150" 
                            :min="0"
                            v-model="param_min_duration">
                        </v-number-input>
                        <p style="margin: auto;">to</p>
                        <v-number-input
                            label=""
                            control-variant="stacked"
                            max-width="150"
                            :min="0"
                            v-model="param_max_duration">
                        </v-number-input>
                    </div>

                    <p>Start session</p>
                    
                    <v-date-input
                        label="Date start"
                        prepend-icon=""
                        v-model="date"
                    ></v-date-input>
                    <v-text-field
                    v-model="time"
                    :active="menu2"
                    :focus="menu2"
                    label="Time start"
                    :readonly="time_allow"
                    >
                        <v-menu
                            v-model="menu2"
                            :close-on-content-click="false"
                            activator="parent"
                            transition="scale-transition"
                        >
                            <v-time-picker
                                v-if="menu2"
                                v-model="time"
                                full-width
                                format="24hr"
                            ></v-time-picker>
                        </v-menu>
                    </v-text-field>
                
                </div>
                <div class="center">
                    <v-btn variant="outlined" class="btn" @click="getTrips">Search</v-btn>
                </div>
            </div>
        </div>

        <div class="col w-2">
            <div class="table-content">
                <div class="table-header">
                    <!-- <v-pagination length="5"></v-pagination> -->
                    <!-- <p v-if="size === 100">Showing first 100 results</p> -->
                    <div class="button-group">
                        <v-btn variant="outlined" class="btn" href="/charts">Charts</v-btn>
                        <v-btn variant="outlined" class="btn">Export</v-btn>
                    </div>
                </div>
                <v-data-table :headers="header" :items="trips">
                </v-data-table>
                <v-btn variant="outlined" class="btn" @click="loadMore">Load more</v-btn>
            </div>
        </div>
    </div>
</template>

<script setup>
import AppHeader from '@/components/AppHeader.vue';
import { VNumberInput } from 'vuetify/labs/VNumberInput'
import { VTimePicker } from 'vuetify/labs/VTimePicker'
import { VDateInput } from 'vuetify/labs/VDateInput'
import { onMounted, ref } from 'vue';
import api from '@/config/api';

const header = [
    { title: 'Trip ID', key: 'trip_id', sortable: false },
    { title: 'Subscriber type', key: 'subscriber_type', sortable: false },
    { title: 'Bike ID', key: 'bike_id', sortable: false },
    { title: 'Start', key: 'start_time', sortable: false },
    { title: 'Start station', key: 'start_station_name', sortable: false },
    { title: 'End station', key: 'end_station_name', sortable: false },
    { title: 'Duration (minutes)', key: 'duration_minutes', sortable: false },
]

// const trips = [
//     {
//         trip_id: 4098766,
//         subscriber_type: '24 Hour Walk Up Pass',
//         bike_id: 306,
//         start_time: '2015-03-12 22:22:59 UTC',
//         start_station_name: 'ACC - West & 12th Street',
//         end_station_name: 'ACC - West & 12th Street',
//         duration_minutes: 45
//     },
//     {
//         trip_id: 4098766,
//         subscriber_type: 'Lorem Ipsum',
//         bike_id: 306,
//         start_time: '2015-11-12 21:45:22 UTC',
//         start_station_name: 'ACC - West & 12th Street',
//         end_station_name: 'ACC - West & 12th Street',
//         duration_minutes: 11
//     },
//     {
//         trip_id: 4098766,
//         subscriber_type: '24 Hour Walk Up Pass',
//         bike_id: 306,
//         start_time: '2015-03-12 22:22:59 UTC',
//         start_station_name: 'ACC - West & 12th Street',
//         end_station_name: 'ACC - West & 12th Street',
//         duration_minutes: 45
//     },
//     {
//         trip_id: 4098766,
//         subscriber_type: '24 Hour Walk Up Pass',
//         bike_id: 306,
//         start_time: '2015-03-12 22:22:59 UTC',
//         start_station_name: 'ACC - West & 12th Street',
//         end_station_name: 'ACC - West & 12th Street',
//         duration_minutes: 45
//     },
// ]

const trips = ref()

const param_trip_id = ref()
const param_subscriber_type = ref()
const param_bike_id = ref()
const param_start_station_name = ref()
const param_end_station_name = ref()
const param_min_duration = ref()
const param_max_duration = ref()
const param_start_date = ref()
const param_start_time = ref()
const size = ref()
const offset = ref(0)

const date = ref(null)
const time = ref(null)
const menu2 = ref(false)
const time_allow = ref(false)

async function getTrips() {
    await api.get(`/trips`, {
        params: {
            
        }
    })
        .then((response) => {
            trips.value = response.data.trips
        })
        .catch((error) => {
            console.log(error)
        })
}

const additional = ref()

async function loadMore() {
    offset.value += 200;
    await api.get(`/trips`, {
        params: {
            offset: offset.value
        }
    })
        .then((response) => {
            response.data.trips.forEach((trip) => {
                trips.value.push(trip)
            })
        })
}

// onMounted(() => {
//     const test1 = ref(
//         [{
//             "trip_id": "26599763"
//         },
//         {
//             "trip_id": "26742903"
//         }]
//     )
//     const test2 = ref(
//         [{
//             "trip_id": "26599923"
//         },
//         {
//             "trip_id": "26701683"
//         }]
//     )

//     console.log(test1)

//     test2.value.forEach((element) => {
//         test1.value.push(element)
//     })

//     console.log(test1)

// })



</script>

<style>
    .container {
    display: flex;
    min-height: calc(100vh - 64px);
    gap: 20px;
    padding: 20px;
    }

    .col {
    display: flex;
    flex-direction: column;
    }

    .w-1 {
    width: 25%;
    background-color: #f5f5f5;
    border-radius: 8px;
    }

    .w-2 {
    width: 75%;
    background-color: #f5f5f5;
    border-radius: 8px;
    }

    .search-content, .table-content {
    display: flex;
    flex-direction: column;
    height: 100%;
    padding: 20px;
    }

    .title {
    text-align: center;
    margin-bottom: 20px;
    }

    .table-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    }

    .button-group {
    display: flex;
    gap: 10px;
    }

    .btn {
    margin: 10px;
    }

    .search-section {
    margin: 10px;
    flex-grow: 1;
    }

    .center {
    display: flex;
    justify-content: center;
    }
    
    .flex {
        display: flex;
    }
</style>