package com.pet.businessdomain.shareddto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillStateDto {
    private String keyValue;
    private String name;
    private Integer level;
    private Integer xp;
    private Boolean locked;
    private Integer progress; // Porcentaje de progreso al siguiente nivel
    private Integer xpToNextLevel;
}
