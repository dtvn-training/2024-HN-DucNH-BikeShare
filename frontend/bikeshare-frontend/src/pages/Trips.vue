<template>
    <AppHeader></AppHeader>

    <div class="container">
        <div class="col w-1">
            <div class="search-content">
                <div>
                    <h3 class="title">Search</h3>
                </div>
                <div class="search-section">
                    <v-text-field 
                        label="Trip ID"
                        control-variant="stacked"
                        v-model="param_trip_id">
                    </v-text-field>
                    <v-text-field
                        label="Subscriber type"
                        v-model="param_subscriber_type">
                    </v-text-field>
                    <v-radio-group inline label="Bike type" v-model="param_bike_type">
                        <v-radio label="Any" value=""></v-radio>
                        <v-radio label="Classic" value="classic"></v-radio>
                        <v-radio label="Electric" value="electric"></v-radio>
                    </v-radio-group>
                    <v-text-field 
                        label="Bike ID"
                        control-variant="stacked"
                        v-model="param_bike_id">
                    </v-text-field>
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

                    <p>Session start time from</p>
                    <v-text-field type="datetime-local" v-model="param_min_start_time"></v-text-field>
                    <p>to</p>
                    <v-text-field type="datetime-local" v-model="param_max_start_time"></v-text-field>
                
                </div>
                <div class="center">
                    <v-btn variant="outlined" class="btn" @click="getTrips">Search</v-btn>
                </div>
            </div>
        </div>

        <div class="col w-2">
            <div class="table-content">
                <div class="table-header">
                    <div class="button-group">
                        <v-btn variant="outlined" class="btn" @click="navigateToChart">Charts</v-btn>
                        <v-btn variant="outlined" class="btn" @click="exportFullResult">Export</v-btn>
                    </div>
                </div>
                <v-data-table :headers="header" :items="trips">
                </v-data-table>
                <v-btn variant="outlined" class="btn" @click="loadMore" :disabled="disable">Load more</v-btn>
            </div>
        </div>
    </div>

    <v-snackbar v-model="snackbar" :timeout="timeout">{{ text }}</v-snackbar>
</template>

<script setup>
import AppHeader from '@/components/AppHeader.vue';
import { VNumberInput } from 'vuetify/labs/VNumberInput'
import { ref } from 'vue';
import api from '@/config/api';
import { useRouter } from 'vue-router'
const router = useRouter()

const OFFSET = 400
const table = ref('trips')

const header = [
    { title: 'Trip ID', key: 'trip_id', sortable: false },
    { title: 'Subscriber type', key: 'subscriber_type', sortable: false },
    { title: 'Bike ID', key: 'bike_id', sortable: false },
    { title: 'Bike type', key: 'bike_type', sortable: false },
    { title: 'Start time (UTC)', key: 'start_time', sortable: false },
    { title: 'Start station', key: 'start_station_name', sortable: false },
    { title: 'End station', key: 'end_station_name', sortable: false },
    { title: 'Duration (minutes)', key: 'duration_minutes', sortable: false },
]

const trips = ref()

const param_trip_id = ref("")
const param_subscriber_type = ref("")
const param_bike_id = ref("")
const param_bike_type = ref("")
const param_start_station_name = ref("")
const param_end_station_name = ref("")
const param_min_duration = ref(0)
const param_max_duration = ref(0)
const param_min_start_time = ref("")
const param_max_start_time = ref("")
const offset = ref(0)

const disable = ref(true)
const snackbar = ref(false)
const text = ref()
const timeout = ref(2000)

function openSnackbar(content) {
    text.value = content
    snackbar.value = true
}


// Query trips info with limited number of rows
async function getTrips() {
    offset.value = 0
    openSnackbar("Querying...")
    await api.post(`/trips`, {
        limit: OFFSET,
        offset: offset.value,
        trip_id: param_trip_id.value,
        subscriber_type: param_subscriber_type.value,
        bike_id: param_bike_id.value,
        bike_type: param_bike_type.value,
        start_station_name: param_start_station_name.value,
        end_station_name: param_end_station_name.value,
        min_duration: param_min_duration.value,
        max_duration: param_max_duration.value,
        min_start_time: param_min_start_time.value,
        max_start_time: param_max_start_time.value,
    })
        .then((response) => {
            trips.value = response.data.trips
            json.value = JSON.stringify(response.data)
            openSnackbar("Query completed")

            if(trips.value.length % OFFSET != 0 || trips.value.length == 0) {
                disable.value = true
            } else disable.value = false
        })
        .catch((error) => {
            console.log(error)
        })
}

// Load more same amount of rows as first query
async function loadMore() {
    offset.value += OFFSET;
    await api.post(`/trips`, {
        limit: OFFSET,
        offset: offset.value,
        trip_id: param_trip_id.value,
        subscriber_type: param_subscriber_type.value,
        bike_id: param_bike_id.value,
        bike_type: param_bike_type.value,
        start_station_name: param_start_station_name.value,
        end_station_name: param_end_station_name.value,
        min_duration: param_min_duration.value,
        max_duration: param_max_duration.value,
        min_start_time: param_min_start_time.value,
        max_start_time: param_max_start_time.value,
    })
        .then((response) => {
            response.data.trips.forEach((trip) => {
                trips.value.push(trip)
            })
            
            if(trips.value.length % OFFSET != 0) {
                disable.value = true
            }

            openSnackbar("Loaded more rows")
        })
}

const json = ref()

// Export all trips (no limited number of rows)
async function exportFullResult() {
    openSnackbar("Exporting...")
    try {
        const response = await api.post(`/export/trips/getAll`, {
            limit: 0,
            offset: 0,
            trip_id: param_trip_id.value,
            subscriber_type: param_subscriber_type.value,
            bike_id: param_bike_id.value,
            bike_type: param_bike_type.value,
            start_station_name: param_start_station_name.value,
            end_station_name: param_end_station_name.value,
            min_duration: param_min_duration.value,
            max_duration: param_max_duration.value,
            min_start_time: param_min_start_time.value,
            max_start_time: param_max_start_time.value,
        }, {
            responseType: 'blob',
            headers: {
                'Content-Type': 'application/json'
            }
        })

        const blob = new Blob([response.data], {
            type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        });

        const date = new Date();
        const name = `Trips_${date.getFullYear()}_${date.getMonth() + 1}_${date.getDate()}_${date.getHours()}_${date.getMinutes()}_${date.getSeconds()}.xlsx`;

        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', name);
        document.body.appendChild(link);
        link.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(link);
    } catch (error) {
        console.error('Download failed:', error);
    }
}

const navigateToChart = () => {
    const params = {
        limit: 0,
        offset: 0,
        trip_id: param_trip_id.value,
        subscriber_type: param_subscriber_type.value,
        bike_id: param_bike_id.value,
        bike_type: param_bike_type.value,
        start_station_name: param_start_station_name.value,
        end_station_name: param_end_station_name.value,
        min_duration: param_min_duration.value,
        max_duration: param_max_duration.value,
        min_start_time: param_min_start_time.value,
        max_start_time: param_max_start_time.value,
    };
    router.push({ name: 'Charts', query: params });
};


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