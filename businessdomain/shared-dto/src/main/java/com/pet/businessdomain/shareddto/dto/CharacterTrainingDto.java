package com.pet.businessdomain.shareddto.dto;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.shareddto.enumentities.EnumTrainer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CharacterTrainingDto {

    private Long id;

    private Long characterId;
    private Long trainingId;

    // Info opcional para el frontend
    private String trainingName;
    private EnumAll.TrainingType trainingType;
    private EnumAll.DifficultyLevel trainingDifficulty;

    // ===== PROGRESO =====
    private EnumAll.TrainingStatus status;
    private Integer progress;

    private Integer investedHours;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    // ===== RESULTADO =====
    private Integer academicXpGained;
    private Boolean applied;
}
