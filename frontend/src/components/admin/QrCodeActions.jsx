import { downloadDataUri, downloadSvg, fileStem, printQrOnly } from '../utils/qrDownload';
import { t } from '../i18n/ui';

export default function QrCodeActions({ qr, language }) {
  if (!qr?.qrCodeBase64) return null;

  const stem = fileStem(qr.scientificName, qr.slug);

  return (
    <div className="w-full max-w-md space-y-3">
      <button
        type="button"
        className="btn btn-primary w-full !rounded-xl py-3.5 text-base"
        onClick={() => downloadDataUri(qr.qrCodeBase64, `${stem}-qr.png`)}
      >
        {t(language, 'downloadHdQr')}
      </button>
      <button
        type="button"
        className="btn btn-secondary w-full !rounded-xl py-3.5 text-base"
        onClick={() => printQrOnly(qr.qrCodeBase64)}
      >
        {t(language, 'printQrCode')}
      </button>
      {qr.qrCodeSvg && (
        <button
          type="button"
          className="w-full rounded-xl border border-gray-200 bg-white py-3 text-sm font-semibold text-gray-700 hover:bg-gray-50"
          onClick={() => downloadSvg(qr.qrCodeSvg, `${stem}-qr.svg`)}
        >
          {t(language, 'downloadSvgQr')}
        </button>
      )}
    </div>
  );
}
