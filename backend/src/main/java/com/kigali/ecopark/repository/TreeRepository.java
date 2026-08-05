package com.kigali.ecopark.repository;

import com.kigali.ecopark.entity.Tree;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TreeRepository extends JpaRepository<Tree, Long> {

    Optional<Tree> findBySlug(String slug);

    Optional<Tree> findBySlugAndPublishedTrue(String slug);

    Optional<Tree> findByQrCodeIdAndPublishedTrue(String qrCodeId);

    @Query("""
            SELECT DISTINCT t FROM Tree t
            LEFT JOIN FETCH t.translations
            LEFT JOIN FETCH t.images
            WHERE t.published = true
            ORDER BY t.displayOrder ASC, t.scientificName ASC
            """)
    List<Tree> findAllPublishedWithDetails();

    @Query("""
            SELECT t FROM Tree t
            LEFT JOIN FETCH t.translations
            LEFT JOIN FETCH t.images
            WHERE t.slug = :slug
            """)
    Optional<Tree> findBySlugWithDetails(@Param("slug") String slug);

    @Query("""
            SELECT t FROM Tree t
            LEFT JOIN FETCH t.translations
            LEFT JOIN FETCH t.images
            WHERE t.slug = :slug AND t.published = true
            """)
    Optional<Tree> findPublishedBySlugWithDetails(@Param("slug") String slug);

    @Query("""
            SELECT t FROM Tree t
            LEFT JOIN FETCH t.translations
            LEFT JOIN FETCH t.images
            WHERE t.qrCodeId = :qrCodeId AND t.published = true
            """)
    Optional<Tree> findPublishedByQrCodeIdWithDetails(@Param("qrCodeId") String qrCodeId);

    @Query("""
            SELECT t FROM Tree t
            LEFT JOIN FETCH t.translations
            LEFT JOIN FETCH t.images
            WHERE t.id = :id AND t.published = true
            """)
    Optional<Tree> findPublishedByIdWithDetails(@Param("id") Long id);

    @Query("""
            SELECT DISTINCT t FROM Tree t
            LEFT JOIN FETCH t.translations tr
            LEFT JOIN FETCH t.images
            WHERE t.published = true
            AND (
                LOWER(t.scientificName) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(t.family) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(t.qrCodeId) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(t.slug) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(tr.commonName) LIKE LOWER(CONCAT('%', :query, '%'))
            )
            ORDER BY t.displayOrder ASC, t.scientificName ASC
            """)
    List<Tree> searchPublished(@Param("query") String query);

    /**
     * Paginated catalog for Explore Trees. Avoids JOIN FETCH so pagination stays accurate at scale.
     */
    @Query("""
            SELECT DISTINCT t FROM Tree t
            LEFT JOIN t.translations tr
            LEFT JOIN t.categories cat
            WHERE t.published = true
            AND (
                :query IS NULL OR :query = ''
                OR LOWER(t.scientificName) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(COALESCE(t.family, '')) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(t.qrCodeId) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(t.slug) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(COALESCE(tr.commonName, '')) LIKE LOWER(CONCAT('%', :query, '%'))
            )
            AND (
                :family IS NULL OR :family = ''
                OR LOWER(COALESCE(t.family, '')) LIKE LOWER(CONCAT('%', :family, '%'))
            )
            AND (
                :nativeStatus IS NULL OR :nativeStatus = ''
                OR UPPER(COALESCE(t.nativeStatus, 'UNKNOWN')) = UPPER(:nativeStatus)
            )
            AND (
                :category IS NULL OR :category = ''
                OR UPPER(cat) = UPPER(:category)
            )
            """)
    Page<Tree> findCatalog(
            @Param("query") String query,
            @Param("family") String family,
            @Param("nativeStatus") String nativeStatus,
            @Param("category") String category,
            Pageable pageable
    );

    @Query("""
            SELECT DISTINCT t.family FROM Tree t
            WHERE t.published = true AND t.family IS NOT NULL AND t.family <> ''
            ORDER BY t.family ASC
            """)
    List<String> findDistinctFamilies();

    @Query("""
            SELECT DISTINCT c FROM Tree t JOIN t.categories c
            WHERE t.published = true
            ORDER BY c ASC
            """)
    List<String> findDistinctCategories();

    @Query("""
            SELECT DISTINCT UPPER(COALESCE(t.nativeStatus, 'UNKNOWN')) FROM Tree t
            WHERE t.published = true
            """)
    List<String> findDistinctNativeStatuses();
}