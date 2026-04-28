package com.pet.businessdomain.shareddto.dto.products;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductShopOfferDTO {
    private Long id;
    private Long productId;
    private ProductDTO product;
    private BigDecimal offerPrice;
    private BigDecimal originalPrice;
    private BigDecimal discountPercentage;  // Calculado
    private LocalDateTime startsAt;
    private LocalDateTime expiresAt;
    private Integer stockLimit;
    private Integer remainingStock;
    private Integer dailyPurchaseLimit;
    private String offerType;
    private Boolean active;
    private Boolean isAvailable;  // Si está dentro de fechas y hay stock
}
