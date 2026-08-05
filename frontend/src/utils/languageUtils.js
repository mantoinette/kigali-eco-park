const SUPPORTED_LANGUAGES = [
  { code: 'en', label: 'English', patterns: ['en', 'english', 'eng'] },
  { code: 'rw', label: 'Kinyarwanda', patterns: ['rw', 'kin', 'kinyarwanda', 'ikinyarwanda'] },
  { code: 'fr', label: 'Français', patterns: ['fr', 'french', 'francais', 'français'] },
];

const DEFAULT = SUPPORTED_LANGUAGES[0];

export function resolveLanguage(input) {
  const raw = (input ?? '').trim();
  if (!raw) {
    return { code: DEFAULT.code, label: DEFAULT.label, supported: true, input: DEFAULT.label };
  }

  const normalized = raw.toLowerCase();

  for (const lang of SUPPORTED_LANGUAGES) {
    if (lang.patterns.some((pattern) => normalized === pattern || normalized.includes(pattern))) {
      return { code: lang.code, label: lang.label, supported: true, input: lang.label };
    }
  }

  return { code: DEFAULT.code, label: raw, supported: false, input: raw };
}

export function getLanguageSuggestions() {
  return SUPPORTED_LANGUAGES.map((lang) => lang.label);
}

export const DEFAULT_LANGUAGE_INPUT = DEFAULT.label;
export const DEFAULT_LANGUAGE_CODE = DEFAULT.code;
