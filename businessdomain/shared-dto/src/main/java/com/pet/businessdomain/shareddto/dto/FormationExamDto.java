package com.pet.businessdomain.shareddto.dto;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.Data;

@Data
public class FormationExamDto {

    private Long id;

    private Long formationId;

    private String name;

    private Integer requiredHours;

    private EnumAll.DifficultyLevel difficulty;

    private Integer maxScore;

    private Boolean mandatory;

    private Integer minPassingScore;

    private Integer maxAttempts;

    private Boolean active;
}
