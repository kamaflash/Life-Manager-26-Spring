package com.pet.businessdomain.shareddto.dto;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MissionDTO {
    private Long id;
    private Long characterJobId;
    private String title;
    private String description;
    private String difficulty; // EASY, MEDIUM, HARD, EPIC
    private Integer deadlineHours;
    private Integer remainingHours;
    private Integer xpReward;
    private Integer salaryBonus;
    private Integer reputationGain;
    private String skillReward;
    private LocalDateTime assignedAt;
    private LocalDateTime completedAt;
    private Boolean completed;
    private Integer progress; // 0-100
}
