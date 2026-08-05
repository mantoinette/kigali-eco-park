package com.kigali.ecopark.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tree_translations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"tree_id", "language_code"})
})
public class TreeTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tree_id", nullable = false)
    private Tree tree;

    @Column(name = "language_code", nullable = false, length = 10)
    private String languageCode;

    @Column(name = "common_name", nullable = false, length = 200)
    private String commonName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String uses;

    @Column(name = "ecological_importance", columnDefinition = "TEXT")
    private String ecologicalImportance;

    @Column(name = "benefits_to_people_and_wildlife", columnDefinition = "TEXT")
    private String benefitsToPeopleAndWildlife;

    @Column(name = "common_areas", columnDefinition = "TEXT")
    private String commonAreas;

    @Column(name = "additional_info", columnDefinition = "TEXT")
    private String additionalInfo;

    @Column(name = "quick_facts", columnDefinition = "TEXT")
    private String quickFacts;

    @Column(name = "short_description", columnDefinition = "TEXT")
    private String shortDescription;

    @Column(name = "interesting_facts", columnDefinition = "TEXT")
    private String interestingFacts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUses() {
        return uses;
    }

    public void setUses(String uses) {
        this.uses = uses;
    }

    public String getEcologicalImportance() {
        return ecologicalImportance;
    }

    public void setEcologicalImportance(String ecologicalImportance) {
        this.ecologicalImportance = ecologicalImportance;
    }

    public String getBenefitsToPeopleAndWildlife() {
        return benefitsToPeopleAndWildlife;
    }

    public void setBenefitsToPeopleAndWildlife(String benefitsToPeopleAndWildlife) {
        this.benefitsToPeopleAndWildlife = benefitsToPeopleAndWildlife;
    }

    public String getCommonAreas() {
        return commonAreas;
    }

    public void setCommonAreas(String commonAreas) {
        this.commonAreas = commonAreas;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getQuickFacts() {
        return quickFacts;
    }

    public void setQuickFacts(String quickFacts) {
        this.quickFacts = quickFacts;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getInterestingFacts() {
        return interestingFacts;
    }

    public void setInterestingFacts(String interestingFacts) {
        this.interestingFacts = interestingFacts;
    }
}
