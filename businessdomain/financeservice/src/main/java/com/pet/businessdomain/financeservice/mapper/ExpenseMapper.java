package com.pet.businessdomain.financeservice.mapper;

import com.pet.businessdomain.financeservice.dto.CreateExpenseRequestDto;
import com.pet.businessdomain.financeservice.dto.ExpenseResponseDto;
import com.pet.businessdomain.financeservice.entities.ExpenseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    @Mapping(target = "id", ignore = true) // ID generado por JPA
    @Mapping(target = "account", ignore = true) // se asigna en el service
    ExpenseEntity fromCreate(CreateExpenseRequestDto dto);

    ExpenseResponseDto toDto(ExpenseEntity entity);
}
