package com.kigali.ecopark.repository;

import com.kigali.ecopark.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LanguageRepository extends JpaRepository<Language, String> {

    List<Language> findByActiveTrueOrderByNameAsc();
}
