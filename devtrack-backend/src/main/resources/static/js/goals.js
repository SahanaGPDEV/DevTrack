// =====================================================
// DevTrack - Goals Handler
// =====================================================

let activeGoals = [];
let completedGoals = [];
let currentTab = 'active';

document.addEventListener('DOMContentLoaded', () => {
    if (!requireAuth()) return;
    loadUserInfo();
    loadGoals();
    initGoalForm();
    initProgressForm();
    document.getElementById('goalStartDate').value = getTodayDate();
});

function loadUserInfo() {
    const user = getCurrentUser();
    if (user) {
        document.getElementById('userName').textContent = user.fullName || user.username;
        document.getElementById('userEmail').textContent = user.email;
        document.getElementById('userAvatar').textContent = (user.fullName || user.username).charAt(0).toUpperCase();
    }
}

async function loadGoals() {
    const user = getCurrentUser();
    if (!user) return;

    try {
        const [activeRes, completedRes] = await Promise.all([
            GoalsAPI.getActive(user.userId),
            GoalsAPI.getCompleted(user.userId)
        ]);

        activeGoals = activeRes.success ? activeRes.data : [];
        completedGoals = completedRes.success ? completedRes.data : [];

        renderGoals();
    } catch (error) {
        showNotification('Failed to load goals', 'error');
    }
}

function showTab(tab) {
    currentTab = tab;
    document.getElementById('tabActive').classList.toggle('active', tab === 'active');
    document.getElementById('tabCompleted').classList.toggle('active', tab === 'completed');
    renderGoals();
}

function renderGoals() {
    const container = document.getElementById('goalsList');
    const goals = currentTab === 'active' ? activeGoals : completedGoals;

    if (!goals || goals.length === 0) {
        container.innerHTML = `<div class="empty-state">
            <i class="fas fa-bullseye"></i>
            <h3>No ${currentTab} goals</h3>
            <p>${currentTab === 'active' ? 'Create a new goal to get started' : 'Complete some goals to see them here'}</p>
        </div>`;
        return;
    }

    const priorityColors = { LOW: 'success', MEDIUM: 'warning', HIGH: 'danger' };

    container.innerHTML = goals.map(goal => {
        const progress = goal.targetValue > 0 ? Math.min(100, (goal.currentValue / goal.targetValue) * 100) : 0;
        return `
            <div class="goal-item">
                <div class="goal-header">
                    <div>
                        <span class="goal-title">${goal.title}</span>
                        <span class="badge badge-${priorityColors[goal.priority] || 'primary'}" style="margin-left: 0.5rem;">${goal.priority}</span>
                    </div>
                    <div>
                        ${!goal.isCompleted ? `
                            <button class="btn btn-sm btn-success" onclick="openProgressModal(${goal.goalId})">
                                <i class="fas fa-plus"></i> Progress
                            </button>
                        ` : ''}
                        <button class="btn btn-sm btn-secondary" onclick="editGoal(${goal.goalId})">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-danger" onclick="deleteGoal(${goal.goalId})">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </div>
                ${goal.description ? `<p style="color: var(--text-secondary); font-size: 0.875rem; margin: 0.5rem 0;">${goal.description}</p>` : ''}
                <div class="goal-progress">
                    <div class="progress-bar" style="flex: 1;">
                        <div class="progress-fill" style="width: ${progress}%"></div>
                    </div>
                    <span class="goal-percentage">${Math.round(progress)}%</span>
                </div>
                <div style="display: flex; justify-content: space-between; font-size: 0.75rem; color: var(--text-muted); margin-top: 0.5rem;">
                    <span>${goal.currentValue} / ${goal.targetValue} ${goal.category.replace('_', ' ')}</span>
                    <span>${formatDate(goal.startDate)} - ${formatDate(goal.endDate)}</span>
                </div>
            </div>
        `;
    }).join('');
}

function openGoalModal() {
    document.getElementById('goalModalTitle').textContent = 'New Goal';
    document.getElementById('goalForm').reset();
    document.getElementById('editGoalId').value = '';
    document.getElementById('goalStartDate').value = getTodayDate();
    document.getElementById('goalModal').classList.add('active');
}

function closeGoalModal() {
    document.getElementById('goalModal').classList.remove('active');
}

function editGoal(goalId) {
    const goal = [...activeGoals, ...completedGoals].find(g => g.goalId === goalId);
    if (!goal) return;

    document.getElementById('goalModalTitle').textContent = 'Edit Goal';
    document.getElementById('editGoalId').value = goal.goalId;
    document.getElementById('goalTitle').value = goal.title;
    document.getElementById('goalDescription').value = goal.description || '';
    document.getElementById('goalType').value = goal.goalType;
    document.getElementById('goalCategory').value = goal.category;
    document.getElementById('goalTarget').value = goal.targetValue;
    document.getElementById('goalPriority').value = goal.priority;
    document.getElementById('goalStartDate').value = goal.startDate;
    document.getElementById('goalEndDate').value = goal.endDate;
    document.getElementById('goalModal').classList.add('active');
}

async function deleteGoal(goalId) {
    if (!confirm('Delete this goal?')) return;

    try {
        const response = await GoalsAPI.delete(goalId);
        if (response.success) {
            showNotification('Goal deleted');
            loadGoals();
        }
    } catch (error) {
        showNotification('Failed to delete', 'error');
    }
}

function initGoalForm() {
    document.getElementById('goalForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const user = getCurrentUser();
        const editId = document.getElementById('editGoalId').value;

        const data = {
            title: document.getElementById('goalTitle').value,
            description: document.getElementById('goalDescription').value,
            goalType: document.getElementById('goalType').value,
            category: document.getElementById('goalCategory').value,
            targetValue: parseInt(document.getElementById('goalTarget').value),
            priority: document.getElementById('goalPriority').value,
            startDate: document.getElementById('goalStartDate').value,
            endDate: document.getElementById('goalEndDate').value,
        };

        try {
            const response = editId
                ? await GoalsAPI.update(editId, data)
                : await GoalsAPI.create(user.userId, data);

            if (response.success) {
                showNotification(editId ? 'Goal updated!' : 'Goal created!');
                closeGoalModal();
                loadGoals();
            }
        } catch (error) {
            showNotification(error.message || 'Failed to save', 'error');
        }
    });
}

function openProgressModal(goalId) {
    document.getElementById('progressGoalId').value = goalId;
    document.getElementById('progressValue').value = '';
    document.getElementById('progressModal').classList.add('active');
}

function closeProgressModal() {
    document.getElementById('progressModal').classList.remove('active');
}

function initProgressForm() {
    document.getElementById('progressForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const goalId = document.getElementById('progressGoalId').value;
        const progress = parseInt(document.getElementById('progressValue').value);

        try {
            const response = await GoalsAPI.updateProgress(goalId, progress);
            if (response.success) {
                showNotification('Progress updated!');
                closeProgressModal();
                loadGoals();
            }
        } catch (error) {
            showNotification('Failed to update progress', 'error');
        }
    });
}
