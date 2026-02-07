package com.pet.businessdomain.formationservice.dto;

import com.pet.businessdomain.formationservice.entities.enumentities.Enum;
import lombok.Data;

import java.util.List;

@Data
public class FormationDto {

    private Long id;

    // ===== IDENTIDAD =====
    private String code;
    private String name;
    private String description;

    // ===== CLASIFICACIÓN =====
    private Enum.CareerInterest category;
    private Enum.TrainingType type;
    private Enum.DifficultyLevel difficulty;

    // ===== REQUISITOS =====
    private Enum.EducationLevel minEducationLevel;
    private Integer minAcademicLevel;
    private Integer minAcademicXp;
    private Integer maxAcademicXp;
    private List<Enum.CareerInterest> allowedCareers;

    // ===== COSTE =====
    private Integer durationHours;
    private Double cost;
    private Integer effort;

    // ===== RESULTADO =====
    private Integer academicXpReward;
    private List<String> skillsUnlocked;

    private Boolean repeatable;
    private Boolean active;
}
