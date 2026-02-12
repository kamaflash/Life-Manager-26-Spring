package com.pet.businessdomain.formationservice.dto;

import com.pet.businessdomain.formationservice.entities.CharacterStats;
import com.pet.businessdomain.formationservice.entities.enumentities.Enum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CharacterTrainingDto {

    private Long id;

    private Long characterId;
    private Long trainingId;

    // Info opcional para el frontend
    private String trainingName;
    private Enum.TrainingType trainingType;
    private Enum.DifficultyLevel trainingDifficulty;

    // ===== PROGRESO =====
    private Enum.TrainingStatus status;
    private Integer progress;

    private Integer investedHours;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    // ===== RESULTADO =====
    private Integer academicXpGained;
    private Boolean applied;
    private BigDecimal cost;
    private CharacterStats stats;

}
