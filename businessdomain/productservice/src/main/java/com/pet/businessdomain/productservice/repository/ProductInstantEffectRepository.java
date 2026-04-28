package com.pet.businessdomain.productservice.repository;

import com.pet.businessdomain.productservice.entities.ProductInstantEffect;
import com.pet.businessdomain.shareddto.enumentities.products.EffectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductInstantEffectRepository extends JpaRepository<ProductInstantEffect, Long> {

    // ✅ Método para encontrar por ID de producto
    @Query("SELECT e FROM ProductInstantEffect e WHERE e.product.id = :productId")
    List<ProductInstantEffect> findByProductId(@Param("productId") Long productId);

    List<ProductInstantEffect> findByType(EffectType type);

    // ✅ Método para eliminar por ID de producto
    @Modifying
    @Transactional
    @Query("DELETE FROM ProductInstantEffect e WHERE e.product.id = :productId")
    void deleteByProductId(@Param("productId") Long productId);
}