package com.pet.businessdomain.shareddto.dto.products;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UseProductRequestDTO {
    private Long characterId;
    private Long inventoryId;      // ID del item en inventario
    private Long productId;        // Alternativa si se usa por producto
    private Integer quantity;      // Cantidad a usar (para consumibles)
    private String target;         // Objetivo específico (ej: habilidad a mejorar)
}
