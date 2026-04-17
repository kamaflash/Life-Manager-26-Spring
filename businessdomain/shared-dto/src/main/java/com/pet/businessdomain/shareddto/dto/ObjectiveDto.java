package com.pet.businessdomain.shareddto.dto;

import lombok.Data;

@Data
public class ObjectiveDto {

    private Long id;
    private String description;

    private String type;

    private String targetKey;
    private Integer targetValue;
    private Integer currentDefault;

    private boolean optional;
    private Integer orderIndex;

    private String hint;
}
