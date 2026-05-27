// ─── CONFIG ───────────────────────────────────────────────────────────────────
const BASE_URL = 'http://localhost:8080/api';

// ─── TOKEN HELPERS ────────────────────────────────────────────────────────────
function getToken()  { return sessionStorage.getItem('jwt_token'); }
function getUser()   { return JSON.parse(sessionStorage.getItem('user') || 'null'); }
function saveAuth(token, username, role) {
  sessionStorage.setItem('jwt_token', token);
  sessionStorage.setItem('user', JSON.stringify({ username, role }));
}
function clearAuth() {
  sessionStorage.removeItem('jwt_token');
  sessionStorage.removeItem('user');
}
function isAdmin() { const u = getUser(); return u && u.role === 'ROLE_ADMIN'; }
function isLoggedIn() { return !!getToken(); }

// ─── FETCH WRAPPER ────────────────────────────────────────────────────────────
async function request(method, path, body = null, auth = true) {
  const headers = { 'Content-Type': 'application/json' };
  if (auth) headers['Authorization'] = 'Bearer ' + getToken();
  const opts = { method, headers };
  if (body) opts.body = JSON.stringify(body);
  const res = await fetch(BASE_URL + path, opts);
  if (res.status === 204) return null;
  const data = await res.json().catch(() => null);
  if (!res.ok) {
    const msg = data?.message || data?.error || `Error ${res.status}`;
    throw new Error(msg);
  }
  return data;
}

// ─── AUTH ─────────────────────────────────────────────────────────────────────
const Auth = {
  login:    (email, password)             => request('POST', '/auth/login',    { email, password }, false),
  register: (username, email, password)   => request('POST', '/auth/register', { username, email, password }, false),
};

// ─── USERS ────────────────────────────────────────────────────────────────────
const Users = {
  getAll:          (page=0, size=20)          => request('GET',    `/users?page=${page}&size=${size}&sort=username`),
  getById:         (id)                       => request('GET',    `/users/${id}`),
  patch:           (id, dto)                  => request('PATCH',  `/users/${id}`, dto),
  changePassword:  (id, dto)                  => request('PUT',    `/users/${id}/password`, dto),
  delete:          (id)                       => request('DELETE', `/users/${id}`),
};

// ─── CATEGORIES ───────────────────────────────────────────────────────────────
const Categories = {
  getAll:   ()           => request('GET',    '/categories'),
  getById:  (id)         => request('GET',    `/categories/${id}`),
  create:   (name)       => request('POST',   '/categories', { name }),
  update:   (id, name)   => request('PUT',    `/categories/${id}`, { name }),
  delete:   (id)         => request('DELETE', `/categories/${id}`),
};

// ─── INGREDIENTS ──────────────────────────────────────────────────────────────
const Ingredients = {
  getAll:   (page=0, size=20) => request('GET',    `/ingredients?page=${page}&size=${size}&sort=name`),
  getById:  (id)              => request('GET',    `/ingredients/${id}`),
  create:   (name, categoryId)=> request('POST',   '/ingredients', { name, categoryId }),
  update:   (id, name, categoryId) => request('PUT', `/ingredients/${id}`, { name, categoryId }),
  delete:   (id)              => request('DELETE', `/ingredients/${id}`),
};

// ─── RECIPES ──────────────────────────────────────────────────────────────────
const Recipes = {
  getAll: (filters={}, page=0, size=10) => {
    const params = new URLSearchParams({ page, size, sort: 'title' });
    Object.entries(filters).forEach(([k,v]) => { if (v !== null && v !== '' && v !== undefined) params.set(k, v); });
    return request('GET', `/recipes?${params}`);
  },
  getById:       (id)          => request('GET',    `/recipes/${id}`),
  getByIngredient: (ingId, page=0) => request('GET', `/recipes/by-ingredient/${ingId}?page=${page}&size=10`),
  getMatches:    (page=0)      => request('GET',    `/recipes/matches?page=${page}&size=10`),
  create:        (userId, dto) => request('POST',   `/recipes/users/${userId}`, dto),
  patch:         (id, dto)     => request('PATCH',  `/recipes/${id}`, dto),
  delete:        (id)          => request('DELETE', `/recipes/${id}`),
};

// ─── PANTRY ───────────────────────────────────────────────────────────────────
const Pantry = {
  getAll:            (userId, page=0, size=20) => request('GET',    `/users/${userId}/pantry?page=${page}&size=${size}`),
  getSortedByCategory: (userId, page=0)        => request('GET',    `/users/${userId}/pantry/sorted-by-category?page=${page}&size=20`),
  getByCategory:     (userId, catId)           => request('GET',    `/users/${userId}/pantry/category/${catId}`),
  add:               (userId, dto)             => request('POST',   `/users/${userId}/pantry`, dto),
  patch:             (userId, itemId, dto)     => request('PATCH',  `/users/${userId}/pantry/${itemId}`, dto),
  delete:            (userId, itemId)          => request('DELETE', `/users/${userId}/pantry/${itemId}`),
};

// ─── SHOPPING LIST ────────────────────────────────────────────────────────────
const ShoppingList = {
  getAll:            (userId, page=0)  => request('GET',    `/users/${userId}/shopping-list?page=${page}&size=20`),
  getSortedByCategory: (userId, page=0)=> request('GET',    `/users/${userId}/shopping-list/sorted-by-category?page=${page}&size=20`),
  getUnchecked:      (userId)          => request('GET',    `/users/${userId}/shopping-list/unchecked`),
  add:               (userId, dto)     => request('POST',   `/users/${userId}/shopping-list`, dto),
  addFromRecipe:     (userId, recipeId)=> request('POST',   `/users/${userId}/shopping-list/from-recipe/${recipeId}`),
  patch:             (userId, itemId, dto) => request('PATCH', `/users/${userId}/shopping-list/${itemId}`, dto),
  toggle:            (userId, itemId)  => request('PATCH',  `/users/${userId}/shopping-list/${itemId}/toggle`),
  delete:            (userId, itemId)  => request('DELETE', `/users/${userId}/shopping-list/${itemId}`),
  clearAll:          (userId)          => request('DELETE', `/users/${userId}/shopping-list`),
  clearChecked:      (userId)          => request('DELETE', `/users/${userId}/shopping-list/checked`),
};
