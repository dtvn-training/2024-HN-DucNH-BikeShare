import axios from 'axios';
const api = axios.create({
    baseURL: 'https://two024-hn-ducnh-bikeshare-atsk.onrender.com/',
    timeout: 480000,
    headers: {
        'Content-Type': 'application/json',
    },
});

export default api;