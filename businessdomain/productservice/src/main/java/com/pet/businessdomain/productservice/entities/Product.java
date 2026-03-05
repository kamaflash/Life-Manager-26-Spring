package com.pet.businessdomain.productservice.entities;

import com.pet.businessdomain.productservice.entities.enumentities.ProductCategory;
import jakarta.persistence.*;
import lombok.Data;

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

    private Integer price;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private Boolean active = true;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<ProductEffect> effects;
}
