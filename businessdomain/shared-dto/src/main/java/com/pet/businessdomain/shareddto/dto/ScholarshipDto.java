package com.pet.businessdomain.shareddto.dto;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ScholarshipDto {

    private Long id;
    private String title;
    private String description;

    private BigDecimal amount;

    private Boolean active;

    private Integer minXpRequired;

    private LocalDate startDate;
    private LocalDate endDate;

    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDate resolutionDate;

    // Nivel formativo
    private EnumAll.EducationLevel educationLevel;

    // Requisitos académicos
    private Double minGrade;
    private Boolean requiresMerit;

    // Requisitos económicos
    private BigDecimal maxFamilyIncome;
    private Boolean requiresEconomicProof;
}
