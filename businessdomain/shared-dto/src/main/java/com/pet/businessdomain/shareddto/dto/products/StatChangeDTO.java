package com.pet.businessdomain.shareddto.dto.products;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatChangeDTO {
    private String statName;
    private int oldValue;
    private int newValue;
    private int change;
    private String description;
}
