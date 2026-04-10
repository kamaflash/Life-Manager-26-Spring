package com.pet.businessdomain.jobservice.entities;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_missions")
@Data
public class MissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long characterJobId;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private EnumAll.MissionDifficulty difficulty; // EASY, MEDIUM, HARD, EPIC

    private Integer deadlineHours; // horas para completar

    // Recompensas
    private Integer xpReward;
    private Integer salaryBonus;
    private Integer reputationGain;
    private String skillReward; // habilidad que mejora

    private LocalDateTime assignedAt;
    private LocalDateTime completedAt;
    private Boolean completed = false;
}

