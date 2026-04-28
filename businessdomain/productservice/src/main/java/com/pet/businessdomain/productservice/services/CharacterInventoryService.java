package com.pet.businessdomain.productservice.services;


import com.pet.businessdomain.shareddto.dto.products.*;
import com.pet.businessdomain.shareddto.enumentities.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface CharacterInventoryService {

    // ========== COMPRA ==========
    BuyProductResponseDTO buyProduct(BuyProductRequestDTO request);

    // ========== USO ==========
    UseProductResponseDTO useProduct(UseProductRequestDTO request);

    // ========== EQUIPAR/DESEQUIPAR ==========
    CharacterInventoryDTO equipProduct(Long inventoryId, Long characterId);
    CharacterInventoryDTO unequipProduct(Long inventoryId, Long characterId);

    // ========== INVENTARIO ==========
    List<CharacterInventoryDTO> getInventory(Long characterId);
    List<CharacterInventoryDTO> getEquippedInventory(Long characterId);
    CharacterInventoryDTO getInventoryItem(Long inventoryId);

    // ========== CRAFTING ==========
    CraftProductResponseDTO craftProduct(CraftProductRequestDTO request);

    // ========== VENTA ==========
    SellProductResponseDTO sellProduct(Long inventoryId, Long characterId, Integer quantity);

    // ========== OFERTAS ==========
    List<ProductShopOfferDTO> getActiveShopOffers();
    BuyProductResponseDTO buyFromOffer(Long characterId, Long offerId, Integer quantity);

    // ========== EFECTOS PASIVOS ==========
    void applyPassiveEffects(Long characterId);
    void removePassiveEffects(Long characterId, Long productId);

    // ========== RESUMEN ==========
    InventorySummaryDTO getInventorySummary(Long characterId);
    Page<CharacterInventoryDTO> getIventoryForCharacter(
            Long characterId,
            Pageable pageable,
            String search,
            ProductCategory category,
            String rarity,
            String slot,
            Integer minLevel,
            Integer maxLevel,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean isConsumable,
            Boolean isEventItem,
            String season
    );
    Page<ProductDTO> getAvailableProductsForCharacter(
            Long characterId,
            Pageable pageable,
            String search,
            ProductCategory category,
            String rarity,
            String slot,
            Integer minLevel,
            Integer maxLevel,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean isConsumable,
            Boolean isEventItem,
            String season
    );
}