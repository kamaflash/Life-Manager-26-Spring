package com.pet.businessdomain.formationservice.entities;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor

@Entity
@Data
@Table(name = "training")
public class CharacterTraining {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referencias externas
    private Long characterId;
    private Long trainingId;

    // ===== PROGRESO =====
    @Enumerated(EnumType.STRING)
    private EnumAll.TrainingStatus status;
    // AVAILABLE, IN_PROGRESS, COMPLETED, FAILED

    private Integer progress; // 0-100
    private String trainingName;
    private EnumAll.TrainingType trainingType;
    private EnumAll.DifficultyLevel trainingDifficulty;

    private Integer investedHours;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    // ===== RESULTADO =====
    private Integer academicXpGained;
    private Boolean applied;
    private BigDecimal cost;
    private CharacterStats stats;

}
