<template>
    <AppHeader></AppHeader>

    <div class="container">
        <div class="col w-1">
            <div class="search-content">
                <div>
                    <h3 class="title">Search</h3>
                </div>
                <div class="search-section">
                    <v-text-field label="Station name"></v-text-field>
                    <v-radio-group inline label="Status">
                        <v-radio label="Any" value=""></v-radio>
                        <v-radio label="Active" value="active"></v-radio>
                        <v-radio label="Closed" value="closed"></v-radio>
                    </v-radio-group>
                    <v-text-field label="Address"></v-text-field>
                    <v-text-field label="Property type"></v-text-field>
                    <v-radio-group inline label="Power type">
                        <v-radio label="Any" value=""></v-radio>
                        <v-radio label="Solar" value="solar"></v-radio>
                        <v-radio label="Non-metered" value="non_metered"></v-radio>
                    </v-radio-group>
                    <v-range-slider label="Foot print length"
                                    v-model="length_value"
                                    strict thumb-label="always" :thumb-size="10" :min="0" :max="100" :step="1">
                    </v-range-slider>
                    <v-range-slider label="Foot print width"
                                    v-model="width_value"
                                    strict thumb-label="always" :thumb-size="10" :min="0" :max="20" :step="1">
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
                    <v-pagination length="5"></v-pagination>
                    <div class="button-group">
                        <v-btn variant="outlined" class="btn" href="/charts">Charts</v-btn>
                        <v-btn variant="outlined" class="btn">Export</v-btn>
                    </div>
                </div>
                <v-data-table-server :headers="header" :items="stations" hide-default-footer></v-data-table-server>
            </div>
        </div>
    </div>
</template>

<script setup>
import AppHeader from '@/components/AppHeader.vue';
import { ref } from 'vue';
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

const trips = [
    {
        name: "Test Example",
        status: "Active",
        address: "243A De La Thanh",
        city_asset_number: "4361725371",
        alternate_name: "",
        property_type: "nonmetered_parking",
        number_of_docks: 12,
        power_type: "Solar",
        foot_print_length: 50,
        foot_print_width: 20,
        council_district: 2,
        modified_date: '2021-01-04 00:00:00 UTC'
    },
    {
        name: "Lorem Ipsum",
        status: "Closed",
        address: "1 Dai Co Viet",
        city_asset_number: "4361725371",
        power_type: "Classic",
        foot_print_length: 34
    },
    {
        name: "Test Example",
        status: "Active",
        address: "243A De La Thanh",
        city_asset_number: "4361725371",
        power_type: "Solar",
        foot_print_length: 50
    }
]

const stations = ref()

const length_value = ref([0, 100])
const width_value = ref([0, 20])

async function getStations() {
    await api.get(`/stations/`)
        .then((response) => {
            stations.value = response.data.stations
            console.log("1");
        }).catch((error) => {
            console.log(error);
        })
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
</style>