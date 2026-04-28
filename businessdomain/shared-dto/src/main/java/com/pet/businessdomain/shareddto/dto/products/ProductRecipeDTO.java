package com.pet.businessdomain.shareddto.dto.products;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductRecipeDTO {
    private Long id;
    private Long resultProductId;
    private ProductDTO resultProduct;
    private Integer resultQuantity;
    private Integer craftingTimeHours;
    private Integer requiredSkillLevel;
    private List<IngredientDTO> ingredients;
    private Boolean isDiscoverable;
    private Integer xpReward;
}
