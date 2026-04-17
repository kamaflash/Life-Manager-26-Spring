package com.pet.businessdomain.eventservice.entities;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "character_mission_records")
@Data
public class CharacterMissionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long characterId;          // FK al personaje (cross-service)
    private Long missionId;

    @Enumerated(EnumType.STRING)
    private EnumAll.MissionStatus status;
    // LOCKED, AVAILABLE, IN_PROGRESS, COMPLETED, FAILED, ABANDONED

    private LocalDateTime acceptedAt;
    private LocalDateTime completedAt;
    private LocalDateTime failedAt;
    private LocalDateTime expiresAt;   // Para misiones con límite de tiempo

    private Integer timesCompleted;    // Para misiones repetibles

    @ElementCollection
    @CollectionTable(
            name = "cmr_objective_progress",
            joinColumns = @JoinColumn(name = "cmr_id")
    )
    @MapKeyColumn(name = "objective_id")
    @Column(name = "current_value")
    private java.util.Map<Long, Integer> objectiveProgress; // objectiveId -> progreso actual

    private boolean rewardsClaimed;

    private LocalDateTime updatedAt;
}

