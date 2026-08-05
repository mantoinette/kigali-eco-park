package com.kigali.ecopark.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class TreeImageAcquisitionService {

    private static final Logger log = LoggerFactory.getLogger(TreeImageAcquisitionService.class);
    private static final String WIKIMEDIA_API =
            "https://commons.wikimedia.org/w/api.php?action=query&generator=search" +
                    "&gsrsearch=%s&gsrnamespace=6&gsrlimit=%d&prop=imageinfo&iiprop=url" +
                    "&iiurlwidth=1280&format=json";

    private final Path uploadRoot;
    private final String publicBaseUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public TreeImageAcquisitionService(
            @Value("${app.upload.root:uploads}") String uploadRoot,
            @Value("${app.upload.public-base-url:http://localhost:8080}") String publicBaseUrl
    ) {
        this.uploadRoot = Path.of(uploadRoot).toAbsolutePath().normalize();
        this.publicBaseUrl = publicBaseUrl.replaceAll("/$", "");
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public record ImageRequest(String sourceUrl, String caption, boolean primary, int displayOrder) {}

    public record AcquiredImage(String publicUrl, String caption, boolean primary, int displayOrder) {}

    public List<AcquiredImage> acquireImages(String slug, String scientificName, List<ImageRequest> requests) {
        try {
            Files.createDirectories(uploadRoot.resolve("trees").resolve(slug));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create upload directory for " + slug, e);
        }

        List<AcquiredImage> acquired = new ArrayList<>();
        List<String> wikimediaCandidates = new ArrayList<>();

        for (ImageRequest request : requests) {
            if (request.sourceUrl() != null && !request.sourceUrl().isBlank()) {
                acquireFromUrl(slug, request).ifPresent(acquired::add);
            }
        }

        if (acquired.isEmpty()) {
            wikimediaCandidates.addAll(searchWikimedia(scientificName, 3));
            for (int i = 0; i < wikimediaCandidates.size(); i++) {
                String caption = scientificName + " — image " + (i + 1);
                ImageRequest request = new ImageRequest(
                        wikimediaCandidates.get(i),
                        caption,
                        i == 0,
                        i + 1
                );
                acquireFromUrl(slug, request).ifPresent(acquired::add);
            }
        }

        if (acquired.isEmpty()) {
            log.warn("No images found for {}. Add images manually to uploads/trees/{}", scientificName, slug);
        }

        return acquired;
    }

    private Optional<AcquiredImage> acquireFromUrl(String slug, ImageRequest request) {
        try {
            if (request.sourceUrl().startsWith("direct:")) {
                String publicUrl = request.sourceUrl().substring("direct:".length());
                return Optional.of(new AcquiredImage(
                        publicUrl,
                        request.caption(),
                        request.primary(),
                        request.displayOrder()
                ));
            }

            String extension = extensionFromUrl(request.sourceUrl());
            String filename = "image-" + request.displayOrder() + extension;
            Path destination = uploadRoot.resolve("trees").resolve(slug).resolve(filename);

            if (!Files.exists(destination)) {
                if (request.sourceUrl().startsWith("local:")) {
                    Path source = Path.of(request.sourceUrl().substring("local:".length())).toAbsolutePath().normalize();
                    Files.copy(source, destination);
                } else {
                    download(request.sourceUrl(), destination);
                }
                log.info("Stored tree image for {} -> {}", slug, destination);
            }

            // Relative path so the SPA (Vite proxy / same host) can load images
            // without depending on a brittle LAN IP in PUBLIC_API_URL.
            String publicUrl = "/uploads/trees/" + slug + "/" + filename;
            return Optional.of(new AcquiredImage(
                    publicUrl,
                    request.caption(),
                    request.primary(),
                    request.displayOrder()
            ));
        } catch (Exception e) {
            log.warn("Failed to acquire image for {} from {}: {}", slug, request.sourceUrl(), e.getMessage());
            return Optional.empty();
        }
    }

    private void download(String sourceUrl, Path destination) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(sourceUrl))
                .timeout(Duration.ofSeconds(60))
                .header("User-Agent", "KigaliEcoPark/1.0 (educational tree guide)")
                .GET()
                .build();

        HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
        if (response.statusCode() >= 400) {
            throw new IOException("HTTP " + response.statusCode() + " for " + sourceUrl);
        }

        try (InputStream input = response.body()) {
            Files.copy(input, destination);
        }
    }

    List<String> searchWikimedia(String scientificName, int limit) {
        try {
            String query = URLEncoder.encode(scientificName, StandardCharsets.UTF_8);
            String apiUrl = String.format(Locale.ROOT, WIKIMEDIA_API, query, limit);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .timeout(Duration.ofSeconds(20))
                    .header("User-Agent", "KigaliEcoPark/1.0 (educational tree guide)")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 400) {
                return List.of();
            }

            JsonNode pages = objectMapper.readTree(response.body()).path("query").path("pages");
            List<String> urls = new ArrayList<>();
            pages.fields().forEachRemaining(entry -> {
                JsonNode imageInfo = entry.getValue().path("imageinfo");
                if (imageInfo.isArray() && !imageInfo.isEmpty()) {
                    String url = imageInfo.get(0).path("thumburl").asText(null);
                    if (url == null || url.isBlank()) {
                        url = imageInfo.get(0).path("url").asText(null);
                    }
                    if (url != null && !url.isBlank()) {
                        urls.add(url);
                    }
                }
            });
            return urls;
        } catch (Exception e) {
            log.warn("Wikimedia search failed for {}: {}", scientificName, e.getMessage());
            return List.of();
        }
    }

    private String extensionFromUrl(String url) {
        if (url.startsWith("local:")) {
            String path = url.substring("local:".length()).toLowerCase(Locale.ROOT);
            if (path.endsWith(".png")) {
                return ".png";
            }
            if (path.endsWith(".webp")) {
                return ".webp";
            }
            return ".jpg";
        }
        String path = URI.create(url).getPath().toLowerCase(Locale.ROOT);
        if (path.endsWith(".png")) {
            return ".png";
        }
        if (path.endsWith(".webp")) {
            return ".webp";
        }
        return ".jpg";
    }
}
