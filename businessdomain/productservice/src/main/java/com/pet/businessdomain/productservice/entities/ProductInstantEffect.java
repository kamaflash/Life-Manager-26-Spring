package com.pet.businessdomain.productservice.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pet.businessdomain.shareddto.enumentities.products.EffectType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "product_instant_effects")
@Data
public class ProductInstantEffect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Product product;

    @Enumerated(EnumType.STRING)
    private EffectType type;

    private Integer amount;
    private Boolean isPercentage = false;
    private String target;
    private Boolean isRandom = false;
    private Integer minAmount;
    private Integer maxAmount;
    private String description;
}