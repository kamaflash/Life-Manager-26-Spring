package com.pet.businessdomain.shareddto.dto;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class JobEventDTO {
    private Long id;
    private Long characterJobId;
    private String eventType; // BONUS, PROMOTION, DEMOTION, CONFLICT, PROJECT_SUCCESS, PROJECT_FAILURE, BURNOUT, OFFER_FROM_RIVAL, MENTOR_LEAVES, TEAM_RESTRUCTURE, QUARTERLY_REVIEW
    private String title;
    private String description;
    private Integer salaryChange;
    private Integer performanceChange;
    private Integer satisfactionChange;
    private Integer stressChange;
    private LocalDateTime occurredAt;
    private Boolean resolved;
}
