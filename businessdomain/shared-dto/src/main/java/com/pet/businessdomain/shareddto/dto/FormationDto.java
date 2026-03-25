package com.pet.businessdomain.shareddto.dto;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
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
    private EnumAll.CareerInterest category;   // TECHNOLOGY, BUSINESS, ARTS, HEALTH...
    private EnumAll.TrainingType type;         // COURSE, DEGREE, MASTER, WORKSHOP
    private EnumAll.DifficultyLevel difficulty; // BASIC, INTERMEDIATE, ADVANCED

    // ===== REQUISITOS =====
    private EnumAll.EducationLevel minEducationLevel;
    private Integer minAcademicLevel;
    private Integer minAcademicXp;
    private Integer maxAcademicXp;
    private List<EnumAll.CareerInterest> allowedCareers;

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
    private CharacterStatsDto stats;

    private List<EnumAll.WorkingDay> workingDays;
    private String startTime;
    private String endTime;
}
