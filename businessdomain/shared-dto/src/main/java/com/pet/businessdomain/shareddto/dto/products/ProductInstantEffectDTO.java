package com.pet.businessdomain.shareddto.dto.products;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductInstantEffectDTO {
    private Long id;
    private EffectTypeDTO type;
    private Integer amount;
    private Boolean isPercentage;
    private String target;

    // Efectos aleatorios
    private Boolean isRandom;
    private Integer minAmount;
    private Integer maxAmount;

    // Descripción
    private String description;
    private String formattedDescription;  // Ej: "+20 Salud" o "+10% XP"
}
