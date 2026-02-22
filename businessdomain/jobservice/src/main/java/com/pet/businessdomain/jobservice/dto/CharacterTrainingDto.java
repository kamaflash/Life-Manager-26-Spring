package com.pet.businessdomain.jobservice.dto;

import com.pet.businessdomain.jobservice.entities.enumjobs.EnumFormation;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CharacterTrainingDto {

    private Long id;

    private Long characterId;
    private Long trainingId;

    // Info opcional para el frontend
    private String trainingName;
    private EnumFormation.TrainingType trainingType;
    private EnumFormation.DifficultyLevel trainingDifficulty;

    // ===== PROGRESO =====
    private EnumFormation.TrainingStatus status;
    private Integer progress;

    private Integer investedHours;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    // ===== RESULTADO =====
    private Integer academicXpGained;
    private Boolean applied;
}
