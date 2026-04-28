package com.pet.businessdomain.shareddto.dto.products;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SellProductResponseDTO {
    private boolean success;
    private String message;
    private Long productId;
    private String productName;
    private Integer quantitySold;
    private BigDecimal totalPrice;
    private Integer remainingQuantity;
    private BigDecimal newBalance;
}
