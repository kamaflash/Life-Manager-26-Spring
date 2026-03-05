package com.pet.businessdomain.productservice.repository;

import com.pet.businessdomain.productservice.entities.CharacterInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CharacterInventoryRepository extends JpaRepository<CharacterInventory, Long> {

    List<CharacterInventory> findByCharacterId(Long characterId);

    Optional<CharacterInventory> findByCharacterIdAndProductId(Long characterId, Long productId);
}
