<template>
    <AppHeader></AppHeader>

    <div class="col w-1">
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
            <v-radio-group inline label="Power type">
                <v-radio label="Any" value=""></v-radio>
                <v-radio label="Solar" value="solar"></v-radio>
                <v-radio label="Classic" value="classic"></v-radio>
            </v-radio-group>
            <v-range-slider label="Foot print length"
                v-model="length_value"
                strict thumb-label="always" :min="0" :max="50" :step="1">
            </v-range-slider>
            <v-range-slider label="Foot print width"
                v-model="width_value"
                strict thumb-label="always" :min="0" :max="50" :step="1">
            </v-range-slider>
        </div>
        <div>
            <v-btn variant="outlined" class="btn">Search</v-btn>
        </div>
    </div>

    <div class="col w-2">
        <div>
            <div class="col center">
                <v-pagination length="5"></v-pagination>
                <v-btn variant="outlined" class="btn">Charts</v-btn>
                <v-btn variant="outlined" class="btn">Export</v-btn>
            </div>
            <v-data-table :headers="header" :items="trips" hide-default-footer="true">
    
            </v-data-table>
        </div>
    </div>
</template>

<script setup>
import AppHeader from '@/components/AppHeader.vue';
import { ref } from 'vue';

const header = [
    { title: 'Name', key: 'name', sortable: 'false' },
    { title: 'Status', key: 'status', sortable: 'false' },
    { title: 'Address', key: 'address', sortable: 'false' },
    { title: 'City asset number', key: 'city_asset_number', sortable: 'false' },
    { title: 'Alternate name', key: 'alternate_name', sortable: 'false' },
    { title: 'Property type', key: 'property_type', sortable: 'false' },
    { title: 'Number of docks', key: 'number_of_docks', sortable: 'false' },
    { title: 'Power type', key: 'power_type', sortable: 'false' },
    { title: 'Foot print length', key: 'foot_print_length', sortable: 'false' },
    { title: 'Foot print width', key: 'foot_print_width', sortable: 'false' },
    { title: 'Council district', key: 'council_district', sortable: 'false' },
    { title: 'Modified date', key: 'modified_date', sortable: 'false' },
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

const length_value = ref([0, 50])
const width_value = ref([0, 30])


</script>

<style>
    .col {
        display: inline-block;
    }

    .w-1 {
        width: 25%;
    }
    
    .w-2 {
        width: 70%;
    }

    .title {
        text-align: center;
    }

    .center {
        align-items: center;
    }
    
    .btn {
        margin: 10px;
    }

    .search-section {
        margin: 10px
    }

</style>