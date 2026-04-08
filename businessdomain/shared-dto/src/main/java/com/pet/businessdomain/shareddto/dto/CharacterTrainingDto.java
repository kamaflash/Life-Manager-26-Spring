package com.pet.businessdomain.shareddto.dto;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.shareddto.enumentities.EnumTrainer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CharacterTrainingDto {

    private Long id;

    private Long characterId;
    private Long trainingId;

    private EnumAll.TrainingStatus status;

    private Integer progress = 0;
    private String trainingName;
    private EnumAll.TrainingType trainingType;
    private EnumAll.DifficultyLevel trainingDifficulty;

    private Integer investedHours = 0;

    private Integer studyHours = 0;

    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    private Integer academicXpGained;
    private Double grade;

    private Boolean applied;
    private BigDecimal cost;
}
