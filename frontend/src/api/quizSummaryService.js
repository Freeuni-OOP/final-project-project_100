import api from './axios.js';

export const getQuizSummary = async (quizId) => {
    const response = await api.get(`/quizzes/${quizId}/summary`);
    return response.data;
};