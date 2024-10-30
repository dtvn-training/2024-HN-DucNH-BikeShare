import axios from 'axios';
const api = axios.create({
    baseURL: 'http://localhost:8080/', // adjust this to your API URL
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
    },
});

export default api;