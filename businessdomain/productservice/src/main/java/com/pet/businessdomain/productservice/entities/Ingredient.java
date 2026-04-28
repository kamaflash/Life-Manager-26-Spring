package com.pet.businessdomain.productservice.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "recipe_ingredients")
@Data
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ RELACIÓN CON RECETA
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private ProductRecipe recipe;  // ← este campo se llama "recipe", no "recipeId"

    private Long productId;
    private Integer quantity = 1;
    private Boolean isConsumed = true;
    private String alternativeNote;
}