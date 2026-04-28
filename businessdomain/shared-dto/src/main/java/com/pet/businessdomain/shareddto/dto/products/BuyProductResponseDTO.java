package com.pet.businessdomain.shareddto.dto.products;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BuyProductResponseDTO {
    private boolean success;
    private String message;
    private Long inventoryId;
    private ProductDTO product;
    private Integer quantity;
    private BigDecimal totalPrice;
    private BigDecimal newBalance;   // Saldo restante de la cuenta
}
