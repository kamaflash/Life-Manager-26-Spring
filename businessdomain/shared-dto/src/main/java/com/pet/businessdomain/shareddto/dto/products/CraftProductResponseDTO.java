package com.pet.businessdomain.shareddto.dto.products;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CraftProductResponseDTO {
    private boolean success;
    private String message;
    private ProductDTO craftedProduct;
    private Integer quantity;
    private int xpGained;
    private List<IngredientConsumedDTO> ingredientsConsumed;
    private LocalDateTime completionTime;  // Si crafting requiere tiempo
}
