package com.pet.businessdomain.productservice.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pet.businessdomain.shareddto.enumentities.products.TriggerCondition;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "product_passive_effects")
@Data
public class ProductPassiveEffect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonBackReference  // ← Cambiar de @JsonIgnore a @JsonBackReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Product product;

    private String stat;
    private Integer value;
    private Boolean isPercentage = false;

    @Enumerated(EnumType.STRING)
    private TriggerCondition trigger;

    private String condition;
    private String conditionValue;
    private Integer chancePercentage = 100;
    private String description;
}