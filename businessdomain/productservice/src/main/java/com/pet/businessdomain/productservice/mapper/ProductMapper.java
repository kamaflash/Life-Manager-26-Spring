package com.pet.businessdomain.productservice.mapper;

import com.pet.businessdomain.productservice.dto.ProductResponseDTO;
import com.pet.businessdomain.productservice.entities.Product;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
uses = { ProductEffectMapper.class }
)
public interface ProductMapper {

    ProductResponseDTO toDTO(Product entity);

    Product toEntity(ProductResponseDTO dto);
}
