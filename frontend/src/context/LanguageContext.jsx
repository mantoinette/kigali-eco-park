import { createContext, useContext, useEffect, useMemo, useState } from 'react';

const LanguageContext = createContext(null);

const STORAGE_KEY = 'kigali-ecopark-lang';
const SUPPORTED = ['en', 'rw', 'fr'];

const LANGUAGE_LABELS = {
  en: 'English',
  rw: 'Kinyarwanda',
  fr: 'French',
};

export function LanguageProvider({ children }) {
  const [language, setLanguageState] = useState(
    () => localStorage.getItem(STORAGE_KEY) || 'en'
  );

  useEffect(() => {
    const code = SUPPORTED.includes(language) ? language : 'en';
    localStorage.setItem(STORAGE_KEY, code);
    document.documentElement.lang = code;
  }, [language]);

  const setLanguage = (code) => {
    if (SUPPORTED.includes(code)) {
      setLanguageState(code);
    }
  };

  const value = useMemo(() => ({
    language,
    setLanguage,
    languageLabels: LANGUAGE_LABELS,
    supportedLanguages: SUPPORTED,
  }), [language]);

  return (
    <LanguageContext.Provider value={value}>
      {children}
    </LanguageContext.Provider>
  );
}

export function useLanguage() {
  const context = useContext(LanguageContext);
  if (!context) {
    throw new Error('useLanguage must be used within LanguageProvider');
  }
  return context;
}
