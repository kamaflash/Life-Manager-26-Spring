package com.pet.businessdomain.financeservice.mapper;

import com.pet.businessdomain.financeservice.entities.TransactionEntity;
import com.pet.businessdomain.shareddto.dto.TransactionResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true)
    TransactionEntity toEntity(TransactionResponseDto dto);

    TransactionResponseDto toDto(TransactionEntity entity);
    List<TransactionResponseDto> toDtoList(List<TransactionEntity> finances);

}
