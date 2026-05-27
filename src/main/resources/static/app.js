// ─── SESSION HELPERS (used across all pages) ──────────────────────────────────
function requireAuth() {
  if (!isLoggedIn()) { window.location.href = 'index.html'; }
}
function redirectIfLoggedIn() {
  if (isLoggedIn()) { window.location.href = 'dashboard.html'; }
}
function logout() {
  clearAuth();
  window.location.href = 'index.html';
}

// Store userId in sessionStorage after login
function getUserId()       { return sessionStorage.getItem('user_id'); }
function saveUserId(id)    { sessionStorage.setItem('user_id', id); }

// ─── ALERTS ───────────────────────────────────────────────────────────────────
function showAlert(message, type = 'success', containerId = 'alert-container') {
  const container = document.getElementById(containerId);
  if (!container) return;
  const alert = document.createElement('div');
  alert.className = `alert alert-${type}`;
  alert.textContent = message;
  container.appendChild(alert);
  setTimeout(() => alert.remove(), 4000);
}

// ─── MODAL ────────────────────────────────────────────────────────────────────
function openModal(id)  { document.getElementById(id)?.classList.add('open'); }
function closeModal(id) { document.getElementById(id)?.classList.remove('open'); }

// Close modal on backdrop click
document.addEventListener('click', e => {
  if (e.target.classList.contains('modal-backdrop')) {
    e.target.classList.remove('open');
  }
});

// ─── PAGINATION RENDERER ──────────────────────────────────────────────────────
function renderPagination(pageData, containerId, onPageChange) {
  const container = document.getElementById(containerId);
  if (!container || !pageData) return;
  const { number: current, totalPages } = pageData;
  if (totalPages <= 1) { container.innerHTML = ''; return; }

  let html = '<div class="pagination">';
  html += `<button class="btn-page" ${current === 0 ? 'disabled' : ''} data-page="${current - 1}">‹</button>`;
  for (let i = 0; i < totalPages; i++) {
    html += `<button class="btn-page ${i === current ? 'active' : ''}" data-page="${i}">${i + 1}</button>`;
  }
  html += `<button class="btn-page" ${current === totalPages - 1 ? 'disabled' : ''} data-page="${current + 1}">›</button>`;
  html += '</div>';
  container.innerHTML = html;
  container.querySelectorAll('.btn-page:not([disabled])').forEach(btn => {
    btn.addEventListener('click', () => onPageChange(+btn.dataset.page));
  });
}

// ─── EMPTY STATE ──────────────────────────────────────────────────────────────
function emptyState(message, icon = '🍽️') {
  return `<div class="empty-state"><span class="empty-icon">${icon}</span><p>${message}</p></div>`;
}

// ─── UNIT & DIFFICULTY OPTIONS ────────────────────────────────────────────────
const UNITS = ['GRAM','KILOGRAM','MILLILITER','LITER','PIECE','TABLESPOON','TEASPOON','CUP','OUNCE','POUND'];
const DIFFICULTIES = ['EASY','MEDIUM','HARD'];

function unitOptions(selected = '') {
  return UNITS.map(u => `<option value="${u}" ${u === selected ? 'selected' : ''}>${u}</option>`).join('');
}
function difficultyOptions(selected = '') {
  return DIFFICULTIES.map(d => `<option value="${d}" ${d === selected ? 'selected' : ''}>${d}</option>`).join('');
}

// ─── NAV ACTIVE STATE ─────────────────────────────────────────────────────────
function setActiveNav() {
  const page = location.pathname.split('/').pop();
  document.querySelectorAll('.nav-link').forEach(link => {
    if (link.getAttribute('href') === page) link.classList.add('active');
  });
}
document.addEventListener('DOMContentLoaded', setActiveNav);

// ─── SHELL RENDERER ──────────────────────────────────────────────────────────
function renderShell(pageTitle) {
  const user = getUser();
  const admin = isAdmin();
  document.body.innerHTML = `
    <div id="alert-container"></div>
    <div class="app-shell">
      <header class="app-topbar">
        <div style="display:flex;align-items:center;gap:var(--space-4)">
          <button class="mobile-menu-btn btn-ghost btn-icon" id="mobile-menu-btn" aria-label="Toggle menu">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 12h18M3 6h18M3 18h18"/></svg>
          </button>
          <a href="dashboard.html" class="app-logo">
            <svg width="32" height="32" viewBox="0 0 56 56" fill="none" aria-label="RecipeApp">
              <circle cx="28" cy="28" r="28" fill="var(--color-primary-highlight)"/>
              <path d="M18 34 C18 28 22 22 28 20 C34 22 38 28 38 34" stroke="var(--color-primary)" stroke-width="2.5" stroke-linecap="round" fill="none"/>
              <rect x="16" y="34" width="24" height="4" rx="2" fill="var(--color-primary)"/>
              <path d="M24 20 C24 16 20 14 20 14" stroke="var(--color-accent)" stroke-width="2" stroke-linecap="round"/>
              <circle cx="20" cy="13" r="2" fill="var(--color-accent)"/>
              <path d="M32 20 C32 16 36 14 36 14" stroke="var(--color-accent)" stroke-width="2" stroke-linecap="round"/>
              <circle cx="36" cy="13" r="2" fill="var(--color-accent)"/>
            </svg>
            <span class="app-logo-text">RecipeApp</span>
          </a>
        </div>
        <div class="app-topbar-actions">
          <span class="topbar-user">Hello, <strong>${user?.username || ''}</strong></span>
          <button data-theme-toggle aria-label="Toggle theme">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/></svg>
          </button>
          <button class="btn btn-ghost btn-sm" onclick="logout()">Sign out</button>
        </div>
      </header>

      <nav class="app-sidebar" id="app-sidebar">
        <div class="nav-section">
          <div class="nav-section-label">Main</div>
          <a href="dashboard.html" class="nav-link">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7" rx="1"/><rect x="14" y="3" width="7" height="7" rx="1"/><rect x="3" y="14" width="7" height="7" rx="1"/><rect x="14" y="14" width="7" height="7" rx="1"/></svg>
            Dashboard
          </a>
          <a href="recipes.html" class="nav-link">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2a7 7 0 0 1 7 7c0 4-3 6-4 9H9c-1-3-4-5-4-9a7 7 0 0 1 7-7z"/><path d="M9 21h6M10 21v1h4v-1"/></svg>
            Recipes
          </a>
          <a href="pantry.html" class="nav-link">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 6h18M3 12h18M3 18h18"/><rect x="5" y="3" width="14" height="18" rx="2"/></svg>
            Pantry
          </a>
          <a href="shopping-list.html" class="nav-link">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M6 2L3 6v14a2 2 0 002 2h14a2 2 0 002-2V6l-3-4z"/><line x1="3" y1="6" x2="21" y2="6"/><path d="M16 10a4 4 0 01-8 0"/></svg>
            Shopping List
          </a>
        </div>
        ${admin ? `
        <div class="nav-section">
          <div class="nav-section-label">Admin</div>
          <a href="admin-ingredients.html" class="nav-link">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="3"/><path d="M12 2v3M12 19v3M4.22 4.22l2.12 2.12M17.66 17.66l2.12 2.12M2 12h3M19 12h3M4.22 19.78l2.12-2.12M17.66 6.34l2.12-2.12"/></svg>
            Ingredients
          </a>
          <a href="admin-categories.html" class="nav-link">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 4h6v6H4zM14 4h6v6h-6zM4 14h6v6H4zM14 14h6v6h-6z"/></svg>
            Categories
          </a>
          <a href="admin-users.html" class="nav-link">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 00-3-3.87M16 3.13a4 4 0 010 7.75"/></svg>
            Users
          </a>
        </div>` : ''}
        <div class="nav-section">
          <div class="nav-section-label">Account</div>
          <a href="profile.html" class="nav-link">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
            Profile
          </a>
        </div>
      </nav>

      <main class="app-main" id="app-main">
        <div id="page-content"></div>
      </main>
    </div>
  `;

  // Theme toggle
  (function(){
    const t = document.querySelector('[data-theme-toggle]'), r = document.documentElement;
    let d = r.getAttribute('data-theme') || (matchMedia('(prefers-color-scheme:dark)').matches ? 'dark' : 'light');
    r.setAttribute('data-theme', d);
    t && t.addEventListener('click', () => {
      d = d === 'dark' ? 'light' : 'dark'; r.setAttribute('data-theme', d);
    });
  })();

  // Mobile menu
  document.getElementById('mobile-menu-btn')?.addEventListener('click', () => {
    document.getElementById('app-sidebar').classList.toggle('open');
  });

  setActiveNav();
}
