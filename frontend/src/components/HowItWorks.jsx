import { useLanguage } from '../context/LanguageContext';
import { t } from '../i18n/ui';

const STEPS = ['step1', 'step2', 'step3'];
const ICONS = ['📱', '📷', '📖'];

export default function HowItWorks() {
  const { language } = useLanguage();

  return (
    <section className="how-it-works">
      <div className="section-container">
        <header className="section-header">
          <span className="section-eyebrow">{t(language, 'howItWorks')}</span>
          <h2>{t(language, 'howItWorksTitle')}</h2>
          <p>{t(language, 'howItWorksIntro')}</p>
        </header>
        <div className="steps-grid">
          {STEPS.map((step, i) => (
            <article key={step} className="step-card">
              <div className="step-number">{i + 1}</div>
              <div className="step-icon" aria-hidden="true">{ICONS[i]}</div>
              <h3>{t(language, `${step}Title`)}</h3>
              <p>{t(language, `${step}Text`)}</p>
            </article>
          ))}
        </div>
      </div>
    </section>
  );
}
