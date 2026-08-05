import { createContext, useContext, useEffect, useMemo, useState } from 'react';
import { fetchMe, loginRequest, registerRequest } from '../api/client';

const AuthContext = createContext(null);
const STORAGE_KEY = 'kigali-ecopark-auth';

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try {
      const raw = localStorage.getItem(STORAGE_KEY);
      return raw ? JSON.parse(raw) : null;
    } catch {
      return null;
    }
  });
  const [loading, setLoading] = useState(!!user?.token);

  useEffect(() => {
    if (!user?.token) {
      setLoading(false);
      return;
    }

    fetchMe(user.token)
      .then((profile) => {
        const next = { ...profile, token: user.token };
        setUser(next);
        localStorage.setItem(STORAGE_KEY, JSON.stringify(next));
      })
      .catch(() => {
        setUser(null);
        localStorage.removeItem(STORAGE_KEY);
      })
      .finally(() => setLoading(false));
  }, []);

  const login = async (email, password) => {
    const response = await loginRequest(email, password);
    setUser(response);
    localStorage.setItem(STORAGE_KEY, JSON.stringify(response));
    return response;
  };

  const register = async (fullName, email, password) => {
    const response = await registerRequest(fullName, email, password);
    setUser(response);
    localStorage.setItem(STORAGE_KEY, JSON.stringify(response));
    return response;
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem(STORAGE_KEY);
  };

  const value = useMemo(() => ({
    user,
    loading,
    isAuthenticated: !!user?.token,
    login,
    register,
    logout,
  }), [user, loading]);

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
}
