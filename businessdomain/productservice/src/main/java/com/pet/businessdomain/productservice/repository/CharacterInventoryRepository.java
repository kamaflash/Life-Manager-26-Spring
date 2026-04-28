package com.pet.businessdomain.productservice.repository;

import com.pet.businessdomain.productservice.entities.CharacterInventory;
import com.pet.businessdomain.productservice.entities.Product;
import com.pet.businessdomain.shareddto.enumentities.ProductCategory;
import com.pet.businessdomain.shareddto.enumentities.products.ProductRarity;
import com.pet.businessdomain.shareddto.enumentities.products.ProductSlot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CharacterInventoryRepository extends JpaRepository<CharacterInventory, Long> {

    // ========== BÚSQUEDAS BÁSICAS ==========
    List<CharacterInventory> findByCharacterId(Long characterId);

    Optional<CharacterInventory> findByCharacterIdAndProductId(Long characterId, Long productId);

    // ========== BÚSQUEDAS POR ESTADO ==========
    List<CharacterInventory> findByCharacterIdAndEquippedTrue(Long characterId);

    List<CharacterInventory> findByCharacterIdAndActiveUntilAfter(Long characterId, LocalDateTime now);

    List<CharacterInventory> findByCharacterIdAndExpiresAtBefore(Long characterId, LocalDateTime now);

    List<CharacterInventory> findByCharacterIdAndLastUsedAtBefore(Long characterId, LocalDateTime date);

    // ========== BÚSQUEDAS POR PRODUCTO ==========
    List<CharacterInventory> findByProductId(Long productId);

    List<CharacterInventory> findByCharacterIdAndProductIn(Long characterId, List<Product> products);

    // ========== VERIFICACIONES ==========
    boolean existsByCharacterIdAndProductIdAndEquippedTrue(Long characterId, Long productId);

    @Query("SELECT COUNT(i) > 0 FROM CharacterInventory i WHERE i.characterId = :characterId AND i.product.id = :productId AND i.quantity > 0")
    boolean hasProduct(@Param("characterId") Long characterId, @Param("productId") Long productId);

    // ========== SUMATORIOS ==========
    @Query("SELECT SUM(i.quantity) FROM CharacterInventory i WHERE i.characterId = :characterId")
    Integer countTotalItemsByCharacter(@Param("characterId") Long characterId);

    @Query("SELECT COUNT(DISTINCT i.product.id) FROM CharacterInventory i WHERE i.characterId = :characterId")
    Integer countUniqueItemsByCharacter(@Param("characterId") Long characterId);

    // ========== ACTUALIZACIONES ==========
    @Modifying
    @Transactional
    @Query("UPDATE CharacterInventory i SET i.quantity = i.quantity + :quantity WHERE i.id = :id")
    void addQuantity(@Param("id") Long id, @Param("quantity") Integer quantity);

    @Modifying
    @Transactional
    @Query("UPDATE CharacterInventory i SET i.quantity = i.quantity - :quantity WHERE i.id = :id AND i.quantity >= :quantity")
    int removeQuantity(@Param("id") Long id, @Param("quantity") Integer quantity);

    @Modifying
    @Transactional
    @Query("UPDATE CharacterInventory i SET i.equipped = :equipped, i.equippedSince = :equippedSince WHERE i.id = :id")
    void updateEquippedStatus(@Param("id") Long id,
                              @Param("equipped") Boolean equipped,
                              @Param("equippedSince") LocalDateTime equippedSince);

    @Modifying
    @Transactional
    @Query("UPDATE CharacterInventory i SET i.activeUntil = :activeUntil WHERE i.id = :id")
    void updateActiveUntil(@Param("id") Long id, @Param("activeUntil") LocalDateTime activeUntil);

    @Modifying
    @Transactional
    @Query("UPDATE CharacterInventory i SET i.durabilityLeft = i.durabilityLeft - 1 WHERE i.id = :id AND i.durabilityLeft > 0")
    int decrementDurability(@Param("id") Long id);

    @Modifying
    @Transactional
    void deleteByCharacterIdAndProductId(Long characterId, Long productId);

    // ========== LIMPIEZA ==========
    @Modifying
    @Transactional
    void deleteByCharacterIdAndExpiresAtBefore(Long characterId, LocalDateTime date);

    @Query("SELECT DISTINCT p FROM Product p " +
            "LEFT JOIN FETCH p.instantEffects " +
            "LEFT JOIN FETCH p.passiveEffects " +
            "WHERE p.id = :id")
    Optional<Product> findByCharacterIdWithFullProduct(@Param("id") Long id);
    /**
     * Busca productos en el inventario de un personaje con filtros avanzados.
     *
     * @param characterId ID del personaje
     * @param search Término de búsqueda (nombre del producto)
     * @param category Categoría del producto
     * @param rarity Rareza del producto
     * @param slot Ranura del producto
     * @param minLevel Nivel mínimo requerido
     * @param maxLevel Nivel máximo requerido
     * @param equipped Estado de equipamiento (true/false/null)
     * @param pageable Configuración de paginación
     * @return Página de inventario del personaje
     */
    @Query("SELECT ci FROM CharacterInventory ci " +
            "WHERE ci.characterId = :characterId " +
            "AND (:search IS NULL OR LOWER(ci.product.name) LIKE CONCAT('%', LOWER(CAST(:search AS text)), '%') " +
            "                  OR LOWER(ci.product.description) LIKE CONCAT('%', LOWER(CAST(:search AS text)), '%')) " +
            "AND (:category IS NULL OR ci.product.category = :category) " +
            "AND (:rarity IS NULL OR ci.product.rarity = :rarity) " +
            "AND (:slot IS NULL OR ci.product.slot = :slot) " +
            "AND (:minLevel IS NULL OR ci.product.requiredLevel >= :minLevel) " +
            "AND (:maxLevel IS NULL OR ci.product.requiredLevel <= :maxLevel) " +
            "AND (:equipped IS NULL OR ci.equipped = :equipped)")
    Page<CharacterInventory> findInventoryByCharacterIdAndFilters(
            @Param("characterId") Long characterId,
            @Param("search") String search,
            @Param("category") ProductCategory category,
            @Param("rarity") ProductRarity rarity,
            @Param("slot") ProductSlot slot,
            @Param("minLevel") Integer minLevel,
            @Param("maxLevel") Integer maxLevel,
            @Param("equipped") Boolean equipped,
            Pageable pageable
    );
}