package com.pet.businessdomain.shareddto.dto;

import com.pet.businessdomain.shareddto.enumentities.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CharacterInventoryResponseDTO {

    private Long productId;
    private String productName;
    private ProductCategory category;
    private Integer quantity;
    private ProductResponseDTO product;
}
