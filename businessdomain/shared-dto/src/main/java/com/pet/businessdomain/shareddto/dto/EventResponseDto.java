package com.pet.businessdomain.shareddto.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventResponseDto {

    private Long id;
    private String code;
    private String title;
    private String description;
    private String lore;

    private String type;
    private String scope;

    private String city;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private boolean recurring;
    private String cronExpression;

    private Integer maxParticipants;
    private Integer durationMinutes;

    private String status;
    private boolean autoTrigger;
    private Long ownerCharacterId;

    private List<RewardDto> rewards;
    private List<MissionRequirementDto> requirements;

    private String iconUrl;
    private String bannerUrl;
}
