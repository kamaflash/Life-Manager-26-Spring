package com.pet.businessdomain.formationservice.entities;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "scholarships")
@Data
public class ScholarshipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    private BigDecimal amount;

    private Boolean active = true;

    private Integer minXpRequired;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    // Nivel formativo al que aplica
    @Enumerated(EnumType.STRING)
    private EnumAll.EducationLevel educationLevel; // PRIMARY, SECONDARY, UNIVERSITY, MASTER, etc.

    // Requisitos académicos
    private Double minGrade; // nota mínima (ej: 7.5)
    private Boolean requiresMerit; // si requiere excelencia

    // Requisitos económicos
    private BigDecimal maxFamilyIncome; // renta máxima permitida
    private Boolean requiresEconomicProof;

    private LocalDate resolutionDate;
}
