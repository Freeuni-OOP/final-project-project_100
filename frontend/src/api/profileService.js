import api from './axios.js';

export const getProfile = async (username) => {
    const response = await api.get(`/profiles/${username}`);
    return response.data;
};