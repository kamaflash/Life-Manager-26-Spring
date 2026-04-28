package com.pet.businessdomain.shareddto.dto.products;

import com.pet.businessdomain.shareddto.enumentities.ProductCategory;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class InventorySummaryDTO {
    private Long characterId;
    private Integer totalItems;
    private Integer uniqueItems;
    private Integer equippedItems;

    // Agrupaciones
    private Map<ProductCategory, Integer> itemsByCategory;
    private Map<ProductRarityDTO, Integer> itemsByRarity;
    private Map<ProductSlotDTO, Integer> itemsBySlot;

    // Listas
    private List<CharacterInventoryDTO> recentAcquisitions;
    private List<CharacterInventoryDTO> expiringSoon;
    private List<CharacterInventoryDTO> equipped;
    private List<CharacterInventoryDTO> onCooldown;

    // Estadísticas
    private Integer totalValue;
    private Integer totalPassiveBonuses;
}
