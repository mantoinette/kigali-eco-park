import { useEffect, useState } from 'react';
import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';
import { getLanguageSuggestions } from '../utils/languageUtils';

export default function LanguageSwitcher({ compact = false, showHint = false }) {
  const { language, languageInput, languageSupported, applyLanguageInput } = useLanguage();
  const [draft, setDraft] = useState(languageInput);
  const suggestions = getLanguageSuggestions();
  const listId = compact ? 'lang-suggestions-compact' : 'lang-suggestions';

  useEffect(() => {
    setDraft(languageInput);
  }, [languageInput]);

  const commit = () => {
    applyLanguageInput(draft);
  };

  return (
    <div className={`language-switcher ${compact ? 'compact' : ''}`}>
      <label htmlFor="lang-input" className={compact ? 'sr-only' : 'language-label'}>
        {t(language, 'languageLabel')}
      </label>
      <div className="language-input-wrap">
        <span className="language-input-icon" aria-hidden="true">🌐</span>
        <input
          id="lang-input"
          type="text"
          list={listId}
          value={draft}
          onChange={(e) => setDraft(e.target.value)}
          onBlur={commit}
          onKeyDown={(e) => {
            if (e.key === 'Enter') {
              e.preventDefault();
              commit();
            }
          }}
          placeholder={t(language, 'languagePlaceholder')}
          aria-label={t(language, 'languageLabel')}
          autoComplete="off"
          spellCheck={false}
        />
        <button type="button" className="language-apply-btn" onClick={commit}>
          {compact ? '✓' : t(language, 'languageApply')}
        </button>
      </div>
      {showHint && !languageSupported && (
        <p className="language-fallback-note">{t(language, 'languageFallback')}</p>
      )}
      {!compact && (
        <p className="language-hint">{t(language, 'languageHint')}</p>
      )}
      <datalist id={listId}>
        {suggestions.map((name) => (
          <option key={name} value={name} />
        ))}
      </datalist>
    </div>
  );
}
