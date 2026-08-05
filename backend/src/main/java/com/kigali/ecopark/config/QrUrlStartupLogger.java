package com.kigali.ecopark.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class QrUrlStartupLogger implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(QrUrlStartupLogger.class);

    @Value("${app.frontend-base-url}")
    private String frontendBaseUrl;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Tree QR codes point to: {}/scan/{{qrCodeId}}", frontendBaseUrl);
        if (frontendBaseUrl.contains("localhost") || frontendBaseUrl.contains("127.0.0.1")) {
            log.warn("PUBLIC_SITE_URL is localhost — phone QR scans will NOT work. "
                    + "Run scripts/phone-test.ps1 or set PUBLIC_SITE_URL to http://YOUR-PC-IP:5173");
        }
    }
}
