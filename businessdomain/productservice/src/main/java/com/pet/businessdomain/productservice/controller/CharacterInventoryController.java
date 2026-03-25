package com.pet.businessdomain.productservice.controller;

import com.pet.businessdomain.shareddto.dto.CharacterDto;
import com.pet.businessdomain.shareddto.dto.CharacterInventoryResponseDTO;
import com.pet.businessdomain.productservice.services.CharacterInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class CharacterInventoryController {

    private final CharacterInventoryService inventoryService;

    @PostMapping("/buy/{characterId}/{productId}")
    public CharacterDto buyProduct(
            @PathVariable(name="characterId") Long characterId,
            @PathVariable(name="productId") Long productId,
            @RequestParam(name="quantity", defaultValue = "1") Integer quantity
    ) {
        return inventoryService.buyProduct(characterId, productId, quantity);
    }

    @GetMapping("/{characterId}")
    public List<CharacterInventoryResponseDTO> getInventory(@PathVariable(name="characterId") Long characterId) {
        return inventoryService.getInventory(characterId);
    }
}
