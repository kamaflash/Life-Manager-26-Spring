package com.pet.businessdomain.productservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_recipes")
@Data
public class ProductRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long resultProductId;           // Producto que se obtiene
    private Integer resultQuantity = 1;      // Cantidad que se obtiene

    private Integer craftingTimeHours = 0;    // Horas que tarda en fabricarse
    private Integer requiredSkillLevel = 0;   // Nivel de habilidad necesario

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "recipe_id")
    private List<Ingredient> ingredients = new ArrayList<>();

    private Boolean isDiscoverable = true;    // Si se puede descubrir o solo comprar
    private Integer xpReward = 0;             // XP al fabricar
}