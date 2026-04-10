package com.pet.businessdomain.jobservice.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "job_requirements")
@Data
public class RequirementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // SKILL, STAT, EDUCATION, EXPERIENCE_YEARS, LEVEL

    private String skill_key; // "programming", "intelligence", "bachelor_degree"

    private Integer minValue; // Nivel mínimo requerido

    private Boolean mandatory; // Si es obligatorio o solo recomendado

    // ✅ AÑADIR: Relación con JobVacancyEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacancy_id", nullable = false)
    private JobVacancyEntity vacancy;
}