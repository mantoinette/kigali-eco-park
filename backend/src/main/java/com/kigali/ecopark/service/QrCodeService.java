package com.kigali.ecopark.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.kigali.ecopark.dto.QrCodeResponseDto;
import com.kigali.ecopark.entity.Tree;
import com.kigali.ecopark.repository.TreeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Map;

@Service
public class QrCodeService {

    private final TreeRepository treeRepository;
    private final String frontendBaseUrl;

    public QrCodeService(
            TreeRepository treeRepository,
            @Value("${app.frontend-base-url}") String frontendBaseUrl
    ) {
        this.treeRepository = treeRepository;
        this.frontendBaseUrl = frontendBaseUrl.replaceAll("/$", "");
    }

    public QrCodeResponseDto generateQrCode(String slug) {
        Tree tree = treeRepository.findBySlugAndPublishedTrue(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tree not found"));

        String url = frontendBaseUrl + "/scan/" + tree.getQrCodeId();
        String base64 = generateBase64QrCode(url);

        return new QrCodeResponseDto(
                tree.getQrCodeId(),
                tree.getSlug(),
                tree.getScientificName(),
                url,
                base64
        );
    }

    public String buildTreeUrl(String slug) {
        Tree tree = treeRepository.findBySlugAndPublishedTrue(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tree not found"));
        return frontendBaseUrl + "/scan/" + tree.getQrCodeId();
    }

    private String generateBase64QrCode(String content) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(
                    content,
                    BarcodeFormat.QR_CODE,
                    300,
                    300,
                    Map.of(EncodeHintType.MARGIN, 1)
            );

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate QR code", e);
        }
    }
}
