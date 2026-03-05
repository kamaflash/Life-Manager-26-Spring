package com.pet.businessdomain.productservice.entities;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
@Table(name = "product_effects")

public class ProductEffect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private Integer value;
}
