package com.pet.businessdomain.productservice.repository;

import com.pet.businessdomain.productservice.entities.ProductShopOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductShopOfferRepository extends JpaRepository<ProductShopOffer, Long> {

    // ========== OFERTAS ACTIVAS ==========
    @Query("SELECT o FROM ProductShopOffer o WHERE o.active = true " +
            "AND o.startsAt <= :now AND o.expiresAt >= :now " +
            "AND (o.stockLimit = -1 OR o.remainingStock > 0)")
    List<ProductShopOffer> findActiveOffers(@Param("now") LocalDateTime now);

    Optional<ProductShopOffer> findByProductIdAndActiveTrueAndStartsAtBeforeAndExpiresAtAfter(
            Long productId, LocalDateTime now, LocalDateTime now2);

    // ========== OFERTAS POR PRODUCTO ==========
    List<ProductShopOffer> findByProductId(Long productId);

    List<ProductShopOffer> findByProductIdAndActiveTrue(Long productId);

    // ========== OFERTAS PRÓXIMAS ==========
    @Query("SELECT o FROM ProductShopOffer o WHERE o.active = true AND o.startsAt > :now")
    List<ProductShopOffer> findUpcomingOffers(@Param("now") LocalDateTime now);

    // ========== OFERTAS EXPIRADAS ==========
    @Query("SELECT o FROM ProductShopOffer o WHERE o.active = true AND o.expiresAt < :now")
    List<ProductShopOffer> findExpiredOffers(@Param("now") LocalDateTime now);

    // ========== ACTUALIZACIONES ==========
    @Modifying
    @Transactional
    @Query("UPDATE ProductShopOffer o SET o.remainingStock = o.remainingStock - :quantity " +
            "WHERE o.id = :id AND (o.stockLimit = -1 OR o.remainingStock >= :quantity)")
    int decrementStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    // ========== LIMPIEZA ==========
    @Modifying
    @Transactional
    void deleteByExpiresAtBefore(LocalDateTime date);
}