package com.kigali.ecopark.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "trees")
public class Tree {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scientific_name", nullable = false, length = 200)
    private String scientificName;

    @Column(nullable = false, unique = true, length = 200)
    private String slug;

    @Column(name = "qr_code_id", nullable = false, unique = true, length = 50)
    private String qrCodeId;

    @Column(name = "is_published", nullable = false)
    private boolean published = true;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(length = 100)
    private String family;

    /** NATIVE, INTRODUCED, or UNKNOWN — used by Explore Trees filters. */
    @Column(name = "native_status", length = 20)
    private String nativeStatus = "UNKNOWN";

    /**
     * Visitor-facing tags (MEDICINAL, FRUIT, SHADE, TIMBER, FIBRE, ORNAMENTAL, WILDLIFE).
     * New trees pick up filters automatically when these are set in the database.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tree_categories", joinColumns = @JoinColumn(name = "tree_id"))
    @Column(name = "category", length = 40, nullable = false)
    private Set<String> categories = new LinkedHashSet<>();

    @Column(name = "typical_height", length = 50)
    private String typicalHeight;

    @Column(length = 120)
    private String origin;

    @Column(name = "age_estimate", length = 80)
    private String ageEstimate;

    private Double latitude;

    private Double longitude;

    @Column(name = "audio_url", length = 500)
    private String audioUrl;

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    @OneToMany(mappedBy = "tree", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TreeTranslation> translations = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tree", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    private Set<TreeImage> images = new LinkedHashSet<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(String qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getNativeStatus() {
        return nativeStatus;
    }

    public void setNativeStatus(String nativeStatus) {
        this.nativeStatus = nativeStatus;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public String getTypicalHeight() {
        return typicalHeight;
    }

    public void setTypicalHeight(String typicalHeight) {
        this.typicalHeight = typicalHeight;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getAgeEstimate() {
        return ageEstimate;
    }

    public void setAgeEstimate(String ageEstimate) {
        this.ageEstimate = ageEstimate;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Set<TreeTranslation> getTranslations() {
        return translations;
    }

    public void setTranslations(Set<TreeTranslation> translations) {
        this.translations = translations;
    }

    public Set<TreeImage> getImages() {
        return images;
    }

    public void setImages(Set<TreeImage> images) {
        this.images = images;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
