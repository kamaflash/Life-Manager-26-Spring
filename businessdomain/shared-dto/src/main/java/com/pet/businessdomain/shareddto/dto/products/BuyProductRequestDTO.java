package com.pet.businessdomain.shareddto.dto.products;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BuyProductRequestDTO {
    private Long characterId;
    private Long productId;
    private Integer quantity;
    private Long offerId;           // Opcional: si compra desde oferta especial
    private Long accountId;         // Cuenta a debitar
}
