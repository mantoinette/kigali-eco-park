import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

export default function FaqPage() {
  const { language } = useLanguage();
  const items = t(language, 'faqItems');

  return (
    <div className="page">
      <div className="page-hero-strip">
        <div className="section-container">
          <span className="section-eyebrow">FAQ</span>
          <h1>{t(language, 'faqTitle')}</h1>
          <p>{t(language, 'faqIntro')}</p>
        </div>
      </div>

      <div className="section-container page-body">
        <div className="faq-list">
          {Array.isArray(items) && items.map((item, index) => (
            <details key={item.q} className="faq-item" open={index === 0}>
              <summary>
                <span className="faq-q">{item.q}</span>
                <span className="faq-chevron" aria-hidden="true">+</span>
              </summary>
              <p>{item.a}</p>
            </details>
          ))}
        </div>
      </div>
    </div>
  );
}
