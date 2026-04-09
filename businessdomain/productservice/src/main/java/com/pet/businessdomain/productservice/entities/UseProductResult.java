package com.pet.businessdomain.productservice.entities;

import com.pet.businessdomain.shareddto.dto.CharacterDto;
import lombok.Data;

import java.util.List;

@Data
public class UseProductResult {
    private boolean success;
    private String message;
    private CharacterDto updatedCharacter;
    private CharacterInventory updatedInventory;
    private List<String> effectsApplied;
    private int xpGained;
    private int levelUps;
}
