package com.pet.businessdomain.shareddto.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class CharacterMissionRecordResponseDto {

    private Long id;
    private Long characterId;
    private Long missionId;

    private String status;

    private LocalDateTime acceptedAt;
    private LocalDateTime completedAt;
    private LocalDateTime failedAt;
    private LocalDateTime expiresAt;

    private Integer timesCompleted;

    private Map<Long, Integer> objectiveProgress;

    private boolean rewardsClaimed;

    private LocalDateTime updatedAt;
}
