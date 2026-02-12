package com.pet.businessdomain.userservice.dto;

import com.pet.businessdomain.userservice.entities.enumentities.EnumTrainer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CharacterTrainingDto {

    private Long id;

    private Long characterId;
    private Long trainingId;

    // Info opcional para el frontend
    private String trainingName;
    private EnumTrainer.TrainingType trainingType;
    private EnumTrainer.DifficultyLevel trainingDifficulty;

    // ===== PROGRESO =====
    private EnumTrainer.TrainingStatus status;
    private Integer progress;

    private Integer investedHours;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    // ===== RESULTADO =====
    private Integer academicXpGained;
    private Boolean applied;
}
