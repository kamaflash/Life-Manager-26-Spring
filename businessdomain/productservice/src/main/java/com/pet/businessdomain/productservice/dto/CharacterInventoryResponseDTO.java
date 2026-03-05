package com.pet.businessdomain.productservice.dto;

import com.pet.businessdomain.productservice.entities.enumentities.ProductCategory;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CharacterInventoryResponseDTO {

    private Long productId;
    private String productName;
    private ProductCategory category;
    private Integer quantity;
}
