const API_BASE = import.meta.env.VITE_API_URL || '/api';

const sleep = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

/** Ping the API until Render free tier wakes (or timeout). Free tier can take 3–5 minutes. */
export async function wakeApi(maxWaitMs = 300000) {
  const started = Date.now();
  let delayMs = 5000;
  while (Date.now() - started < maxWaitMs) {
    try {
      const response = await fetch(`${API_BASE}/health`, { method: 'GET' });
      if (response.ok) return true;
    } catch {
      // Backend sleeping — retry until timeout.
    }
    await sleep(delayMs);
    delayMs = Math.min(delayMs + 5000, 25000);
  }
  return false;
}

async function requestWithRetry(path, options = {}, attempts = 3) {
  let lastError;
  for (let i = 0; i < attempts; i += 1) {
    try {
      return await request(path, options);
    } catch (err) {
      lastError = err;
      if (i < attempts - 1) await sleep(15000);
    }
  }
  throw lastError;
}

async function request(path, options = {}) {
  const { headers: customHeaders, ...rest } = options;
  const response = await fetch(`${API_BASE}${path}`, {
    ...rest,
    headers: {
      'Content-Type': 'application/json',
      ...(customHeaders || {}),
    },
  });

  if (!response.ok) {
    let message = `Request failed: ${response.status}`;
    try {
      const text = await response.text();
      if (text) {
        try {
          const json = JSON.parse(text);
          message = json.message || json.error || text;
        } catch {
          message = text;
        }
      }
    } catch {
      // keep default message
    }
    throw new Error(message);
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}

export function fetchLanguages() {
  return request('/languages');
}

export function fetchTrees(lang) {
  return request(`/trees?lang=${encodeURIComponent(lang)}`);
}

export function fetchTreeCatalog({
  lang = 'en',
  q = '',
  family = '',
  category = '',
  nativeStatus = '',
  sort = 'park',
  page = 0,
  size = 12,
} = {}) {
  const params = new URLSearchParams();
  params.set('lang', lang);
  params.set('page', String(page));
  params.set('size', String(size));
  if (q) params.set('q', q);
  if (family) params.set('family', family);
  if (category) params.set('category', category);
  if (nativeStatus) params.set('nativeStatus', nativeStatus);
  if (sort) params.set('sort', sort);
  return requestWithRetry(`/trees/catalog?${params.toString()}`);
}

export function fetchTreeFilters() {
  return request('/trees/filters');
}

export function searchTrees(query, lang) {
  return request(`/trees/search?q=${encodeURIComponent(query)}&lang=${encodeURIComponent(lang)}`);
}

export function fetchMapMarkers(lang) {
  return request(`/trees/map?lang=${encodeURIComponent(lang)}`);
}

export function fetchSiteStats() {
  return request('/trees/stats');
}

export function fetchTreeById(id, lang) {
  return request(`/trees/${encodeURIComponent(id)}?lang=${encodeURIComponent(lang)}`);
}

export function fetchTreeBySlug(slug, lang) {
  return request(`/trees/slug/${encodeURIComponent(slug)}?lang=${encodeURIComponent(lang)}`);
}

export function fetchTreeByQrCode(qrCodeId, lang) {
  return request(`/trees/qr/${encodeURIComponent(qrCodeId)}?lang=${encodeURIComponent(lang)}`);
}

/** Public visitor landing for park QR labels (opaque access token). */
export function fetchTreeByAccessToken(token, lang) {
  return request(`/trees/access/${encodeURIComponent(token)}?lang=${encodeURIComponent(lang)}`);
}

/** Admin-only: generate printable QR image for a tree slug. */
export function fetchQrCode(slug, authToken) {
  if (!authToken) {
    return Promise.reject(new Error('Admin sign-in required to generate QR codes'));
  }
  return request(`/qr/${encodeURIComponent(slug)}`, {
    headers: {
      Authorization: `Bearer ${authToken}`,
    },
  });
}

export function registerRequest(fullName, email, password) {
  return request('/auth/register', {
    method: 'POST',
    body: JSON.stringify({ fullName, email, password }),
  });
}

export function loginRequest(email, password) {
  return request('/auth/login', {
    method: 'POST',
    body: JSON.stringify({ email, password }),
  });
}

/** Admin-only sign-in — rejects non-ADMIN accounts on the backend. */
export function loginAdminRequest(email, password) {
  return request('/auth/admin/login', {
    method: 'POST',
    body: JSON.stringify({ email, password }),
  });
}

export function fetchMe(token) {
  return request('/auth/me', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
}

/** Public: submit contact / QR request form. */
export function submitContactRequest(payload) {
  return request('/contact', {
    method: 'POST',
    body: JSON.stringify(payload),
  });
}

/** Admin: list contact requests. */
export function fetchContactRequests(token, { status, requestType } = {}) {
  const params = new URLSearchParams();
  if (status) params.set('status', status);
  if (requestType) params.set('requestType', requestType);
  const qs = params.toString();
  return request(`/contact${qs ? `?${qs}` : ''}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
}

export function fetchContactStats(token) {
  return request('/contact/stats', {
    headers: { Authorization: `Bearer ${token}` },
  });
}

export function updateContactRequest(token, id, payload) {
  return request(`/contact/${id}`, {
    method: 'PATCH',
    headers: { Authorization: `Bearer ${token}` },
    body: JSON.stringify(payload),
  });
}

export function deleteContactRequest(token, id) {
  return request(`/contact/${id}`, {
    method: 'DELETE',
    headers: { Authorization: `Bearer ${token}` },
  });
}

function adminHeaders(token) {
  return { Authorization: `Bearer ${token}` };
}

export function fetchAdminTrees(token, lang = 'en') {
  return request(`/admin/trees?lang=${encodeURIComponent(lang)}`, {
    headers: adminHeaders(token),
  });
}

export function fetchAdminTree(token, id, lang = 'en') {
  return request(`/admin/trees/${encodeURIComponent(id)}?lang=${encodeURIComponent(lang)}`, {
    headers: adminHeaders(token),
  });
}

export function createAdminTree(token, payload) {
  return request('/admin/trees', {
    method: 'POST',
    headers: adminHeaders(token),
    body: JSON.stringify(payload),
  });
}

export function updateAdminTree(token, id, payload) {
  return request(`/admin/trees/${encodeURIComponent(id)}`, {
    method: 'PUT',
    headers: adminHeaders(token),
    body: JSON.stringify(payload),
  });
}

export function deleteAdminTree(token, id) {
  return request(`/admin/trees/${encodeURIComponent(id)}`, {
    method: 'DELETE',
    headers: adminHeaders(token),
  });
}

export function fetchAdminLanguages(token) {
  return request('/admin/languages', {
    headers: adminHeaders(token),
  });
}

export function createAdminLanguage(token, payload) {
  return request('/admin/languages', {
    method: 'POST',
    headers: adminHeaders(token),
    body: JSON.stringify(payload),
  });
}

export function updateAdminLanguage(token, code, payload) {
  return request(`/admin/languages/${encodeURIComponent(code)}`, {
    method: 'PATCH',
    headers: adminHeaders(token),
    body: JSON.stringify(payload),
  });
}

export function deleteAdminLanguage(token, code) {
  return request(`/admin/languages/${encodeURIComponent(code)}`, {
    method: 'DELETE',
    headers: adminHeaders(token),
  });
}

export function fetchAdminUsers(token) {
  return request('/admin/users', {
    headers: adminHeaders(token),
  });
}

export function createAdminUser(token, payload) {
  return request('/admin/users', {
    method: 'POST',
    headers: adminHeaders(token),
    body: JSON.stringify(payload),
  });
}

export function updateAdminUser(token, id, payload) {
  return request(`/admin/users/${encodeURIComponent(id)}`, {
    method: 'PATCH',
    headers: adminHeaders(token),
    body: JSON.stringify(payload),
  });
}

export function deleteAdminUser(token, id) {
  return request(`/admin/users/${encodeURIComponent(id)}`, {
    method: 'DELETE',
    headers: adminHeaders(token),
  });
}
