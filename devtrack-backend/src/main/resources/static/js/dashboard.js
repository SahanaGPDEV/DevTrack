// =====================================================
// DevTrack - Dashboard Handler
// =====================================================

let hoursChart = null;
let difficultyChart = null;
let selectedMood = 'OKAY';

document.addEventListener('DOMContentLoaded', () => {
    if (!requireAuth()) return;

    loadUserInfo();
    loadDashboardData();
    initQuickLogForm();
    initMoodSelector();

    // Set default date to today
    document.getElementById('logDate').value = getTodayDate();
});

async function loadUserInfo() {
    const user = getCurrentUser();
    if (user) {
        document.getElementById('userName').textContent = user.fullName || user.username;
        document.getElementById('userEmail').textContent = user.email;
        document.getElementById('userAvatar').textContent = (user.fullName || user.username).charAt(0).toUpperCase();
    }
}

async function loadDashboardData() {
    const user = getCurrentUser();
    if (!user) return;

    try {
        const response = await AnalyticsAPI.get(user.userId);

        if (response.success) {
            const data = response.data;

            // Update Stats
            document.getElementById('totalHours').textContent = data.totalHoursCoded || 0;
            document.getElementById('totalProblems').textContent = data.totalProblemsSolved || 0;
            document.getElementById('currentStreak').textContent = data.currentStreak || 0;
            document.getElementById('activeGoals').textContent = data.activeGoals || 0;

            // Update Streak Display
            document.getElementById('streakDisplay').textContent = data.currentStreak || 0;
            document.getElementById('longestStreak').textContent = data.longestStreak || 0;

            // Update Weekly Summary
            document.getElementById('weeklyHours').textContent = data.weeklyHours?.toFixed(1) || 0;
            document.getElementById('weeklyProblems').textContent = data.weeklyProblems || 0;
            document.getElementById('avgHours').textContent = data.avgHoursPerDay?.toFixed(1) || 0;

            // Render Charts
            renderHoursChart(data.dailyHoursChart || []);
            renderDifficultyChart(data.problemsByDifficulty || {});

            // Render Goals Progress
            renderGoalsProgress(data.goalProgress || []);
        }
    } catch (error) {
        console.error('Error loading dashboard:', error);
        showNotification('Failed to load dashboard data', 'error');
    }
}

function renderHoursChart(data) {
    const ctx = document.getElementById('hoursChart').getContext('2d');

    if (hoursChart) {
        hoursChart.destroy();
    }

    const labels = data.map(d => d.date);
    const values = data.map(d => d.hours);

    hoursChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Hours',
                data: values,
                backgroundColor: 'rgba(99, 102, 241, 0.5)',
                borderColor: 'rgb(99, 102, 241)',
                borderWidth: 2,
                borderRadius: 8,
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    grid: { color: 'rgba(255, 255, 255, 0.1)' },
                    ticks: { color: '#94a3b8' }
                },
                x: {
                    grid: { display: false },
                    ticks: { color: '#94a3b8' }
                }
            }
        }
    });
}

function renderDifficultyChart(data) {
    const ctx = document.getElementById('difficultyChart').getContext('2d');

    if (difficultyChart) {
        difficultyChart.destroy();
    }

    difficultyChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['Easy', 'Medium', 'Hard'],
            datasets: [{
                data: [data.EASY || 0, data.MEDIUM || 0, data.HARD || 0],
                backgroundColor: [
                    'rgba(16, 185, 129, 0.8)',
                    'rgba(245, 158, 11, 0.8)',
                    'rgba(239, 68, 68, 0.8)'
                ],
                borderWidth: 0,
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: { color: '#94a3b8', padding: 20 }
                }
            },
            cutout: '60%'
        }
    });
}

function renderGoalsProgress(goals) {
    const container = document.getElementById('goalsProgressList');

    if (goals.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-bullseye"></i>
                <h3>No Active Goals</h3>
                <p>Create goals to track your progress</p>
            </div>
        `;
        return;
    }

    container.innerHTML = goals.map(goal => `
        <div class="goal-progress-item">
            <div class="goal-progress-header">
                <span class="goal-progress-title">${goal.title}</span>
                <span class="goal-progress-percentage">${Math.round(goal.percentage)}%</span>
            </div>
            <div class="progress-bar">
                <div class="progress-fill" style="width: ${goal.percentage}%"></div>
            </div>
            <div class="goal-progress-meta">
                <span>${goal.current} / ${goal.target}</span>
            </div>
        </div>
    `).join('');
}

function initMoodSelector() {
    const moodOptions = document.querySelectorAll('.mood-option');
    moodOptions.forEach(option => {
        option.addEventListener('click', () => {
            moodOptions.forEach(o => o.classList.remove('selected'));
            option.classList.add('selected');
            selectedMood = option.dataset.mood;
        });
    });
}

function openQuickLogModal() {
    document.getElementById('quickLogModal').classList.add('active');
}

function closeQuickLogModal() {
    document.getElementById('quickLogModal').classList.remove('active');
    document.getElementById('quickLogForm').reset();
    document.getElementById('logDate').value = getTodayDate();

    // Reset mood selector
    document.querySelectorAll('.mood-option').forEach(o => o.classList.remove('selected'));
    document.querySelector('[data-mood="OKAY"]').classList.add('selected');
    selectedMood = 'OKAY';
}

function initQuickLogForm() {
    const form = document.getElementById('quickLogForm');
    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const user = getCurrentUser();
        if (!user) return;

        const logData = {
            logDate: document.getElementById('logDate').value,
            hoursCoded: parseFloat(document.getElementById('logHours').value),
            description: document.getElementById('logDescription').value,
            technologiesUsed: document.getElementById('logTechnologies').value,
            mood: selectedMood,
        };

        try {
            const response = await LogsAPI.create(user.userId, logData);

            if (response.success) {
                showNotification('Log saved successfully!');
                closeQuickLogModal();
                loadDashboardData();
            } else {
                showNotification(response.message || 'Failed to save log', 'error');
            }
        } catch (error) {
            showNotification(error.message || 'Failed to save log', 'error');
        }
    });
}
