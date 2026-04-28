package com.pet.businessdomain.productservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_shop_offers")
@Data
public class ProductShopOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private BigDecimal offerPrice;           // Precio especial
    private BigDecimal originalPrice;        // Precio original (para mostrar descuento)

    private LocalDateTime startsAt;
    private LocalDateTime expiresAt;

    private Integer stockLimit = -1;         // -1 = ilimitado
    private Integer remainingStock = -1;

    private Integer dailyPurchaseLimit = 0;   // 0 = sin límite
    private String offerType;                 // "DISCOUNT", "BUNDLE", "FLASH_SALE"

    private Boolean active = true;
}