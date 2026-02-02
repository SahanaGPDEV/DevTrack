// =====================================================
// DevTrack - DSA Tracker Handler
// =====================================================

let allProblems = [];

document.addEventListener('DOMContentLoaded', () => {
    if (!requireAuth()) return;
    loadUserInfo();
    loadProblems();
    initProblemForm();
    document.getElementById('problemDate').value = getTodayDate();
});

function loadUserInfo() {
    const user = getCurrentUser();
    if (user) {
        document.getElementById('userName').textContent = user.fullName || user.username;
        document.getElementById('userEmail').textContent = user.email;
        document.getElementById('userAvatar').textContent = (user.fullName || user.username).charAt(0).toUpperCase();
    }
}

async function loadProblems() {
    const user = getCurrentUser();
    if (!user) return;

    try {
        const response = await DsaAPI.getAll(user.userId);
        if (response.success) {
            allProblems = response.data;
            renderProblems(allProblems);
        }
    } catch (error) {
        showNotification('Failed to load problems', 'error');
    }
}

function filterProblems() {
    const difficulty = document.getElementById('filterDifficulty').value;
    const platform = document.getElementById('filterPlatform').value;

    let filtered = allProblems;
    if (difficulty) filtered = filtered.filter(p => p.difficulty === difficulty);
    if (platform) filtered = filtered.filter(p => p.platform === platform);

    renderProblems(filtered);
}

function renderProblems(problems) {
    const tbody = document.getElementById('problemsTableBody');

    if (!problems || problems.length === 0) {
        tbody.innerHTML = `<tr><td colspan="6" class="empty-state">
            <i class="fas fa-code"></i><h3>No problems found</h3>
        </td></tr>`;
        return;
    }

    const badgeClass = { EASY: 'badge-easy', MEDIUM: 'badge-medium', HARD: 'badge-hard' };

    tbody.innerHTML = problems.map(p => `
        <tr>
            <td>
                <strong>${p.title}</strong>
                ${p.problemLink ? `<a href="${p.problemLink}" target="_blank" style="margin-left: 0.5rem;"><i class="fas fa-external-link-alt"></i></a>` : ''}
            </td>
            <td>${p.platform}</td>
            <td><span class="badge ${badgeClass[p.difficulty]}">${p.difficulty}</span></td>
            <td>${p.topic || '-'}</td>
            <td>${formatDate(p.solvedDate)}</td>
            <td>
                <button class="btn btn-sm btn-secondary" onclick="editProblem(${p.problemId})"><i class="fas fa-edit"></i></button>
                <button class="btn btn-sm btn-danger" onclick="deleteProblem(${p.problemId})"><i class="fas fa-trash"></i></button>
            </td>
        </tr>
    `).join('');
}

function openProblemModal() {
    document.getElementById('problemModalTitle').textContent = 'Add Problem';
    document.getElementById('problemForm').reset();
    document.getElementById('editProblemId').value = '';
    document.getElementById('problemDate').value = getTodayDate();
    document.getElementById('problemModal').classList.add('active');
}

function closeProblemModal() {
    document.getElementById('problemModal').classList.remove('active');
}

async function editProblem(problemId) {
    const problem = allProblems.find(p => p.problemId === problemId);
    if (!problem) return;

    document.getElementById('problemModalTitle').textContent = 'Edit Problem';
    document.getElementById('editProblemId').value = problem.problemId;
    document.getElementById('problemTitle').value = problem.title;
    document.getElementById('problemPlatform').value = problem.platform;
    document.getElementById('problemDifficulty').value = problem.difficulty;
    document.getElementById('problemTopic').value = problem.topic || '';
    document.getElementById('problemDate').value = problem.solvedDate;
    document.getElementById('problemLink').value = problem.problemLink || '';
    document.getElementById('problemNotes').value = problem.notes || '';
    document.getElementById('problemModal').classList.add('active');
}

async function deleteProblem(problemId) {
    if (!confirm('Delete this problem?')) return;

    try {
        const response = await DsaAPI.delete(problemId);
        if (response.success) {
            showNotification('Problem deleted');
            loadProblems();
        }
    } catch (error) {
        showNotification('Failed to delete', 'error');
    }
}

function initProblemForm() {
    document.getElementById('problemForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const user = getCurrentUser();
        const editId = document.getElementById('editProblemId').value;

        const data = {
            title: document.getElementById('problemTitle').value,
            platform: document.getElementById('problemPlatform').value,
            difficulty: document.getElementById('problemDifficulty').value,
            topic: document.getElementById('problemTopic').value,
            solvedDate: document.getElementById('problemDate').value,
            problemLink: document.getElementById('problemLink').value,
            notes: document.getElementById('problemNotes').value,
            status: 'SOLVED'
        };

        try {
            const response = editId
                ? await DsaAPI.update(editId, data)
                : await DsaAPI.create(user.userId, data);

            if (response.success) {
                showNotification(editId ? 'Problem updated!' : 'Problem added!');
                closeProblemModal();
                loadProblems();
            }
        } catch (error) {
            showNotification(error.message || 'Failed to save', 'error');
        }
    });
}
