package com.pet.businessdomain.shareddto.dto.products;

import com.pet.businessdomain.shareddto.enumentities.ProductCategory;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ShopFilterDTO {
    private String search;
    private ProductCategory category;
    private ProductRarityDTO rarity;
    private BigDecimal maxPrice;
    private Boolean onlyOnSale;  // Solo ofertas activas
    private Boolean onlyNew;     // Productos nuevos (últimos 7 días)
    private Boolean limitedStock; // Productos con stock limitado
}
