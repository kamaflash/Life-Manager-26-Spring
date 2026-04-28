package com.pet.businessdomain.productservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.shareddto.enumentities.ProductCategory;
import com.pet.businessdomain.shareddto.enumentities.products.ProductRarity;
import com.pet.businessdomain.shareddto.enumentities.products.ProductSlot;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;

import java.math.BigDecimal;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
@Data
@EqualsAndHashCode(exclude = {"instantEffects", "passiveEffects"})
@ToString(exclude = {"instantEffects", "passiveEffects"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  // ← AÑADIR
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ========== INFORMACIÓN BÁSICA ==========
    @Column(length = 255, columnDefinition = "varchar(255)")
    @JdbcTypeCode(Types.VARCHAR)
    private String name;

    @Column(length = 1000, columnDefinition = "text")
    @JdbcTypeCode(Types.LONGVARCHAR)
    private String description;

    private String img;
    private String icon;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Enumerated(EnumType.STRING)
    private EnumAll.ProductUsageType usageType;

    // ========== PRECIO Y VALOR ==========
    private BigDecimal price;
    private BigDecimal discountPrice;
    private LocalDate discountStartDate;
    private LocalDate discountEndDate;
    private Integer pa = 1;

    // ========== RAREZA Y PROGRESIÓN ==========
    @Enumerated(EnumType.STRING)
    private ProductRarity rarity = ProductRarity.COMMON;

    private Integer requiredLevel = 1;
    private Integer requiredXp = 0;

    // ========== RANURA Y EQUIPAMIENTO ==========
    @Enumerated(EnumType.STRING)
    private ProductSlot slot = ProductSlot.NONE;

    // ========== USO Y DURACIÓN ==========
    private Boolean isConsumable = true;
    private Boolean stackable = true;
    private Integer durationHours = 0;
    private Integer durability = 0;
    private Integer cooldownHours = 0;

    // ========== RESTRICCIONES ==========
    private Boolean active = true;
    private Boolean sellable = true;
    private Boolean tradable = false;

    // ========== TEMPORADA Y EVENTOS ==========
    private String season;
    private Boolean isEventItem = false;
    private LocalDate eventStartDate;
    private LocalDate eventEndDate;

    // ========== TAGS Y FILTRADO ==========
    @ElementCollection
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    // ========== EFECTOS ==========
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "product")
    @JsonManagedReference
    private Set<ProductInstantEffect> instantEffects = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "product")
    @JsonManagedReference
    private Set<ProductPassiveEffect> passiveEffects = new HashSet<>();

    // ========== REQUISITOS ==========
    @ElementCollection
    @CollectionTable(name = "product_requirements", joinColumns = @JoinColumn(name = "product_id"))
    private List<String> requirements = new ArrayList<>();

    // ========== ESTADÍSTICAS ==========
    private Integer totalUsageCount = 0;
    private Integer rating = 0;
    private Integer ratingCount = 0;

    // ========== TIMESTAMPS ==========
    private LocalDate createdAt;
    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        updatedAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }
}