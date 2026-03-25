package com.pet.businessdomain.productservice.entities;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.shareddto.enumentities.ProductCategory;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 1000)
    private String description;

    private String img;

    private String icon;

    private BigDecimal price;

    // ⚡ Coste en puntos de acción
    private Integer pa;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Enumerated(EnumType.STRING)
    private EnumAll.ProductUsageType usageType;

    private Boolean active = true;

    // 🔥 NUEVO → rareza (muy importante en juegos)
    private String rarity; // COMMON, RARE, EPIC, LEGENDARY

    // 🔥 NUEVO → stackeable (para inventario)
    private Boolean stackable = true;

    // 🔥 NUEVO → si se puede vender
    private Boolean sellable = true;

    // 🔥 NUEVO → cooldown en horas
    private Integer cooldownHours;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<ProductEffect> effects;
}