// =====================================================
// DevTrack - Skills Handler
// =====================================================

let allSkills = [];

document.addEventListener('DOMContentLoaded', () => {
    if (!requireAuth()) return;
    loadUserInfo();
    loadSkills();
    initSkillForm();
    initHoursForm();
});

function loadUserInfo() {
    const user = getCurrentUser();
    if (user) {
        document.getElementById('userName').textContent = user.fullName || user.username;
        document.getElementById('userEmail').textContent = user.email;
        document.getElementById('userAvatar').textContent = (user.fullName || user.username).charAt(0).toUpperCase();
    }
}

async function loadSkills() {
    const user = getCurrentUser();
    if (!user) return;

    try {
        const response = await SkillsAPI.getAll(user.userId);
        if (response.success) {
            allSkills = response.data;
            renderSkills(allSkills);
        }
    } catch (error) {
        showNotification('Failed to load skills', 'error');
    }
}

function filterSkills() {
    const category = document.getElementById('filterCategory').value;
    const filtered = category ? allSkills.filter(s => s.category === category) : allSkills;
    renderSkills(filtered);
}

function renderSkills(skills) {
    const container = document.getElementById('skillsGrid');

    if (!skills || skills.length === 0) {
        container.innerHTML = `<div class="empty-state" style="grid-column: 1/-1;">
            <i class="fas fa-layer-group"></i>
            <h3>No skills found</h3>
            <p>Add skills to start tracking your proficiency</p>
        </div>`;
        return;
    }

    const proficiencyColors = {
        BEGINNER: '#94a3b8',
        INTERMEDIATE: '#6366f1',
        ADVANCED: '#10b981',
        EXPERT: '#f59e0b'
    };

    const categoryIcons = {
        LANGUAGE: 'fas fa-code',
        FRAMEWORK: 'fas fa-cubes',
        DATABASE: 'fas fa-database',
        TOOL: 'fas fa-tools',
        OTHER: 'fas fa-star'
    };

    container.innerHTML = skills.map(skill => `
        <div class="skill-card">
            <div class="skill-card-header">
                <div class="skill-icon">
                    <i class="${categoryIcons[skill.category] || 'fas fa-star'}"></i>
                </div>
                <div style="flex: 1;">
                    <div class="skill-name" style="font-weight: 600;">${skill.skillName}</div>
                    <div style="font-size: 0.75rem; color: var(--text-muted);">${skill.category}</div>
                </div>
                <span class="badge" style="background: ${proficiencyColors[skill.proficiency]}20; color: ${proficiencyColors[skill.proficiency]};">
                    ${skill.proficiency}
                </span>
            </div>
            <div class="skill-meta">
                <span><i class="fas fa-clock"></i> ${skill.hoursPracticed || 0}h practiced</span>
                <span><i class="fas fa-folder"></i> ${skill.projectsCount || 0} projects</span>
            </div>
            <div style="display: flex; gap: 0.5rem; margin-top: 1rem;">
                <button class="btn btn-sm btn-success" onclick="openHoursModal(${skill.skillId})" style="flex: 1;">
                    <i class="fas fa-plus"></i> Hours
                </button>
                <button class="btn btn-sm btn-secondary" onclick="editSkill(${skill.skillId})">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="btn btn-sm btn-danger" onclick="deleteSkill(${skill.skillId})">
                    <i class="fas fa-trash"></i>
                </button>
            </div>
        </div>
    `).join('');
}

function openSkillModal() {
    document.getElementById('skillModalTitle').textContent = 'Add Skill';
    document.getElementById('skillForm').reset();
    document.getElementById('editSkillId').value = '';
    document.getElementById('skillModal').classList.add('active');
}

function closeSkillModal() {
    document.getElementById('skillModal').classList.remove('active');
}

function editSkill(skillId) {
    const skill = allSkills.find(s => s.skillId === skillId);
    if (!skill) return;

    document.getElementById('skillModalTitle').textContent = 'Edit Skill';
    document.getElementById('editSkillId').value = skill.skillId;
    document.getElementById('skillName').value = skill.skillName;
    document.getElementById('skillCategory').value = skill.category;
    document.getElementById('skillProficiency').value = skill.proficiency;
    document.getElementById('skillHours').value = skill.hoursPracticed || '';
    document.getElementById('skillProjects').value = skill.projectsCount || '';
    document.getElementById('skillNotes').value = skill.notes || '';
    document.getElementById('skillModal').classList.add('active');
}

async function deleteSkill(skillId) {
    if (!confirm('Delete this skill?')) return;

    try {
        const response = await SkillsAPI.delete(skillId);
        if (response.success) {
            showNotification('Skill deleted');
            loadSkills();
        }
    } catch (error) {
        showNotification('Failed to delete', 'error');
    }
}

function initSkillForm() {
    document.getElementById('skillForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const user = getCurrentUser();
        const editId = document.getElementById('editSkillId').value;

        const data = {
            skillName: document.getElementById('skillName').value,
            category: document.getElementById('skillCategory').value,
            proficiency: document.getElementById('skillProficiency').value,
            hoursPracticed: parseFloat(document.getElementById('skillHours').value) || 0,
            projectsCount: parseInt(document.getElementById('skillProjects').value) || 0,
            notes: document.getElementById('skillNotes').value,
        };

        try {
            const response = editId
                ? await SkillsAPI.update(editId, data)
                : await SkillsAPI.create(user.userId, data);

            if (response.success) {
                showNotification(editId ? 'Skill updated!' : 'Skill added!');
                closeSkillModal();
                loadSkills();
            }
        } catch (error) {
            showNotification(error.message || 'Failed to save', 'error');
        }
    });
}

function openHoursModal(skillId) {
    document.getElementById('hoursSkillId').value = skillId;
    document.getElementById('addHours').value = '';
    document.getElementById('hoursModal').classList.add('active');
}

function closeHoursModal() {
    document.getElementById('hoursModal').classList.remove('active');
}

function initHoursForm() {
    document.getElementById('hoursForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const skillId = document.getElementById('hoursSkillId').value;
        const hours = parseFloat(document.getElementById('addHours').value);

        try {
            const response = await SkillsAPI.addHours(skillId, hours);
            if (response.success) {
                showNotification('Hours added!');
                closeHoursModal();
                loadSkills();
            }
        } catch (error) {
            showNotification('Failed to add hours', 'error');
        }
    });
}
