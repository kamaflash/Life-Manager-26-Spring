package com.pet.businessdomain.shareddto.dto;

import lombok.Data;

@Data
public class SkillStateDto {
    private String keyValue;
    private Integer level;
    private Integer xp;
    private Boolean locked;
}
