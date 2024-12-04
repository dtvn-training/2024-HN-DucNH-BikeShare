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
                    <v-btn variant="tonal" class="btn" @click="resetParam">Reset</v-btn>
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
                    <template #item.start_station_name="{ item }">
                        <div>
                            <p @click="handlePopup(item.start_station_name, item.end_station_name)">
                                {{ item.start_station_name }}
                            </p>
                        </div>
                    </template>
                    <template #item.start_time="{ item }">
                        <div>
                            {{ formatTimestamp(new Date(item.start_time)) }}
                        </div>
                    </template>
                    <template #item.end_station_name="{ item }">
                        <div>
                            <p @click="handlePopup(item.start_station_name, item.end_station_name)">
                                {{ item.end_station_name }}
                            </p>
                        </div>
                    </template>
                </v-data-table>
                <v-btn variant="outlined" class="btn" @click="loadMore" :disabled="disable">Load more</v-btn>
            </div>
        </div>
    </div>
    <AppFooter></AppFooter>

    <v-snackbar v-model="snackbar" :timeout="timeout">{{ text }}</v-snackbar>
    <v-dialog v-model="popup" max-width="1400">
        <v-card v-if="selectedTrip.start_station_name.localeCompare('')">
            <v-card-title>
                <p class="center">{{ selectedTrip.start_station_name }} to {{ selectedTrip.end_station_name }}</p>
            </v-card-title>
            <v-card-text>
                <p class="center">Distance: {{ selectedTrip.distance }} km</p>
            </v-card-text>
            
            <div class="flex">
                <Map :latitude="coordinates.latitude" :longitude="coordinates.longitude" class="map"></Map>
                <Map :latitude="coordinates2.latitude" :longitude="coordinates2.longitude"></Map>
            </div>
            <v-card-actions>
                <v-btn color="blue darken-1" text @click="handlePopupClose">Close</v-btn>
            </v-card-actions>
        </v-card>
        <v-card v-else>
            <v-card-title>Not enough information</v-card-title>
        </v-card>
    </v-dialog>
</template>

<script setup>
import AppHeader from '@/components/AppHeader.vue';
import { VNumberInput } from 'vuetify/labs/VNumberInput'
import { onMounted, ref } from 'vue';
import api from '@/config/api';
import { useRouter } from 'vue-router'
import AppFooter from '@/components/AppFooter.vue';
import Map from './Map.vue';

const router = useRouter()

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

const OFFSET = 400
const popup = ref(false)
const disable = ref(true)
const snackbar = ref(false)
const text = ref()
const timeout = ref(2000)
const json = ref()

const stations = ref([])
const selectedTrip = ref({
    start_station_name: '',
    end_station_name: '',
    distance: 0
})
const coordinates = ref({
    latitude: 0,
    longitude: 0
})
const coordinates2 = ref({
    latitude: 0,
    longitude: 0
})
const distance = ref(0)

function formatTimestamp(timestamp) {
    return timestamp.toISOString().slice(0, 19).replace('T', ' ')
}

function formatStationName(name) {
    return name.replace("/", " & ")
}

function resetParam() {
    param_trip_id.value = ""
    param_subscriber_type.value = ""
    param_bike_id.value = ""
    param_bike_type.value = ""
    param_start_station_name.value = ""
    param_end_station_name.value = ""
    param_min_duration.value = 0
    param_max_duration.value = 0
    param_min_start_time.value = ""
    param_max_start_time.value = ""
}

function handlePopup(item, item_end) {
    console.log(item)

    stations.value.forEach((station) => {
        if (!item.localeCompare(station.name) || !formatStationName(item).localeCompare(station.name)) {
            const [latitude1, longitude1] = station.location.replace('(', '').replace(')', '').split(',').map(num => parseFloat(num));
            coordinates.value = {
                latitude: latitude1,
                longitude: longitude1
            };
            console.log(coordinates.value)
        }
        if (!item_end.localeCompare(station.name) || !formatStationName(item_end).localeCompare(station.name)) {
            const [latitude1, longitude1] = station.location.replace('(', '').replace(')', '').split(',').map(num => parseFloat(num));
            coordinates2.value = {
                latitude: latitude1,
                longitude: longitude1
            };
            console.log(coordinates2.value)
        }
    })

    if (coordinates.value.latitude !== 0 && coordinates2.value.latitude !== 0) {
        const calc_distance = getDistanceFromLatLonInKm(coordinates.value.latitude, coordinates.value.longitude, coordinates2.value.latitude, coordinates2.value.longitude)
        console.log(Math.round(calc_distance * 100) / 100)
        distance.value = Math.round(calc_distance * 100) / 100
        selectedTrip.value = {
            start_station_name: item,
            end_station_name: item_end,
            distance: distance.value
        }
        popup.value = true
    } else {
        console.log("Not found")
    }
    popup.value = true
}

function handlePopupClose() {
    popup.value = false
    resetPopupData()
}

watch(() => popup.value, (value) => {
    if (!value) {
        resetPopupData()
    }
})

function resetPopupData() {
    selectedTrip.value = {
        start_station_name: '',
        end_station_name: '',
        distance: 0
    }
    coordinates.value = {
        latitude: 0,
        longitude: 0
    }
    coordinates2.value = {
        latitude: 0,
        longitude: 0
    }
    distance.value = 0
}

function getDistanceFromLatLonInKm(lat1, lon1, lat2, lon2) {
  var R = 6371;
  var dLat = deg2rad(lat2-lat1);
  var dLon = deg2rad(lon2-lon1); 
  var a = 
    Math.sin(dLat/2) * Math.sin(dLat/2) +
    Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * 
    Math.sin(dLon/2) * Math.sin(dLon/2); 
  var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
  var d = R * c;
  return d;
}

function deg2rad(deg) {
  return deg * (Math.PI/180)
}

function openSnackbar(content) {
    text.value = content
    snackbar.value = true
}

function navigateToChart() {
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
    const routeData = router.resolve({ name: 'Charts', query: params });
    window.open(routeData.href, '_blank');
}

async function getStations() {
    await api.post(`/stations`, {
        name: "",
        address: "",
        status: "",
        property_type: "",
        power_type: "",
        min_docks: 0,
        max_docks: 0,
        min_length: 0,
        max_length: 0,
        min_width: 0,
        max_width: 0,
    })
    .then((response) => {
        stations.value = response.data.stations
    }).catch((error) => {
        console.log(error);
        })
}

onMounted(() => {
    getStations()
})

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

    .map {
        padding-left: 10px;
    }

    .center {
        text-align: center;
    }
</style>