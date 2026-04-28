package com.pet.businessdomain.productservice.mapper;

import com.pet.businessdomain.productservice.entities.Ingredient;
import com.pet.businessdomain.productservice.entities.Product;
import com.pet.businessdomain.shareddto.dto.products.IngredientDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface IngredientMapper {

    // ========== ENTITY → DTO ==========
    @Mapping(target = "product", source = "productId", qualifiedByName = "mapToProduct")
    IngredientDTO toDto(Ingredient entity);

    List<IngredientDTO> toDtoList(List<Ingredient> entities);

    // ========== DTO → ENTITY ==========
    @Mapping(target = "id", ignore = true)
    Ingredient toEntity(IngredientDTO dto);

    // ========== UPDATE ==========
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntity(IngredientDTO dto, @MappingTarget Ingredient entity);

    // ========== QUALIFIERS ==========
    @Named("mapToProduct")
    default Product mapToProduct(Long productId) {
        return null;
    }
}