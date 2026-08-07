import { useEffect, useRef, useState } from 'react';

function easeOutCubic(t) {
  return 1 - (1 - t) ** 3;
}

/**
 * Animates a number from 0 up to `value` when it enters the viewport.
 */
export default function CountUp({
  value,
  language = 'en',
  duration = 1600,
  className = '',
  suffix = '',
  prefix = '',
}) {
  const target = Number(value);
  const safeTarget = Number.isFinite(target) ? target : 0;
  const [display, setDisplay] = useState(0);
  const [started, setStarted] = useState(false);
  const ref = useRef(null);

  useEffect(() => {
    const node = ref.current;
    if (!node) return undefined;

    const observer = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting) {
          setStarted(true);
          observer.disconnect();
        }
      },
      { threshold: 0.35 }
    );

    observer.observe(node);
    return () => observer.disconnect();
  }, []);

  useEffect(() => {
    if (!started) return undefined;

    let frameId = 0;
    const start = performance.now();
    setDisplay(0);

    const tick = (now) => {
      const progress = Math.min(1, (now - start) / duration);
      const current = Math.round(safeTarget * easeOutCubic(progress));
      setDisplay(current);
      if (progress < 1) {
        frameId = requestAnimationFrame(tick);
      }
    };

    frameId = requestAnimationFrame(tick);
    return () => cancelAnimationFrame(frameId);
  }, [started, safeTarget, duration]);

  const locale = language === 'fr' ? 'fr-FR' : 'en-US';
  const formatted = display.toLocaleString(locale);

  return (
    <span ref={ref} className={className}>
      {prefix}
      {formatted}
      {suffix}
    </span>
  );
}
