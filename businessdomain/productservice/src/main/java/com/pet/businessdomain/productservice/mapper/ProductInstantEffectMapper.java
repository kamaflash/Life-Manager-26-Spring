package com.pet.businessdomain.productservice.mapper;

import com.pet.businessdomain.productservice.entities.ProductInstantEffect;
import com.pet.businessdomain.shareddto.dto.products.EffectTypeDTO;
import com.pet.businessdomain.shareddto.dto.products.ProductInstantEffectDTO;
import com.pet.businessdomain.shareddto.enumentities.products.EffectType;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductInstantEffectMapper {

    // ========== ENTITY → DTO ==========
    @Mapping(target = "formattedDescription", source = ".", qualifiedByName = "formatDescription")
    @Mapping(target = "type", source = "type", qualifiedByName = "mapEffectTypeToDTO")
    // ✅ NO hay campo "product" en ProductInstantEffectDTO, no se necesita ignore
    ProductInstantEffectDTO toDto(ProductInstantEffect entity);

    List<ProductInstantEffectDTO> toDtoList(List<ProductInstantEffect> entities);

    // ========== DTO → ENTITY ==========
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", source = "type", qualifiedByName = "mapEffectTypeToEntity")
    // ✅ El campo "product" en la entidad se queda null, está bien
    ProductInstantEffect toEntity(ProductInstantEffectDTO dto);

    List<ProductInstantEffect> toEntityList(List<ProductInstantEffectDTO> dtos);

    // ========== UPDATE ==========
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntity(ProductInstantEffectDTO dto, @MappingTarget ProductInstantEffect entity);

    // ========== QUALIFIERS ==========
    @Named("formatDescription")
    default String formatDescription(ProductInstantEffect effect) {
        if (effect == null) return "";

        String prefix = effect.getIsPercentage() ? "+" : "";
        String suffix = effect.getIsPercentage() ? "%" : "";
        String effectName = getEffectDisplayName(effect.getType());

        if (effect.getIsRandom() && effect.getMinAmount() != null && effect.getMaxAmount() != null) {
            return String.format("%s: %d-%d%s", effectName, effect.getMinAmount(), effect.getMaxAmount(), suffix);
        }

        return String.format("%s: %s%d%s", effectName, prefix, effect.getAmount(), suffix);
    }

    @Named("getEffectDisplayName")
    default String getEffectDisplayName(EffectType type) {
        if (type == null) return "";
        switch (type) {
            case HEALTH_RESTORE: return "Salud";
            case ENERGY_RESTORE: return "Energía";
            case STRESS_REDUCTION: return "Reducción de estrés";
            case XP_ACADEMY_BOOST: return "Bonus XP Académico";
            case XP_JOB_BOOST: return "Bonus XP Laboral";
            case TRAVEL_SPEED: return "Velocidad de viaje";
            case STUDY_EFFICIENCY: return "Eficiencia de estudio";
            case WORK_EFFICIENCY: return "Eficiencia laboral";
            case SALARY_BONUS: return "Bonus salarial";
            case HAPPINESS_BOOST: return "Felicidad";
            case DOUBLE_XP: return "Doble XP";
            default: return type.name();
        }
    }

    @Named("mapEffectTypeToDTO")
    default EffectTypeDTO mapEffectTypeToDTO(EffectType type) {
        if (type == null) return null;
        return EffectTypeDTO.valueOf(type.name());
    }

    @Named("mapEffectTypeToEntity")
    default EffectType mapEffectTypeToEntity(EffectTypeDTO dto) {
        if (dto == null) return null;
        return EffectType.valueOf(dto.name());
    }
}