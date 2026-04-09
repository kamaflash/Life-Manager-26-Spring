package com.pet.businessdomain.productservice.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_passive_effects")
@Data
public class ProductPassiveEffect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stat;  // ENERGY, HEALTH, INTELLIGENCE, etc.
    private Integer value;  // Valor del efecto (+5, -2)
    private boolean percentage;  // Si es porcentaje o valor fijo

    // Condiciones para activar el efecto pasivo
    private String condition;  // Ej: "TIME_OF_DAY", "HAS_BUFF", etc.
    private String conditionValue;  // Ej: "MORNING", "STUDYING"
}
