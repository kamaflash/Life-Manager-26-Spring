package com.pet.businessdomain.formationservice.dto;

import com.pet.businessdomain.formationservice.entities.CharacterStats;
import com.pet.businessdomain.formationservice.entities.enumentities.Enum;
import lombok.Data;

import java.util.List;

@Data
public class FormationDto {

    private Long id;

    // ===== IDENTIDAD =====
    private String code;          // ej: JAVA_BASIC_01
    private String name;
    private String description;

    // ===== CLASIFICACIÓN =====
    private Enum.CareerInterest category;   // TECHNOLOGY, BUSINESS, ARTS, HEALTH...
    private Enum.TrainingType type;         // COURSE, DEGREE, MASTER, WORKSHOP
    private Enum.DifficultyLevel difficulty; // BASIC, INTERMEDIATE, ADVANCED

    // ===== REQUISITOS =====
    private Enum.EducationLevel minEducationLevel;
    private Integer minAcademicLevel;
    private Integer minAcademicXp;
    private Integer maxAcademicXp;
    private List<Enum.CareerInterest> allowedCareers;

    // ===== COSTE =====
    private Integer durationHours;
    private Double cost;
    private Integer effort; // energía / estrés requerido

    // ===== RESULTADO =====
    private Integer academicXpReward;
    private List<String> skillsUnlocked;

    private Boolean repeatable;
    private Boolean active;

    // ===== PROGRESO DEL USUARIO =====
    private Integer xp;            // progreso actual del usuario
    private Integer level;         // nivel alcanzado
    private String lastPracticed;  // fecha última práctica
    private String decayRate;      // tasa de decaimiento (si aplica)
    private Boolean locked;        // si está bloqueado o no
    private CharacterStats stats;

}
