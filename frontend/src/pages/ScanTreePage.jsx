import { Navigate, useParams } from 'react-router-dom';

/**
 * QR scan entry — always lands on the dedicated Tree Details page
 * (/trees/TREE-001) so Information, Images, Uses, Print Sign, and QR match.
 */
export default function ScanTreePage() {
  const { qrCodeId } = useParams();
  return <Navigate to={`/trees/${qrCodeId}`} replace />;
}
