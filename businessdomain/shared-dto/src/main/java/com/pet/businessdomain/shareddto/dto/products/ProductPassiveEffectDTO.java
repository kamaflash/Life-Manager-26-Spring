package com.pet.businessdomain.shareddto.dto.products;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductPassiveEffectDTO {
    private Long id;
    private String stat;
    private Integer value;
    private Boolean isPercentage;
    private TriggerConditionDTO trigger;
    private String condition;
    private String conditionValue;
    private Integer chancePercentage;
    private String description;
    private String formattedDescription;
}
