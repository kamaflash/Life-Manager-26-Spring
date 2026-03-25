package com.pet.businessdomain.shareddto.dto;

import lombok.Data;

@Data
public class ProductEffectDTO {
    private String category; // Ej: ENERGY, HEALTH, etc.
    private Integer value;   // Ej: +10, +50
}
