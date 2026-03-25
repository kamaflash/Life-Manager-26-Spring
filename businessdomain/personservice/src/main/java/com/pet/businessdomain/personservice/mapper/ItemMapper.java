package com.pet.businessdomain.personservice.mapper;

import com.pet.businessdomain.shareddto.dto.ItemDto;
import com.pet.businessdomain.personservice.entities.ItemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemDto toDto(ItemEntity entity);
    ItemEntity toEntity(ItemDto dto);
}

