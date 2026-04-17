package com.pet.businessdomain.shareddto.dto;

import lombok.Data;

@Data
public class RewardDto {

    private Long id;

    private String type;

    private String targetKey;
    private Integer value;

    private String itemCode;
    private String titleGranted;
    private String badgeCode;

    private boolean isPrimary;
}
