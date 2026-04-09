package com.pet.businessdomain.shareddto.dto;

import lombok.Data;

@Data
public class ProductPassiveEffectDTO {
    private String stat;
    private Integer value;
    private boolean percentage;
    private String condition;
    private String conditionValue;
}
