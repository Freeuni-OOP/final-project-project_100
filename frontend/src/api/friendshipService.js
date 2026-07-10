// Assuming you have a utility to get your JWT token.
// Adjust the import or token retrieval to match your auth setup.
const API_BASE_URL = 'http://localhost:8080/api/friends';

const getHeaders = () => ({
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${localStorage.getItem('token')}`
});

export const friendshipService = {
    // Get Lists
    getAcceptedFriends: async () => {
        const response = await fetch(`${API_BASE_URL}/accepted`, { headers: getHeaders() });
        if (!response.ok) throw new Error('Failed to fetch friends');
        return response.json();
    },

    getPendingRequests: async () => {
        const response = await fetch(`${API_BASE_URL}/requests`, { headers: getHeaders() });
        if (!response.ok) throw new Error('Failed to fetch requests');
        return response.json();
    },

    // Actions
    sendRequest: async (targetUsername) => {
        const response = await fetch(`${API_BASE_URL}/request/${targetUsername}`, {
            method: 'POST',
            headers: getHeaders()
        });
        if (!response.ok) throw new Error('Failed to send request');
        return response.text();
    },

    acceptRequest: async (requesterId) => {
        const response = await fetch(`${API_BASE_URL}/accept/${requesterId}`, {
            method: 'PUT',
            headers: getHeaders()
        });
        if (!response.ok) throw new Error('Failed to accept request');
        return response.text();
    },

    rejectRequest: async (requesterId) => {
        const response = await fetch(`${API_BASE_URL}/reject/${requesterId}`, {
            method: 'PUT',
            headers: getHeaders()
        });
        if (!response.ok) throw new Error('Failed to reject request');
        return response.text();
    },

    removeFriend: async (targetUserId) => {
        const response = await fetch(`${API_BASE_URL}/remove/${targetUserId}`, {
            method: 'DELETE',
            headers: getHeaders()
        });
        if (!response.ok) throw new Error('Failed to modify friendship');
        return response.text();
    }
};