// Shared Authentication Script for SenpaiStreamingWorld
// This file handles login state across ALL pages using localStorage

(function() {
    // Run immediately when script loads (not waiting for DOMContentLoaded)
    function initAuth() {
        const userSection = document.getElementById('userSection');
        if (!userSection) {
            // If userSection doesn't exist yet, wait for DOM
            if (document.readyState === 'loading') {
                document.addEventListener('DOMContentLoaded', initAuth);
            }
            return;
        }
        
        // Check localStorage for demo user - this is the SINGLE source of truth
        const demoUser = localStorage.getItem('demoUser');
        
        if (demoUser) {
            try {
                const user = JSON.parse(demoUser);
                if (user && user.loggedIn === true) {
                    showLoggedInUser(user.username);
                    return;
                }
            } catch (e) {
                console.error('Error parsing user data:', e);
            }
        }
        
        // Not logged in - show login button
        showLoginButton();
    }
    
    // Make functions globally available
    window.showLoggedInUser = function(username) {
        const userSection = document.getElementById('userSection');
        if (!userSection) return;
        
        userSection.innerHTML = `
            <div class="profile-dropdown">
                <div class="profile-icon" onclick="toggleDropdown()">
                    <i class="fas fa-user-circle"></i>
                    <span>${username}</span>
                    <i class="fas fa-chevron-down"></i>
                </div>
                <div class="dropdown-menu" id="dropdownMenu">
                    <a href="profile.html"><i class="fas fa-user"></i> Profile</a>
                    <a href="profile.html?section=continue-watching"><i class="fas fa-play-circle"></i> Continue Watching</a>
                    <a href="profile.html?section=continue-reading"><i class="fas fa-book-open"></i> Continue Reading</a>
                    <hr>
                    <a href="profile.html?section=settings"><i class="fas fa-cog"></i> Settings</a>
                    <hr>
                    <a href="#" onclick="logout(); return false;" class="logout-btn"><i class="fas fa-sign-out-alt"></i> Logout</a>
                </div>
            </div>
        `;
    };
    
    window.showLoginButton = function() {
        const userSection = document.getElementById('userSection');
        if (!userSection) return;
        
        userSection.innerHTML = '<a href="login.html" class="login-btn">Login</a>';
    };
    
    window.toggleDropdown = function() {
        const menu = document.getElementById('dropdownMenu');
        if (menu) {
            menu.classList.toggle('show');
        }
    };
    
    window.logout = function() {
        // Clear ALL user data from localStorage
        localStorage.removeItem('demoUser');
        localStorage.removeItem('rememberedEmail');
        
        // Update UI immediately
        showLoginButton();
        
        // Redirect to home page
        window.location.href = 'index.html';
    };
    
    window.isLoggedIn = function() {
        const demoUser = localStorage.getItem('demoUser');
        if (demoUser) {
            try {
                const user = JSON.parse(demoUser);
                return user && user.loggedIn === true;
            } catch (e) {
                return false;
            }
        }
        return false;
    };
    
    window.getCurrentUser = function() {
        const demoUser = localStorage.getItem('demoUser');
        if (demoUser) {
            try {
                return JSON.parse(demoUser);
            } catch (e) {
                return null;
            }
        }
        return null;
    };
    
    // Close dropdown when clicking outside
    document.addEventListener('click', function(e) {
        if (!e.target.closest('.profile-dropdown')) {
            const menu = document.getElementById('dropdownMenu');
            if (menu) menu.classList.remove('show');
        }
    });
    
    // Initialize auth - run immediately if DOM ready, otherwise wait
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initAuth);
    } else {
        initAuth();
    }
})();
