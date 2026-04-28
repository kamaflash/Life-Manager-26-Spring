package com.pet.businessdomain.shareddto.dto.products;

import com.pet.businessdomain.shareddto.enumentities.ProductCategory;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ProductFilterDTO {
    private String search;
    private ProductCategory category;
    private ProductRarityDTO rarity;
    private ProductSlotDTO slot;
    private Integer minLevel;
    private Integer maxLevel;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Boolean isConsumable;
    private Boolean stackable;
    private Boolean isEventItem;
    private String season;
    private List<String> tags;
    private Boolean inStock;  // Solo productos con descuento activo
    private Boolean availableForLevel;  // Filtrar por nivel del personaje
}
