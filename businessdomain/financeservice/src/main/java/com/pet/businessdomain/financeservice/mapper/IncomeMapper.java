package com.pet.businessdomain.financeservice.mapper;

import com.pet.businessdomain.financeservice.dto.CreateIncomeRequestDto;
import com.pet.businessdomain.financeservice.dto.IncomeResponseDto;
import com.pet.businessdomain.financeservice.entities.IncomeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IncomeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true)
    IncomeEntity fromCreate(CreateIncomeRequestDto dto);

    IncomeResponseDto toDto(IncomeEntity entity);
}
