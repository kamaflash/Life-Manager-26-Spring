package com.pet.businessdomain.shareddto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterSkillsUpdateResponseDto {
    private Long characterId;
    private Map<String, SkillStateDto> updatedSkills;
    private Integer newAcademicXp;
    private StatsDto updatedStats;
    private boolean success;
    private String message;
}
