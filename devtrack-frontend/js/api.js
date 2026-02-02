// =====================================================
// DevTrack - API Helper Functions
// =====================================================

const API_BASE_URL = 'http://localhost:8085/api';

// Generic API call function
async function apiCall(endpoint, method = 'GET', data = null) {
    const options = {
        method,
        headers: {
            'Content-Type': 'application/json',
        },
    };

    if (data) {
        options.body = JSON.stringify(data);
    }

    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, options);
        const result = await response.json();

        if (!response.ok) {
            throw new Error(result.message || 'API request failed');
        }

        return result;
    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}

// User API
const UserAPI = {
    register: (userData) => apiCall('/users/register', 'POST', userData),
    login: (credentials) => apiCall('/users/login', 'POST', credentials),
    getUser: (userId) => apiCall(`/users/${userId}`),
    updateUser: (userId, userData) => apiCall(`/users/${userId}`, 'PUT', userData),
    deleteUser: (userId) => apiCall(`/users/${userId}`, 'DELETE'),
};

// Daily Logs API
const LogsAPI = {
    create: (userId, logData) => apiCall(`/logs/user/${userId}`, 'POST', logData),
    getAll: (userId) => apiCall(`/logs/user/${userId}`),
    getById: (logId) => apiCall(`/logs/${logId}`),
    getInRange: (userId, startDate, endDate) =>
        apiCall(`/logs/user/${userId}/range?startDate=${startDate}&endDate=${endDate}`),
    update: (logId, logData) => apiCall(`/logs/${logId}`, 'PUT', logData),
    delete: (logId) => apiCall(`/logs/${logId}`, 'DELETE'),
};

// DSA Problems API
const DsaAPI = {
    create: (userId, problemData) => apiCall(`/dsa/user/${userId}`, 'POST', problemData),
    getAll: (userId) => apiCall(`/dsa/user/${userId}`),
    getById: (problemId) => apiCall(`/dsa/${problemId}`),
    getByDifficulty: (userId, difficulty) => apiCall(`/dsa/user/${userId}/difficulty/${difficulty}`),
    getByPlatform: (userId, platform) => apiCall(`/dsa/user/${userId}/platform/${platform}`),
    getByTopic: (userId, topic) => apiCall(`/dsa/user/${userId}/topic?topic=${topic}`),
    update: (problemId, problemData) => apiCall(`/dsa/${problemId}`, 'PUT', problemData),
    delete: (problemId) => apiCall(`/dsa/${problemId}`, 'DELETE'),
};

// Goals API
const GoalsAPI = {
    create: (userId, goalData) => apiCall(`/goals/user/${userId}`, 'POST', goalData),
    getAll: (userId) => apiCall(`/goals/user/${userId}`),
    getById: (goalId) => apiCall(`/goals/${goalId}`),
    getActive: (userId) => apiCall(`/goals/user/${userId}/active`),
    getCompleted: (userId) => apiCall(`/goals/user/${userId}/completed`),
    update: (goalId, goalData) => apiCall(`/goals/${goalId}`, 'PUT', goalData),
    updateProgress: (goalId, progress) => apiCall(`/goals/${goalId}/progress?progress=${progress}`, 'PUT'),
    delete: (goalId) => apiCall(`/goals/${goalId}`, 'DELETE'),
};

// Skills API
const SkillsAPI = {
    create: (userId, skillData) => apiCall(`/skills/user/${userId}`, 'POST', skillData),
    getAll: (userId) => apiCall(`/skills/user/${userId}`),
    getById: (skillId) => apiCall(`/skills/${skillId}`),
    getByCategory: (userId, category) => apiCall(`/skills/user/${userId}/category/${category}`),
    update: (skillId, skillData) => apiCall(`/skills/${skillId}`, 'PUT', skillData),
    addHours: (skillId, hours) => apiCall(`/skills/${skillId}/hours?hours=${hours}`, 'PUT'),
    delete: (skillId) => apiCall(`/skills/${skillId}`, 'DELETE'),
};

// Analytics API
const AnalyticsAPI = {
    get: (userId) => apiCall(`/analytics/user/${userId}`),
};

// Session Management
function getCurrentUser() {
    const userData = localStorage.getItem('devtrack_user');
    return userData ? JSON.parse(userData) : null;
}

function setCurrentUser(user) {
    localStorage.setItem('devtrack_user', JSON.stringify(user));
}

function clearCurrentUser() {
    localStorage.removeItem('devtrack_user');
}

function isLoggedIn() {
    return getCurrentUser() !== null;
}

function requireAuth() {
    if (!isLoggedIn()) {
        window.location.href = 'index.html';
        return false;
    }
    return true;
}

function logout() {
    clearCurrentUser();
    window.location.href = 'index.html';
}

// Notification System
function showNotification(message, type = 'success') {
    const notification = document.getElementById('notification');
    if (notification) {
        notification.textContent = message;
        notification.className = `notification ${type} show`;

        setTimeout(() => {
            notification.classList.remove('show');
        }, 3000);
    }
}

// Date Formatting
function formatDate(dateString) {
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('en-US', options);
}

function getTodayDate() {
    return new Date().toISOString().split('T')[0];
}

function getWeekStartDate() {
    const today = new Date();
    const day = today.getDay();
    const diff = today.getDate() - day + (day === 0 ? -6 : 1);
    return new Date(today.setDate(diff)).toISOString().split('T')[0];
}
