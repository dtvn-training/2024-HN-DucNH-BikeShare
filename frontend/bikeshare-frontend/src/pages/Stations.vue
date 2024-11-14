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
                        label="Station name"
                        v-model:model-value="param_name">
                    </v-text-field>
                    <v-radio-group inline label="Status" v-model="param_status">
                        <v-radio label="Any" value=""></v-radio>
                        <v-radio label="Active" value="active"></v-radio>
                        <v-radio label="Closed" value="closed"></v-radio>
                    </v-radio-group>
                    <v-text-field 
                        label="Address"
                        v-model="param_address">
                    </v-text-field>
                    <v-select
                        label="Property type"
                        :items="property_options"
                        v-model="param_property_type"
                    ></v-select>
                    <v-radio-group inline label="Power type" v-model="param_power_type">
                        <v-radio label="Any" value=""></v-radio>
                        <v-radio label="Solar" value="solar"></v-radio>
                        <v-radio label="Non-metered" value="non-metered"></v-radio>
                    </v-radio-group>
                    <v-range-slider label="Number of docks"
                                    v-model="docks_value"
                                    strict thumb-label="always" :thumb-size="10" :min="min_dock" :max="max_dock" :step="1">
                    </v-range-slider>
                    <v-range-slider label="Foot print length"
                                    v-model="length_value"
                                    strict thumb-label="always" :thumb-size="10" :min="min_length" :max="max_length" :step="1">
                    </v-range-slider>
                    <v-range-slider label="Foot print width"
                                    v-model="width_value"
                                    strict thumb-label="always" :thumb-size="10" :min="min_width" :max="max_width" :step="0.5">
                    </v-range-slider>
                </div>
                <div class="center">
                    <v-btn variant="outlined" class="btn" @click="getStations">Search</v-btn>
                </div>
            </div>
        </div>

        <div class="col w-2">
            <div class="table-content">
                <div class="table-header">
                    <div class="button-group">
                        <v-btn variant="outlined" class="btn" href="/charts">Charts</v-btn>
                        <v-btn variant="outlined" class="btn" @click="downloadExcel">Export</v-btn>
                    </div>
                </div>
                <v-data-table :headers="header" :items="stations">
                </v-data-table>
            </div>
        </div>
    </div>
    <v-snackbar v-model="snackbar" :timeout="timeout">{{ text }}</v-snackbar>
</template>

<script setup>
import AppHeader from '@/components/AppHeader.vue';
import { ref, computed } from 'vue';
import api from '@/config/api';

const header = [
    { title: 'Name', key: 'name', sortable: false },
    { title: 'Status', key: 'status', sortable: false },
    { title: 'Address', key: 'address', sortable: false },
    { title: 'City asset number', key: 'city_asset_number', sortable: false },
    { title: 'Alternate name', key: 'alternate_name', sortable: false },
    { title: 'Property type', key: 'property_type', sortable: false },
    { title: 'Number of docks', key: 'number_of_docks', sortable: false },
    { title: 'Power type', key: 'power_type', sortable: false },
    { title: 'Foot print length', key: 'footprint_length', sortable: false },
    { title: 'Foot print width', key: 'footprint_width', sortable: false },
    { title: 'Council district', key: 'council_district', sortable: false },
    { title: 'Modified date', key: 'modified_date', sortable: false },
]
const stations = ref()
const property_options = ref(["Any", "paid_parking", "sidewalk", "parkland", "undetermined_parking", "nonmetered_parking"])

let min_dock = ref(0)
let max_dock = ref(30)
let min_length = ref(0)
let max_length = ref(70)
let min_width = ref(0.0)
let max_width = ref(20.0)

const param_name = ref("")
const param_status = ref("")
const param_address = ref("")
const param_property_type = ref("")
const param_power_type = ref("")

let param_min_number_of_docks = ref()
let param_max_number_of_docks = ref()
let param_min_footprint_length = ref()
let param_max_footprint_length = ref()
let param_min_footprint_width = ref()
let param_max_footprint_width = ref()

const docks_value = ref([0, 30])
const length_value = ref([0, 70])
const width_value = ref([0, 20])

async function getStations() {
    param_min_number_of_docks = computed(() => {
        const min = Math.min(...docks_value.value)
        return min == min_dock.value? 0 : min
    })
    param_max_number_of_docks = computed(() => {
        const max = Math.max(...docks_value.value)
        return max == max_dock.value? 0 : max
    })
    param_min_footprint_length = computed(() => {
        const min = Math.min(...length_value.value)
        return min == min_length.value? 0 : min
    })
    param_max_footprint_length = computed(() => {
        const max = Math.max(...length_value.value)
        return max == max_length.value? 0 : max
    })
    param_min_footprint_width = computed(() => {
        const min = Math.min(...width_value.value)
        return min == min_width.value? 0 : min
    })
    param_max_footprint_width = computed(() => {
        const max = Math.max(...width_value.value)
        return max == max_width.value? 0 : max
    })
    await api.post(`/stations`, {
        name: param_name.value,
        address: param_address.value,
        status: param_status.value,
        property_type: param_property_type.value,
        power_type: param_power_type.value,
        min_docks: param_min_number_of_docks.value,
        max_docks: param_max_number_of_docks.value,
        min_length: param_min_footprint_length.value,
        max_length: param_max_footprint_length.value,
        min_width: param_min_footprint_width.value,
        max_width: param_max_footprint_width.value,
    })
        .then((response) => {
            stations.value = response.data.stations
            json.value = JSON.stringify(response.data)
            snackbar.value = true
            console.log(JSON.stringify(response.data))
        }).catch((error) => {
            console.log(error);
        })
}

const json = ref()

async function downloadExcel() {
    try {
        const response = await api.post(`/export/stations`, {
            json: json.value
        }, {
            responseType: 'blob',
            headers: {
            'Content-Type': 'application/json'
            }
        });

        const blob = new Blob([response.data], {
            type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        });

        const date = new Date();
        const name = `Stations_${date.getFullYear()}_${date.getMonth() + 1}_${date.getDate()}_${date.getHours()}_${date.getMinutes()}_${date.getSeconds()}.xlsx`;

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

const snackbar = ref(false)
const text = ref("Query completed")
const timeout = ref(2000)

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
</style>