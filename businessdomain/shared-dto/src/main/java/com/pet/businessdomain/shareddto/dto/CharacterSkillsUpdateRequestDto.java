package com.pet.businessdomain.shareddto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterSkillsUpdateRequestDto {
    private Long characterId;
    private List<String> skillsToUnlock;
    private int totalXpReward;
    private Map<String, Integer> statRewards;
    private int academicXpReward;
}
