package com.pet.businessdomain.shareddto.dto.products;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IngredientConsumedDTO {
    private Long productId;
    private String productName;
    private Integer quantityConsumed;
    private Integer quantityHad;
}
