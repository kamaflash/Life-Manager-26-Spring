package com.pet.businessdomain.shareddto.dto.products;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IngredientDTO {
    private Long id;
    private Long productId;
    private ProductDTO product;
    private Integer quantity;
    private Boolean isConsumed;
    private String alternativeNote;
}
