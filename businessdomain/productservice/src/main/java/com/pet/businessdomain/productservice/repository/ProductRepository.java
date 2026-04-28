package com.pet.businessdomain.productservice.repository;

import com.pet.businessdomain.productservice.entities.Product;
import com.pet.businessdomain.shareddto.enumentities.ProductCategory;
import com.pet.businessdomain.shareddto.enumentities.products.ProductRarity;
import com.pet.businessdomain.shareddto.enumentities.products.ProductSlot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // ========== BÚSQUEDAS BÁSICAS ==========
    List<Product> findByActiveTrue();

    Optional<Product> findByIdAndActiveTrue(Long id);

    List<Product> findByCategory(ProductCategory category);

    List<Product> findByCategoryAndActiveTrue(ProductCategory category);

    List<Product> findByRarity(ProductRarity rarity);

    List<Product> findBySlot(ProductSlot slot);

    // ========== BÚSQUEDAS POR NOMBRE ==========
    boolean existsByName(String name);

    List<Product> findByNameContainingIgnoreCase(String name);

    // ========== BÚSQUEDAS POR PRECIO ==========
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findByDiscountPriceIsNotNullAndDiscountStartDateBeforeAndDiscountEndDateAfter(
            LocalDate now, LocalDate now2);

    // ========== BÚSQUEDAS POR NIVEL ==========
    List<Product> findByRequiredLevelLessThanEqual(Integer level);

    // ========== BÚSQUEDAS POR TEMPORADA ==========
    List<Product> findBySeason(String season);

    List<Product> findByIsEventItemTrueAndEventStartDateBeforeAndEventEndDateAfter(
            LocalDate now, LocalDate now2);

    // ========== BÚSQUEDAS POR TAGS ==========
    @Query("SELECT DISTINCT p FROM Product p JOIN p.tags t WHERE t IN :tags")
    List<Product> findByTagsIn(@Param("tags") List<String> tags);

    @Query("SELECT DISTINCT p FROM Product p JOIN p.tags t WHERE t = :tag")
    List<Product> findByTag(@Param("tag") String tag);

    // ========== BÚSQUEDAS CON FILTROS (PAGINADAS) ==========
    @Query("SELECT p FROM Product p WHERE " +
            "(:search IS NULL OR LOWER(p.name) LIKE CONCAT('%', LOWER(CAST(:search AS text)), '%')) AND " +
            "(:category IS NULL OR p.category = :category) AND " +
            "(:rarity IS NULL OR p.rarity = :rarity) AND " +
            "(:slot IS NULL OR p.slot = :slot) AND " +
            "(:minLevel IS NULL OR p.requiredLevel >= :minLevel) AND " +
            "(:maxLevel IS NULL OR p.requiredLevel <= :maxLevel) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:isConsumable IS NULL OR p.isConsumable = :isConsumable) AND " +
            "(:isEventItem IS NULL OR p.isEventItem = :isEventItem) AND " +
            "(:season IS NULL OR p.season = :season) AND " +
            "p.active = true")
    Page<Product> findAllWithFilters(@Param("search") String search,
                                     @Param("category") ProductCategory category,
                                     @Param("rarity") ProductRarity rarity,
                                     @Param("slot") ProductSlot slot,
                                     @Param("minLevel") Integer minLevel,
                                     @Param("maxLevel") Integer maxLevel,
                                     @Param("minPrice") BigDecimal minPrice,
                                     @Param("maxPrice") BigDecimal maxPrice,
                                     @Param("isConsumable") Boolean isConsumable,
                                     @Param("isEventItem") Boolean isEventItem,
                                     @Param("season") String season,
                                     Pageable pageable);

    // ========== OFERTAS ==========
    @Query("SELECT p FROM Product p WHERE p.discountPrice IS NOT NULL " +
            "AND p.discountStartDate <= CURRENT_DATE " +
            "AND p.discountEndDate >= CURRENT_DATE " +
            "AND p.active = true")
    List<Product> findActiveDiscounts();

    // ========== RECOMENDACIONES ==========
    @Query(value = "SELECT p.* FROM products p " +
            "JOIN character_inventory ci ON ci.product_id = p.id " +
            "WHERE ci.character_id = :characterId " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(ci.id) DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Product> findRecommendedProducts(@Param("characterId") Long characterId,
                                          @Param("limit") int limit);

    // ========== ESTADÍSTICAS ==========
    @Query("SELECT COUNT(p) FROM Product p WHERE p.active = true")
    long countActiveProducts();

    @Query("SELECT AVG(p.rating) FROM Product p WHERE p.ratingCount > 0")
    Double getAverageGlobalRating();
    @Query("SELECT DISTINCT p FROM Product p " +
            "LEFT JOIN FETCH p.instantEffects " +
            "LEFT JOIN FETCH p.passiveEffects " +
            "WHERE p.id = :id")
    Optional<Product> findByIdWithEffects(@Param("id") Long id);

    // ========== PRODUCTOS DISPONIBLES PARA PERSONAJE ==========
    @Query("SELECT p FROM Product p WHERE " +
            "p.active = true AND " +
            "(:search IS NULL OR LOWER(p.name) LIKE CONCAT('%', LOWER(CAST(:search AS text)), '%') OR LOWER(p.description) LIKE CONCAT('%', LOWER(CAST(:search AS text)), '%')) AND " +
            "(:category IS NULL OR p.category = :category) AND " +
            "(:rarity IS NULL OR p.rarity = :rarity) AND " +
            "(:slot IS NULL OR p.slot = :slot) AND " +
            "(:minLevel IS NULL OR p.requiredLevel >= :minLevel) AND " +
            "(:maxLevel IS NULL OR p.requiredLevel <= :maxLevel) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:isConsumable IS NULL OR p.isConsumable = :isConsumable) AND " +
            "(:isEventItem IS NULL OR p.isEventItem = :isEventItem) AND " +
            "(:season IS NULL OR p.season = :season)")
    Page<Product> findAvailableProductsForCharacter(
            @Param("search") String search,
            @Param("category") ProductCategory category,
            @Param("rarity") ProductRarity rarity,
            @Param("slot") ProductSlot slot,
            @Param("minLevel") Integer minLevel,
            @Param("maxLevel") Integer maxLevel,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("isConsumable") Boolean isConsumable,
            @Param("isEventItem") Boolean isEventItem,
            @Param("season") String season,
            Pageable pageable
    );
}