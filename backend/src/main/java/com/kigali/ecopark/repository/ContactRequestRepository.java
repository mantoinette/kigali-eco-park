package com.kigali.ecopark.repository;

import com.kigali.ecopark.entity.ContactRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRequestRepository extends JpaRepository<ContactRequest, Long> {

    List<ContactRequest> findAllByOrderByCreatedAtDesc();

    @Query("""
            SELECT c FROM ContactRequest c
            WHERE (:status IS NULL OR c.status = :status)
              AND (:requestType IS NULL OR c.requestType = :requestType)
            ORDER BY c.createdAt DESC
            """)
    List<ContactRequest> findFiltered(
            @Param("status") String status,
            @Param("requestType") String requestType
    );

    long countByStatus(String status);
}
