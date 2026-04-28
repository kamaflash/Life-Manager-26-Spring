package com.pet.businessdomain.shareddto.dto.products;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.shareddto.enumentities.ProductCategory;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private String img;
    private String icon;
    private Integer pa ;

    // Categoría y uso
    private ProductCategory category;
    private EnumAll.ProductUsageType usageType;

    // Precio
    private BigDecimal price;
    private BigDecimal discountPrice;
    private LocalDate discountStartDate;
    private LocalDate discountEndDate;
    private BigDecimal finalPrice;  // Precio actual (con descuento si aplica)

    // Rareza y progresión
    private ProductRarityDTO rarity;
    private Integer requiredLevel;
    private Integer requiredXp;

    // Ranura
    private ProductSlotDTO slot;

    // Uso y duración
    private Boolean isConsumable;
    private Boolean stackable;
    private Integer durationHours;
    private Integer durability;
    private Integer cooldownHours;

    // Restricciones
    private Boolean active;
    private Boolean sellable;
    private Boolean tradable;

    // Temporada y eventos
    private String season;
    private Boolean isEventItem;
    private LocalDate eventStartDate;
    private LocalDate eventEndDate;

    // Tags
    private List<String> tags;

    // Efectos
    private List<ProductInstantEffectDTO> instantEffects;
    private List<ProductPassiveEffectDTO> passiveEffects;

    // Requisitos
    private List<String> requirements;

    // Estadísticas
    private Integer totalUsageCount;
    private Integer rating;
    private Double averageRating;  // rating / ratingCount

    private LocalDate createdAt;
    private LocalDate updatedAt;
}