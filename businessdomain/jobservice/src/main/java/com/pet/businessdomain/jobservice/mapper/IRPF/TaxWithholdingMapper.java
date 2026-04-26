package com.pet.businessdomain.jobservice.mapper.IRPF;

import com.pet.businessdomain.jobservice.entities.IRPF.TaxWithholdingEntity;
import com.pet.businessdomain.shareddto.dto.IRPF.MonthlyWithholdingDTO;
import com.pet.businessdomain.shareddto.dto.IRPF.TaxWithholdingCreateDTO;
import com.pet.businessdomain.shareddto.dto.IRPF.TaxWithholdingDTO;
import com.pet.businessdomain.shareddto.dto.IRPF.TaxWithholdingHistoryDTO;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TaxWithholdingMapper {

    // ENTITY → DTO
    TaxWithholdingDTO toDto(TaxWithholdingEntity entity);

    List<TaxWithholdingDTO> toDtoList(List<TaxWithholdingEntity> entities);

    // CREATE DTO → ENTITY
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    TaxWithholdingEntity toEntity(TaxWithholdingCreateDTO dto);

    // UPDATE
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(TaxWithholdingCreateDTO dto, @MappingTarget TaxWithholdingEntity entity);

    // Para el histórico agregado
    @Mapping(target = "characterId", source = "characterId")
    @Mapping(target = "year", source = "year")
    @Mapping(target = "monthlyWithholdings", source = "entities", qualifiedByName = "toMonthlyWithholdings")
    @Mapping(target = "totalAnnualGross", source = "entities", qualifiedByName = "sumAnnualGross")
    @Mapping(target = "totalAnnualWithheld", source = "entities", qualifiedByName = "sumAnnualWithheld")
    @Mapping(target = "averageRate", source = "entities", qualifiedByName = "calculateAverageRate")
    TaxWithholdingHistoryDTO toHistoryDto(Long characterId, Integer year, List<TaxWithholdingEntity> entities);

    @Named("toMonthlyWithholdings")
    default List<MonthlyWithholdingDTO> toMonthlyWithholdings(List<TaxWithholdingEntity> entities) {
        if (entities == null) return List.of();

        return entities.stream()
                .map(entity -> {
                    MonthlyWithholdingDTO dto = new MonthlyWithholdingDTO();
                    dto.setPeriod(YearMonth.of(entity.getYear(), entity.getMonth()));
                    dto.setGrossIncome(entity.getGrossIncome());
                    dto.setIrpfRate(entity.getIrpfRate());
                    dto.setIrpfWithheld(entity.getIrpfWithheld());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Named("sumAnnualGross")
    default BigDecimal sumAnnualGross(List<TaxWithholdingEntity> entities) {
        if (entities == null) return BigDecimal.ZERO;
        return entities.stream()
                .map(TaxWithholdingEntity::getGrossIncome)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Named("sumAnnualWithheld")
    default BigDecimal sumAnnualWithheld(List<TaxWithholdingEntity> entities) {
        if (entities == null) return BigDecimal.ZERO;
        return entities.stream()
                .map(TaxWithholdingEntity::getIrpfWithheld)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Named("calculateAverageRate")
    default BigDecimal calculateAverageRate(List<TaxWithholdingEntity> entities) {
        if (entities == null || entities.isEmpty()) return BigDecimal.ZERO;

        BigDecimal totalGross = sumAnnualGross(entities);
        BigDecimal totalWithheld = sumAnnualWithheld(entities);

        if (totalGross.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;

        return totalWithheld.multiply(new BigDecimal("100"))
                .divide(totalGross, 2, java.math.RoundingMode.HALF_UP);
    }
}