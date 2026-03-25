package com.pet.businessdomain.productservice.services;

import com.pet.businessdomain.shareddto.dto.CharacterDto;
import com.pet.businessdomain.shareddto.dto.CharacterInventoryResponseDTO;

import java.util.List;

public interface CharacterInventoryService {

    CharacterDto buyProduct(Long characterId, Long productId, Integer quantity);

    List<CharacterInventoryResponseDTO> getInventory(Long characterId);
}