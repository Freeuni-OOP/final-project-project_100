import axios from 'axios';

const api = axios.create({
    baseUrl: "/api",
    headers: {
        "Content-Type": "Application/json",
    },
});

api.interceptors.request.use((config) => {
    const token = localStorage.getItem("token")
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
});

api.interceptors.response.use(
    (response) => response,
    (err) => {
        if (err.response?.status === 401 || err.response?.status === 403) {
            localStorage.removeItem("token");
            localStorage.removeItem("user");
            window.location.href = "/login";
        }

        return Promise.reject(err);
    }
);

export default api;
