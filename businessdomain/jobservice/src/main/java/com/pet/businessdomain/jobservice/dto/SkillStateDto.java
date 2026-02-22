package com.pet.businessdomain.jobservice.dto;

import lombok.Data;

@Data
public class SkillStateDto {

    private String keyValue;

    private int xp;
    private int level;

    private String lastPracticed; // ISO date
    private Double decayRate;

    private Boolean locked;
}
