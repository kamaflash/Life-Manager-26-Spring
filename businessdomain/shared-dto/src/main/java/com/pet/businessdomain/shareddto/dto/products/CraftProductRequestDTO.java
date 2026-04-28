package com.pet.businessdomain.shareddto.dto.products;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CraftProductRequestDTO {
    private Long characterId;
    private Long recipeId;
    private Integer quantity;
}
