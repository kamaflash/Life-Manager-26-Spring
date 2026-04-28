package com.pet.businessdomain.productservice.mapper;

import com.pet.businessdomain.productservice.entities.Product;
import com.pet.businessdomain.productservice.entities.ProductRecipe;
import com.pet.businessdomain.productservice.repository.ProductRepository;
import com.pet.businessdomain.shareddto.dto.products.ProductRecipeDTO;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductMapper.class, IngredientMapper.class})
public abstract class ProductRecipeMapper {

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected ProductMapper productMapper;  // ← Inyectar ProductMapper

    // ========== ENTITY → DTO ==========
    @Mapping(target = "resultProduct", ignore = true)
    @Mapping(target = "ingredients", source = "ingredients")
    public abstract ProductRecipeDTO toDto(ProductRecipe entity);

    public abstract List<ProductRecipeDTO> toDtoList(List<ProductRecipe> entities);

    // ========== DTO → ENTITY ==========
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ingredients", source = "ingredients")
    @Mapping(target = "resultProductId", source = "resultProduct.id")
    public abstract ProductRecipe toEntity(ProductRecipeDTO dto);

    // ========== UPDATE ==========
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    public abstract void updateEntity(ProductRecipeDTO dto, @MappingTarget ProductRecipe entity);

    // ========== AFTER MAPPING ==========
    @AfterMapping
    protected void afterToDto(ProductRecipe entity, @MappingTarget ProductRecipeDTO dto) {
        if (entity.getResultProductId() != null) {
            productRepository.findById(entity.getResultProductId())
                    .ifPresent(product -> dto.setResultProduct(productMapper.toDto(product)));  // ← Usar productMapper inyectado
        }
    }
}