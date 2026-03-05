package com.pet.businessdomain.productservice.services;

import com.pet.businessdomain.productservice.dto.CharacterInventoryResponseDTO;

import java.util.List;

public interface CharacterInventoryService {

    void buyProduct(Long characterId, Long productId, Integer quantity);

    List<CharacterInventoryResponseDTO> getInventory(Long characterId);
}