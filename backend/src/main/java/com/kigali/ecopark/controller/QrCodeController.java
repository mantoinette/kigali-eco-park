package com.kigali.ecopark.controller;

import com.kigali.ecopark.dto.QrCodeResponseDto;
import com.kigali.ecopark.service.AuthService;
import com.kigali.ecopark.service.QrCodeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * QR image generation is an admin asset — not part of the public visitor site.
 */
@RestController
@RequestMapping("/api/qr")
public class QrCodeController {

    private final QrCodeService qrCodeService;
    private final AuthService authService;

    public QrCodeController(QrCodeService qrCodeService, AuthService authService) {
        this.qrCodeService = qrCodeService;
        this.authService = authService;
    }

    @GetMapping("/{slug}")
    public QrCodeResponseDto getQrCode(
            @PathVariable String slug,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        authService.requireAdmin(authorization);
        return qrCodeService.generateQrCode(slug);
    }
}
