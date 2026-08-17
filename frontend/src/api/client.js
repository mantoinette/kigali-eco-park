const API_BASE = import.meta.env.VITE_API_URL || '/api';

const sleep = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

/** Ping the API until Render free tier wakes (or timeout). */
export async function wakeApi(maxWaitMs = 120000) {
  const started = Date.now();
  let delayMs = 4000;
  while (Date.now() - started < maxWaitMs) {
    try {
      const response = await fetch(`${API_BASE}/health`, { method: 'GET' });
      if (response.ok) return true;
    } catch {
      // Backend sleeping, redeploying, or unreachable — retry.
    }
    await sleep(delayMs);
    delayMs = Math.min(delayMs + 3000, 20000);
  }
  return false;
}

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers || {}),
    },
    ...options,
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
  return request(`/trees/catalog?${params.toString()}`);
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

export function fetchQrCode(slug) {
  return request(`/qr/${encodeURIComponent(slug)}`);
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

export function fetchMe(token) {
  return request('/auth/me', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
}
