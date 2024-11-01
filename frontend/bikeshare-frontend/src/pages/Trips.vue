<template>
    <AppHeader></AppHeader>

    <div class="container">
        <div class="col w-1">
            <div class="search-content">
                <div>
                    <h3 class="title">Search</h3>
                </div>
                <div class="search-section">
                    <v-number-input label="Trip ID" control-variant="stacked"></v-number-input>

                    <v-text-field label="Subscriber type"></v-text-field>

                    <v-number-input label="Bike ID" control-variant="stacked"></v-number-input>

                    <v-text-field label="Start station"></v-text-field>
                    
                    <v-text-field label="End station"></v-text-field>
                    
                    <p>Duration (in minutes)</p>
                    <div class="flex">
                        <v-number-input label="" control-variant="stacked" max-width="150" :min="0"></v-number-input>
                        <p style="margin: auto;">to</p>
                        <v-number-input label="" control-variant="stacked" max-width="150" :min="0"></v-number-input>
                    </div>

                    <!-- Start time picker -->
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
                                full-width format="24hr"
                            ></v-time-picker>
                        </v-menu>
                    </v-text-field>
                
                </div>
                <div class="center">
                    <v-btn variant="outlined" class="btn">Search</v-btn>
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
                <v-data-table-server :headers="header" :items="trips" hide-default-footer>
                </v-data-table-server>
            </div>
        </div>
    </div>
</template>

<script setup>
import AppHeader from '@/components/AppHeader.vue';
import { VNumberInput } from 'vuetify/labs/VNumberInput'
import { VTimePicker } from 'vuetify/labs/VTimePicker'
import { VDateInput } from 'vuetify/labs/VDateInput'
import { computed, onMounted, ref, watch } from 'vue';

const header = [
    { title: 'Trip ID', key: 'trip_id', sortable: false },
    { title: 'Subscriber type', key: 'subscriber_type', sortable: false },
    { title: 'Bike ID', key: 'bike_id', sortable: false },
    { title: 'Start', key: 'start_time', sortable: false },
    { title: 'Start station', key: 'start_station_name', sortable: false },
    { title: 'End station', key: 'end_station_name', sortable: false },
    { title: 'Duration (minutes)', key: 'duration_minutes', sortable: false },
]

const trips = [
    {
        trip_id: 4098766,
        subscriber_type: '24 Hour Walk Up Pass',
        bike_id: 306,
        start_time: '2015-03-12 22:22:59 UTC',
        start_station_name: 'ACC - West & 12th Street',
        end_station_name: 'ACC - West & 12th Street',
        duration_minutes: 45
    },
    {
        trip_id: 4098766,
        subscriber_type: 'Lorem Ipsum',
        bike_id: 306,
        start_time: '2015-11-12 21:45:22 UTC',
        start_station_name: 'ACC - West & 12th Street',
        end_station_name: 'ACC - West & 12th Street',
        duration_minutes: 11
    },
    {
        trip_id: 4098766,
        subscriber_type: '24 Hour Walk Up Pass',
        bike_id: 306,
        start_time: '2015-03-12 22:22:59 UTC',
        start_station_name: 'ACC - West & 12th Street',
        end_station_name: 'ACC - West & 12th Street',
        duration_minutes: 45
    },
    {
        trip_id: 4098766,
        subscriber_type: '24 Hour Walk Up Pass',
        bike_id: 306,
        start_time: '2015-03-12 22:22:59 UTC',
        start_station_name: 'ACC - West & 12th Street',
        end_station_name: 'ACC - West & 12th Street',
        duration_minutes: 45
    },
]

const date = ref(null)
const time = ref(null)
const menu2 = ref(false)
const modal2 = ref(false)
const time_allow = ref(false)

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