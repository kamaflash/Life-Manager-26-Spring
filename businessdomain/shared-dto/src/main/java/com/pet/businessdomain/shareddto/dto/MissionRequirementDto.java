package com.pet.businessdomain.shareddto.dto;

import lombok.Data;

@Data
public class MissionRequirementDto {

    private Long id;

    private String type;

    private String targetKey;
    private Integer targetValue;
    private String stringValue;

    private String failMessage;
}
