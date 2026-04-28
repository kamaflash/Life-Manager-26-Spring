package com.pet.businessdomain.productservice.repository;

import com.pet.businessdomain.productservice.entities.ProductPassiveEffect;
import com.pet.businessdomain.shareddto.enumentities.products.TriggerCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductPassiveEffectRepository extends JpaRepository<ProductPassiveEffect, Long> {

    @Query("SELECT e FROM ProductPassiveEffect e WHERE e.product.id = :productId")
    List<ProductPassiveEffect> findByProductId(@Param("productId") Long productId);

    List<ProductPassiveEffect> findByTrigger(TriggerCondition trigger);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductPassiveEffect e WHERE e.product.id = :productId")
    void deleteByProductId(@Param("productId") Long productId);
}