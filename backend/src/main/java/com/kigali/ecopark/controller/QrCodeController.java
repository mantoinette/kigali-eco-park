package com.kigali.ecopark.controller;

import com.kigali.ecopark.dto.QrCodeResponseDto;
import com.kigali.ecopark.service.QrCodeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/qr")
public class QrCodeController {

    private final QrCodeService qrCodeService;

    public QrCodeController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @GetMapping("/{slug}")
    public QrCodeResponseDto getQrCode(@PathVariable String slug) {
        return qrCodeService.generateQrCode(slug);
    }
}
