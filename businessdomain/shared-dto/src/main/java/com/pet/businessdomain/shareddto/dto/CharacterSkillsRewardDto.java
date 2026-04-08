package com.pet.businessdomain.shareddto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterSkillsRewardDto {
    private Long characterId;
    private List<String> skillsToUnlock;
    private int xpReward;  // XP académico total a añadir
    private Map<String, Integer> statRewards;  // Recompensas de estadísticas
}
