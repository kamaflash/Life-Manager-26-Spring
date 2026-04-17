package com.pet.businessdomain.shareddto.dto;

import lombok.Data;

import java.util.List;

@Data
public class MissionResponseDto {

    private Long id;
    private String code;
    private String title;
    private String description;
    private String lore;

    private String type;
    private String category;
    private String difficulty;

    private Integer minAge;
    private Integer requiredXpJobs;
    private Integer requiredXpAcademy;

    private boolean repeatable;
    private Integer cooldownHours;

    private boolean hidden;
    private boolean autoAccept;

    private Integer orderInChain;
    private Long chainId;

    private String iconUrl;
    private String bannerUrl;

    private List<ObjectiveDto> objectives;
    private List<RewardDto> rewards;
    private List<MissionRequirementDto> requirements;

    private List<String> unlocksMissionCodes;
}
