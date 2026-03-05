package com.pet.businessdomain.productservice.services;

import com.pet.businessdomain.productservice.dto.CharacterInventoryResponseDTO;
import com.pet.businessdomain.productservice.entities.CharacterInventory;
import com.pet.businessdomain.productservice.entities.Product;
import com.pet.businessdomain.productservice.repository.CharacterInventoryRepository;
import com.pet.businessdomain.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacterInventoryServiceImpl implements CharacterInventoryService {

    private final CharacterInventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Override
    public void buyProduct(Long characterId, Long productId, Integer quantity) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CharacterInventory inventory = inventoryRepository
                .findByCharacterIdAndProductId(characterId, productId)
                .orElse(null);

        if (inventory != null) {
            inventory.setQuantity(inventory.getQuantity() + quantity);
        } else {
            inventory = new CharacterInventory();
            inventory.setCharacterId(characterId);
            inventory.setProduct(product);
            inventory.setQuantity(quantity);
        }

        inventoryRepository.save(inventory);
    }

    @Override
    public List<CharacterInventoryResponseDTO> getInventory(Long characterId) {

        return inventoryRepository.findByCharacterId(characterId)
                .stream()
                .map(inv -> CharacterInventoryResponseDTO.builder()
                        .productId(inv.getProduct().getId())
                        .productName(inv.getProduct().getName())
                        .category(inv.getProduct().getCategory())
                        .quantity(inv.getQuantity())
                        .build()
                )
                .toList();
    }
}
