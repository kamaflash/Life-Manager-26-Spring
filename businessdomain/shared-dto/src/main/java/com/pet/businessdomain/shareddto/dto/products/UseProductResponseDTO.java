package com.pet.businessdomain.shareddto.dto.products;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UseProductResponseDTO {
    private boolean success;
    private String message;
    private CharacterInventoryDTO updatedInventory;
    private List<String> effectsApplied;
    private int xpGained;
    private int levelUps;
    private List<StatChangeDTO> statChanges;
}
