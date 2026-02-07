package com.pet.businessdomain.financeservice.mapper;


import com.pet.businessdomain.financeservice.dto.FinanceAccountResponseDto;
import com.pet.businessdomain.financeservice.entities.FinanceAccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {IncomeMapper.class, ExpenseMapper.class} // usamos los mappers de relaciones
)
public interface FinanceAccountMapper {

    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    FinanceAccountEntity toEntity(FinanceAccountResponseDto dto);

    FinanceAccountResponseDto toDto(FinanceAccountEntity entity);

    List<FinanceAccountResponseDto> toDtoList(List<FinanceAccountEntity> finances);

}

