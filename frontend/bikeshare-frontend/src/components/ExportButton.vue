<template>
    <v-btn variant="outlined" class="btn" @click="downloadExcel(table, json)">Export</v-btn>
</template>
<script setup>
import api from '@/config/api';

const props = defineProps({
    table: String,
    json: Object
});

async function downloadExcel(table, json) {
    // if (isEmpty(json)) {
    //     console.error('No data to export');
    //     return;
    // }
    if (json === undefined) {
        console.error('No data to export');
        return;
    }

    try {
        const response = await api.post(`/export/${table}`, {
            json: json
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
        const name = `${table}_${date.getFullYear()}_${date.getMonth() + 1}_${date.getDate()}_${date.getHours()}_${date.getMinutes()}_${date.getSeconds()}.xlsx`;

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