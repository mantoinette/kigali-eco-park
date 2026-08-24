export function fileStem(scientificName, slug) {
  const raw = scientificName || slug || 'tree-qr';
  return raw.toLowerCase().replace(/[^a-z0-9]+/g, '-').replace(/^-|-$/g, '');
}

export function downloadDataUri(dataUri, filename) {
  const link = document.createElement('a');
  link.href = dataUri;
  link.download = filename;
  document.body.appendChild(link);
  link.click();
  link.remove();
}

export function downloadSvg(svg, filename) {
  const blob = new Blob([svg], { type: 'image/svg+xml;charset=utf-8' });
  const url = URL.createObjectURL(blob);
  downloadDataUri(url, filename);
  URL.revokeObjectURL(url);
}

/** Print only the QR image — no admin chrome, plaque, or website layout. */
export function printQrOnly(dataUri) {
  const win = window.open('', '_blank', 'noopener,noreferrer,width=720,height=800');
  if (!win) return;
  win.document.open();
  win.document.write(`<!DOCTYPE html>
<html>
  <head>
    <title>QR</title>
    <style>
      @page { size: auto; margin: 12mm; }
      html, body {
        margin: 0;
        height: 100%;
        background: #ffffff;
      }
      body {
        display: flex;
        align-items: center;
        justify-content: center;
      }
      img {
        width: 90mm;
        height: 90mm;
        image-rendering: pixelated;
      }
    </style>
  </head>
  <body>
    <img src="${dataUri}" alt="QR code" onload="window.focus(); window.print();" />
  </body>
</html>`);
  win.document.close();
}
