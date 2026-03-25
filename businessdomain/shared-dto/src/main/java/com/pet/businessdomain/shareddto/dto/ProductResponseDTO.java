package com.pet.businessdomain.shareddto.dto;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.shareddto.enumentities.ProductCategory;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductResponseDTO {

    private Long id;

    // 🧾 Info básica
    private String name;
    private String description;
    private String img;
    private String icon;

    // 💰 Economía
    private BigDecimal price;
    private Boolean sellable;

    // ⚡ Gameplay
    private Integer pa;
    private EnumAll.ProductUsageType usageType;

    // 🎯 Estado
    private Boolean active;

    // 🎨 Game feel
    private String rarity; // COMMON, RARE, EPIC, LEGENDARY
    private Boolean stackable;

    // ⏳ Sistema tiempo
    private Integer cooldownHours;

    // 🧩 Clasificación (útil para filtros frontend)
    private ProductCategory category;

    // ✨ Efectos
    private List<ProductEffectDTO> effects;
}
