package com.pet.businessdomain.productservice.controller;

import com.pet.businessdomain.productservice.dto.CharacterInventoryResponseDTO;
import com.pet.businessdomain.productservice.services.CharacterInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class CharacterInventoryController {

    private final CharacterInventoryService inventoryService;

    @PostMapping("/buy")
    public void buyProduct(
            @RequestParam Long characterId,
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") Integer quantity
    ) {
        inventoryService.buyProduct(characterId, productId, quantity);
    }

    @GetMapping("/{characterId}")
    public List<CharacterInventoryResponseDTO> getInventory(@PathVariable Long characterId) {
        return inventoryService.getInventory(characterId);
    }
}
