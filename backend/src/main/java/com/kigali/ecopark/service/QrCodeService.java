package com.kigali.ecopark.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.kigali.ecopark.dto.QrCodeResponseDto;
import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.repository.TreeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Service
public class QrCodeService {

    private static final int HD_PNG_SIZE = 2400;
    private static final int QUIET_ZONE_MODULES = 4;

    private final TreeRepository treeRepository;
    private final String frontendBaseUrl;

    public QrCodeService(
            TreeRepository treeRepository,
            @Value("${app.frontend-base-url}") String frontendBaseUrl
    ) {
        this.treeRepository = treeRepository;
        this.frontendBaseUrl = frontendBaseUrl.replaceAll("/$", "");
    }

    /**
     * Admin-only: high-resolution QR whose URL opens the public tree page /trees/{slug}.
     */
    @Transactional
    public QrCodeResponseDto generateQrCode(String slug) {
        Tree tree = treeRepository.findBySlugWithDetails(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tree not found"));

        ensureAccessToken(tree);
        String url = frontendBaseUrl + "/trees/" + tree.getSlug();
        QrAssets assets = generateQrAssets(url);

        return new QrCodeResponseDto(
                tree.getQrCodeId(),
                tree.getSlug(),
                tree.getScientificName(),
                url,
                assets.pngDataUri(),
                assets.svg()
        );
    }

    @Transactional
    public void ensureAccessTokensForAllPublished() {
        for (Tree tree : treeRepository.findAll()) {
            if (tree.isPublished()) {
                ensureAccessToken(tree);
            }
        }
    }

    private String ensureAccessToken(Tree tree) {
        if (tree.getQrAccessToken() != null && !tree.getQrAccessToken().isBlank()) {
            return tree.getQrAccessToken();
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        tree.setQrAccessToken(token);
        treeRepository.save(tree);
        return token;
    }

    private QrAssets generateQrAssets(String content) {
        try {
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, QUIET_ZONE_MODULES);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix matrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, 0, 0, hints);
            return new QrAssets(toHdPngDataUri(matrix), toSvg(matrix));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate QR code", e);
        }
    }

    private String toHdPngDataUri(BitMatrix matrix) throws Exception {
        BufferedImage source = MatrixToImageWriter.toBufferedImage(
                matrix,
                new MatrixToImageConfig(0xFF000000, 0xFFFFFFFF)
        );
        BufferedImage hd = new BufferedImage(HD_PNG_SIZE, HD_PNG_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = hd.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        graphics.drawImage(source, 0, 0, HD_PNG_SIZE, HD_PNG_SIZE, null);
        graphics.dispose();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(hd, "PNG", outputStream);
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    private String toSvg(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        StringBuilder path = new StringBuilder(width * height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    path.append("M").append(x).append(',').append(y).append("h1v1h-1z");
                }
            }
        }
        return """
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 %d %d" shape-rendering="crispEdges">
                  <rect width="100%%" height="100%%" fill="#ffffff"/>
                  <path fill="#000000" d="%s"/>
                </svg>
                """.formatted(width, height, path);
    }

    private record QrAssets(String pngDataUri, String svg) {}
}
