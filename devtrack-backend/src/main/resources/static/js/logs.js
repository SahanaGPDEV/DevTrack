// =====================================================
// DevTrack - Daily Logs Handler
// =====================================================

document.addEventListener('DOMContentLoaded', () => {
    if (!requireAuth()) return;
    loadUserInfo();
    loadLogs();
    initLogForm();
    document.getElementById('logDate').value = getTodayDate();
});

function loadUserInfo() {
    const user = getCurrentUser();
    if (user) {
        document.getElementById('userName').textContent = user.fullName || user.username;
        document.getElementById('userEmail').textContent = user.email;
        document.getElementById('userAvatar').textContent = (user.fullName || user.username).charAt(0).toUpperCase();
    }
}

async function loadLogs() {
    const user = getCurrentUser();
    if (!user) return;

    try {
        const response = await LogsAPI.getAll(user.userId);
        if (response.success) {
            renderLogs(response.data);
        }
    } catch (error) {
        showNotification('Failed to load logs', 'error');
    }
}

function renderLogs(logs) {
    const tbody = document.getElementById('logsTableBody');

    if (!logs || logs.length === 0) {
        tbody.innerHTML = `
            <tr><td colspan="6" class="empty-state">
                <i class="fas fa-clock"></i><h3>No logs yet</h3><p>Start logging your coding sessions</p>
            </td></tr>`;
        return;
    }

    const moodEmoji = { GREAT: '😄', GOOD: '🙂', OKAY: '😐', TIRED: '😴', FRUSTRATED: '😤' };

    tbody.innerHTML = logs.map(log => `
        <tr>
            <td>${formatDate(log.logDate)}</td>
            <td><strong>${log.hoursCoded}h</strong></td>
            <td>${log.description || '-'}</td>
            <td>${log.technologiesUsed || '-'}</td>
            <td>${moodEmoji[log.mood] || '😐'} ${log.mood}</td>
            <td>
                <button class="btn btn-sm btn-secondary" onclick="editLog(${log.logId})">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="btn btn-sm btn-danger" onclick="deleteLog(${log.logId})">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        </tr>
    `).join('');
}

function openLogModal() {
    document.getElementById('logModalTitle').textContent = 'Add Log';
    document.getElementById('logForm').reset();
    document.getElementById('editLogId').value = '';
    document.getElementById('logDate').value = getTodayDate();
    document.getElementById('logModal').classList.add('active');
}

function closeLogModal() {
    document.getElementById('logModal').classList.remove('active');
}

async function editLog(logId) {
    try {
        const response = await LogsAPI.getById(logId);
        if (response.success) {
            const log = response.data;
            document.getElementById('logModalTitle').textContent = 'Edit Log';
            document.getElementById('editLogId').value = log.logId;
            document.getElementById('logDate').value = log.logDate;
            document.getElementById('logHours').value = log.hoursCoded;
            document.getElementById('logDescription').value = log.description || '';
            document.getElementById('logTechnologies').value = log.technologiesUsed || '';
            document.getElementById('logMood').value = log.mood || 'OKAY';
            document.getElementById('logProductivity').value = log.productivityScore || '';
            document.getElementById('logModal').classList.add('active');
        }
    } catch (error) {
        showNotification('Failed to load log', 'error');
    }
}

async function deleteLog(logId) {
    if (!confirm('Are you sure you want to delete this log?')) return;

    try {
        const response = await LogsAPI.delete(logId);
        if (response.success) {
            showNotification('Log deleted successfully');
            loadLogs();
        }
    } catch (error) {
        showNotification('Failed to delete log', 'error');
    }
}

function initLogForm() {
    document.getElementById('logForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const user = getCurrentUser();
        const editId = document.getElementById('editLogId').value;

        const logData = {
            logDate: document.getElementById('logDate').value,
            hoursCoded: parseFloat(document.getElementById('logHours').value),
            description: document.getElementById('logDescription').value,
            technologiesUsed: document.getElementById('logTechnologies').value,
            mood: document.getElementById('logMood').value,
            productivityScore: document.getElementById('logProductivity').value ?
                parseInt(document.getElementById('logProductivity').value) : null,
        };

        try {
            let response;
            if (editId) {
                response = await LogsAPI.update(editId, logData);
            } else {
                response = await LogsAPI.create(user.userId, logData);
            }

            if (response.success) {
                showNotification(editId ? 'Log updated!' : 'Log created!');
                closeLogModal();
                loadLogs();
            }
        } catch (error) {
            showNotification(error.message || 'Failed to save log', 'error');
        }
    });
}
