// =====================================================
// DevTrack - Authentication Handler
// =====================================================

document.addEventListener('DOMContentLoaded', () => {
    // Check if already logged in
    if (isLoggedIn()) {
        window.location.href = 'dashboard.html';
        return;
    }

    initAuthTabs();
    initAuthForms();
});

function initAuthTabs() {
    const tabs = document.querySelectorAll('.auth-tab');
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');

            if (tab.dataset.tab === 'login') {
                loginForm.style.display = 'block';
                registerForm.style.display = 'none';
            } else {
                loginForm.style.display = 'none';
                registerForm.style.display = 'block';
            }
        });
    });
}

function initAuthForms() {
    // Login Form
    const loginForm = document.getElementById('loginForm');
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const username = document.getElementById('loginUsername').value;
        const password = document.getElementById('loginPassword').value;

        try {
            const response = await UserAPI.login({ username, password });

            if (response.success) {
                setCurrentUser(response.data);
                showNotification('Login successful! Redirecting...');
                setTimeout(() => {
                    window.location.href = 'dashboard.html';
                }, 1000);
            } else {
                showNotification(response.message || 'Login failed', 'error');
            }
        } catch (error) {
            showNotification(error.message || 'Login failed. Please try again.', 'error');
        }
    });

    // Register Form
    const registerForm = document.getElementById('registerForm');
    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        // Simple email validation
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(document.getElementById('regEmail').value)) {
            showNotification('Please enter a valid email address', 'error');
            return;
        }

        const userData = {
            username: document.getElementById('regUsername').value,
            email: document.getElementById('regEmail').value,
            fullName: document.getElementById('regFullName').value,
            password: document.getElementById('regPassword').value,
        };

        try {
            const response = await UserAPI.register(userData);

            if (response.success) {
                showNotification('Account created! Please login.');
                // Switch to login tab
                document.querySelector('[data-tab="login"]').click();
                registerForm.reset();
            } else {
                showNotification(response.message || 'Registration failed', 'error');
            }
        } catch (error) {
            showNotification(error.message || 'Registration failed. Please try again.', 'error');
        }
    });
}
