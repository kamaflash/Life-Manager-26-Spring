package com.pet.businessdomain.productservice.mapper;

import com.pet.businessdomain.productservice.dto.ProductEffectDTO;
import com.pet.businessdomain.productservice.entities.ProductEffect;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductEffectMapper {

    ProductEffectDTO toDTO(ProductEffect entity);

    ProductEffect toEntity(ProductEffectDTO dto);
}
