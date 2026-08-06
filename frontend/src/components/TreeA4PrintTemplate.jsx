import ParkLogo from './ParkLogo';

function formatMultilineText(text) {
  if (!text) return [];
  return String(text)
    .split(/\r?\n/)
    .map((s) => s.trim())
    .filter(Boolean);
}

function normalizeUses(uses) {
  if (!uses) return [];
  // Support newline, semicolon, or comma-separated data from DB.
  const parts = String(uses)
    .split(/[\n;•]+/g)
    .map((s) => s.trim())
    .filter(Boolean);

  // If it's a single long string with commas, split again.
  if (parts.length === 1 && parts[0].includes(',')) {
    return parts[0].split(',').map((s) => s.trim()).filter(Boolean);
  }

  return parts;
}

/**
 * Single reusable A4 portrait printable template.
 * For print: CSS hides everything except this container.
 */
export default function TreeA4PrintTemplate({ tree, qr }) {
  const qrUrl = qr?.url || '';
  const qrCodeBase64 = qr?.qrCodeBase64 || '';

  const usesList = normalizeUses(tree?.uses);
  const usesLines = usesList.length ? usesList : formatMultilineText(tree?.uses);

  return (
    <div className="tree-a4-print mx-auto border border-gray-200 bg-white">
      <div className="tree-a4-print-inner">
        <header className="tree-a4-header">
          <div className="tree-a4-logo">
            <ParkLogo />
          </div>
          <div className="tree-a4-qr">
            {qrCodeBase64 ? (
              <img src={qrCodeBase64} alt={`QR to ${tree?.commonName}`} className="tree-a4-qr-img" />
            ) : (
              <div className="tree-a4-qr-placeholder" />
            )}
          </div>
        </header>

        <main className="tree-a4-body">
          <div className="tree-a4-titleblock">
            <div className="tree-a4-code">{tree?.qrCodeId}</div>
            <div className="tree-a4-tree-name">{tree?.commonName}</div>
            {tree?.scientificName ? (
              <div className="tree-a4-scientific">
                <em>{tree.scientificName}</em>
              </div>
            ) : null}
          </div>

          <div className="tree-a4-meta">
            <div className="tree-a4-meta-row">
              <span className="tree-a4-meta-label">Common name:</span>
              <span className="tree-a4-meta-value">{tree?.commonName}</span>
            </div>
            <div className="tree-a4-meta-row">
              <span className="tree-a4-meta-label">Family:</span>
              <span className="tree-a4-meta-value">{tree?.family}</span>
            </div>
          </div>

          {tree?.shortDescription ? (
            <section className="tree-a4-section">
              <div className="tree-a4-section-title">Brief description</div>
              <div className="tree-a4-section-text">{tree.shortDescription}</div>
            </section>
          ) : null}

          {usesLines.length ? (
            <section className="tree-a4-section">
              <div className="tree-a4-section-title">Main uses</div>
              <ul className="tree-a4-uses">
                {usesLines.map((u, idx) => (
                  <li key={`${u}-${idx}`}>{u}</li>
                ))}
              </ul>
            </section>
          ) : null}
        </main>

        <footer className="tree-a4-footer">
          <div className="tree-a4-scan-text">Scan to learn more about this tree</div>
          {qrUrl ? (
            <div className="tree-a4-website">
              <span className="tree-a4-meta-label">Website:</span> <span className="tree-a4-website-url">{qrUrl}</span>
            </div>
          ) : null}
        </footer>
      </div>
    </div>
  );
}

