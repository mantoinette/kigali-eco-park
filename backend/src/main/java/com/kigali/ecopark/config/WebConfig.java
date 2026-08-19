package com.kigali.ecopark.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final Path uploadRoot;

    public WebConfig(@Value("${app.upload.root:uploads}") String uploadRoot) {
        this.uploadRoot = Path.of(uploadRoot).toAbsolutePath().normalize();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = uploadRoot.toUri().toString();
        if (!location.endsWith("/")) {
            location = location + "/";
        }
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(location);

        // ONLY classpath — never serve stale uploads/media duplicates from Render disk.
        // Each tree has unique files: TREE-001 … TREE-014 (Umutoyi / Chrysophyllum gorungosanum).
        registry.addResourceHandler("/media/**")
                .addResourceLocations("classpath:/static/media/")
                .setCachePeriod(0);
    }
}
