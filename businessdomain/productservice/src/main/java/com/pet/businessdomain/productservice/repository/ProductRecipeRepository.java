package com.pet.businessdomain.productservice.repository;

import com.pet.businessdomain.productservice.entities.ProductRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRecipeRepository extends JpaRepository<ProductRecipe, Long> {

    Optional<ProductRecipe> findByResultProductId(Long productId);

    List<ProductRecipe> findByRequiredSkillLevelLessThanEqual(Integer level);

    @Query("SELECT r FROM ProductRecipe r WHERE r.isDiscoverable = true")
    List<ProductRecipe> findDiscoverableRecipes();

    @Query("SELECT r FROM ProductRecipe r WHERE SIZE(r.ingredients) <= :maxIngredients")
    List<ProductRecipe> findByMaxIngredients(@Param("maxIngredients") int maxIngredients);
}