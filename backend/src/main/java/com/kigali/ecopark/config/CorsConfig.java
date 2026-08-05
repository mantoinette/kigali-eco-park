package com.kigali.ecopark.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class CorsConfig {

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Value("${app.frontend-base-url:}")
    private String frontendBaseUrl;

    @Value("${app.cors.allow-local-network:false}")
    private boolean allowLocalNetwork;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        Set<String> patterns = new LinkedHashSet<>();

        Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .forEach(patterns::add);

        if (frontendBaseUrl != null && !frontendBaseUrl.isBlank()) {
            patterns.add(frontendBaseUrl.trim());
        }

        // Production frontend on Vercel (stable + preview deployments)
        patterns.add("https://kigali-eco-park.vercel.app");
        patterns.add("https://*.vercel.app");

        if (allowLocalNetwork) {
            patterns.addAll(List.of(
                    "http://localhost:*",
                    "http://127.0.0.1:*",
                    "http://192.168.*:*",
                    "http://10.*:*",
                    "http://172.*:*"
            ));
        }

        config.setAllowedOriginPatterns(new ArrayList<>(patterns));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
