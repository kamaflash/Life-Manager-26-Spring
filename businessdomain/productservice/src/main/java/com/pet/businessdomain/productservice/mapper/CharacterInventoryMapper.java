package com.pet.businessdomain.productservice.mapper;

import com.pet.businessdomain.productservice.entities.CharacterInventory;
import com.pet.businessdomain.productservice.entities.Product;
import com.pet.businessdomain.productservice.repository.ProductRepository;
import com.pet.businessdomain.shareddto.dto.products.CharacterInventoryDTO;
import com.pet.businessdomain.shareddto.dto.products.ProductDTO;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public abstract class CharacterInventoryMapper {

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected ProductMapper productMapper;

    // ========== ENTITY → DTO ==========
    @Mapping(target = "product", source = "product", qualifiedByName = "mapProductWithEffects")
    @Mapping(target = "isOnCooldown", source = ".", qualifiedByName = "calculateIsOnCooldown")
    @Mapping(target = "cooldownRemainingSeconds", source = ".", qualifiedByName = "calculateCooldownRemaining")
    @Mapping(target = "isExpired", source = ".", qualifiedByName = "calculateIsExpired")
    @Mapping(target = "isActive", source = ".", qualifiedByName = "calculateIsActive")
    public abstract CharacterInventoryDTO toDto(CharacterInventory entity);

    public abstract List<CharacterInventoryDTO> toDtoList(List<CharacterInventory> entities);

    // ========== DTO → ENTITY ==========
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "acquiredAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "durabilityLeft", source = "product.durability")
    @Mapping(target = "product", source = "product")
    public abstract CharacterInventory toEntity(CharacterInventoryDTO dto);

    // ========== UPDATE ==========
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "acquiredAt", ignore = true)
    @Mapping(target = "product", ignore = true)
    public abstract void updateEntity(CharacterInventoryDTO dto, @MappingTarget CharacterInventory entity);

    // ========== QUALIFIERS ==========

    @Named("mapProductWithEffects")
    protected ProductDTO mapProductWithEffects(Product product) {
        if (product == null) return null;
        // Recargar el producto completo con efectos desde el repositorio
        Product fullProduct = productRepository.findByIdWithEffects(product.getId())
                .orElse(product);
        return productMapper.toDto(fullProduct);
    }

    @Named("calculateIsOnCooldown")
    protected Boolean calculateIsOnCooldown(CharacterInventory inventory) {
        if (inventory == null || inventory.getLastUsedAt() == null) return false;
        if (inventory.getProduct() == null || inventory.getProduct().getCooldownHours() == null) return false;

        LocalDateTime cooldownEnd = inventory.getLastUsedAt()
                .plusHours(inventory.getProduct().getCooldownHours());
        return LocalDateTime.now().isBefore(cooldownEnd);
    }

    @Named("calculateCooldownRemaining")
    protected Long calculateCooldownRemaining(CharacterInventory inventory) {
        if (inventory == null || inventory.getLastUsedAt() == null) return 0L;
        if (inventory.getProduct() == null || inventory.getProduct().getCooldownHours() == null) return 0L;

        LocalDateTime cooldownEnd = inventory.getLastUsedAt()
                .plusHours(inventory.getProduct().getCooldownHours());
        long secondsRemaining = java.time.Duration.between(LocalDateTime.now(), cooldownEnd).getSeconds();
        return Math.max(0, secondsRemaining);
    }

    @Named("calculateIsExpired")
    protected Boolean calculateIsExpired(CharacterInventory inventory) {
        if (inventory == null || inventory.getExpiresAt() == null) return false;
        return LocalDateTime.now().isAfter(inventory.getExpiresAt());
    }

    @Named("calculateIsActive")
    protected Boolean calculateIsActive(CharacterInventory inventory) {
        if (inventory == null) return false;
        if (inventory.getActiveUntil() == null) return true;
        return LocalDateTime.now().isBefore(inventory.getActiveUntil());
    }
}