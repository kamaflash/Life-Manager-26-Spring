package com.pet.businessdomain.productservice.repository;

import com.pet.businessdomain.productservice.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    // ✅ Usando @Query (recomendado para evitar ambigüedades)
    @Query("SELECT i FROM Ingredient i WHERE i.recipe.id = :recipeId")
    List<Ingredient> findByRecipeId(@Param("recipeId") Long recipeId);

    // ✅ Buscar por ID de producto
    List<Ingredient> findByProductId(Long productId);

    // ✅ Eliminar por ID de receta
    @Modifying
    @Transactional
    @Query("DELETE FROM Ingredient i WHERE i.recipe.id = :recipeId")
    void deleteByRecipeId(@Param("recipeId") Long recipeId);
}