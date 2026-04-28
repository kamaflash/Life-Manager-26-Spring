package com.pet.businessdomain.shareddto.dto.products;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CharacterInventoryDTO {
    private Long id;
    private Long characterId;
    private ProductDTO product;
    private Integer quantity;

    // Estado
    private Boolean equipped;
    private Integer durabilityLeft;
    private LocalDateTime equippedSince;
    private LocalDateTime acquiredAt;
    private LocalDateTime expiresAt;
    private LocalDateTime activeUntil;
    private LocalDateTime lastUsedAt;

    // Información de cooldown
    private Boolean isOnCooldown;
    private Long cooldownRemainingSeconds;

    // Información de expiración
    private Boolean isExpired;
    private Boolean isActive;

    private String notes;
}
