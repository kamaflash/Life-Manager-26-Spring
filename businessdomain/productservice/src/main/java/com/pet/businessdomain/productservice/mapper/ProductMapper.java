package com.pet.businessdomain.productservice.mapper;

import com.pet.businessdomain.productservice.entities.Product;
import com.pet.businessdomain.shareddto.dto.products.ProductDTO;
import com.pet.businessdomain.shareddto.dto.products.ProductRarityDTO;
import com.pet.businessdomain.shareddto.dto.products.ProductSlotDTO;
import com.pet.businessdomain.shareddto.enumentities.products.ProductRarity;
import com.pet.businessdomain.shareddto.enumentities.products.ProductSlot;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // ========== ENTITY → DTO ==========
    @Mapping(target = "finalPrice", source = ".", qualifiedByName = "calculateFinalPrice")
    @Mapping(target = "averageRating", source = ".", qualifiedByName = "calculateAverageRating")
    @Mapping(target = "rarity", source = "rarity", qualifiedByName = "mapRarityToDTO")
    @Mapping(target = "slot", source = "slot", qualifiedByName = "mapSlotToDTO")
    @Mapping(target = "pa", source = "pa")
    @Mapping(target = "instantEffects", source = "instantEffects")  // ← Ya no ignorar
    @Mapping(target = "passiveEffects", source = "passiveEffects")  // ← Ya no ignorar
    ProductDTO toDto(Product entity);

    List<ProductDTO> toDtoList(List<Product> entities);

    // ========== DTO → ENTITY ==========
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalUsageCount", constant = "0")
    @Mapping(target = "rating", constant = "0")
    @Mapping(target = "ratingCount", constant = "0")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "instantEffects", ignore = true)
    @Mapping(target = "passiveEffects", ignore = true)
    @Mapping(target = "pa", source = "pa")
    Product toEntity(ProductDTO dto);

    // ========== UPDATE ==========
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "totalUsageCount", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "ratingCount", ignore = true)
    @Mapping(target = "instantEffects", ignore = true)
    @Mapping(target = "passiveEffects", ignore = true)
    @Mapping(target = "pa", source = "pa")
    void updateEntity(ProductDTO dto, @MappingTarget Product entity);

    // ========== QUALIFIERS ==========
    @Named("calculateFinalPrice")
    default BigDecimal calculateFinalPrice(Product product) {
        if (product == null) return BigDecimal.ZERO;
        LocalDate now = LocalDate.now();
        if (product.getDiscountPrice() != null &&
                product.getDiscountStartDate() != null &&
                product.getDiscountEndDate() != null &&
                !now.isBefore(product.getDiscountStartDate()) &&
                !now.isAfter(product.getDiscountEndDate())) {
            return product.getDiscountPrice();
        }
        return product.getPrice();
    }

    @Named("calculateAverageRating")
    default Double calculateAverageRating(Product product) {
        if (product == null || product.getRatingCount() == null || product.getRatingCount() == 0) {
            return 0.0;
        }
        return (double) product.getRating() / product.getRatingCount();
    }

    @Named("mapRarityToDTO")
    default ProductRarityDTO mapRarityToDTO(ProductRarity rarity) {
        if (rarity == null) return ProductRarityDTO.COMMON;
        return ProductRarityDTO.valueOf(rarity.name());
    }

    @Named("mapSlotToDTO")
    default ProductSlotDTO mapSlotToDTO(ProductSlot slot) {
        if (slot == null) return ProductSlotDTO.NONE;
        return ProductSlotDTO.valueOf(slot.name());
    }
}