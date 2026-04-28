package com.pet.businessdomain.productservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "character_inventory")
@Data
public class CharacterInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long characterId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity = 1;

    // NUEVOS CAMPOS
    private Boolean equipped = false;           // Si está equipado (para slot items)
    private Integer durabilityLeft = 0;         // Usos restantes (0 = ilimitado)
    private LocalDateTime equippedSince;        // Desde cuándo está equipado
    private LocalDateTime acquiredAt;           // Cuándo lo consiguió
    private LocalDateTime expiresAt;            // Si caduca (null = no caduca)

    // Para efectos activos actualmente
    private LocalDateTime activeUntil;          // Hasta cuándo está activo el efecto
    private LocalDateTime lastUsedAt;           // Última vez usado (para cooldown)

    // Notas personales del jugador
    private String notes;

    @PrePersist
    protected void onCreate() {
        acquiredAt = LocalDateTime.now();
        if (product != null && product.getDurability() != null && product.getDurability() > 0) {
            durabilityLeft = product.getDurability();
        }
    }
}