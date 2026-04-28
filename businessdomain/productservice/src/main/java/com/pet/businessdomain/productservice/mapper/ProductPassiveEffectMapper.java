package com.pet.businessdomain.productservice.mapper;

import com.pet.businessdomain.productservice.entities.ProductPassiveEffect;
import com.pet.businessdomain.shareddto.dto.products.ProductPassiveEffectDTO;
import com.pet.businessdomain.shareddto.dto.products.TriggerConditionDTO;
import com.pet.businessdomain.shareddto.enumentities.products.TriggerCondition;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductPassiveEffectMapper {

    // ========== ENTITY → DTO ==========
    @Mapping(target = "formattedDescription", source = ".", qualifiedByName = "formatDescription")
    @Mapping(target = "trigger", source = "trigger", qualifiedByName = "mapTriggerToDTO")
    // ✅ ELIMINAR: @Mapping(target = "product", ignore = true) - No existe en DTO
    ProductPassiveEffectDTO toDto(ProductPassiveEffect entity);

    List<ProductPassiveEffectDTO> toDtoList(List<ProductPassiveEffect> entities);

    // ========== DTO → ENTITY ==========
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trigger", source = "trigger", qualifiedByName = "mapTriggerToEntity")
    // ✅ ELIMINAR: @Mapping(target = "product", ignore = true) - No es necesario, se queda null
    ProductPassiveEffect toEntity(ProductPassiveEffectDTO dto);

    List<ProductPassiveEffect> toEntityList(List<ProductPassiveEffectDTO> dtos);

    // ========== UPDATE ==========
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    // ✅ ELIMINAR: @Mapping(target = "product", ignore = true) - No es necesario
    void updateEntity(ProductPassiveEffectDTO dto, @MappingTarget ProductPassiveEffect entity);

    // ========== QUALIFIERS ==========
    @Named("formatDescription")
    default String formatDescription(ProductPassiveEffect effect) {
        if (effect == null) return "";

        String prefix = effect.getIsPercentage() ? "+" : "";
        String suffix = effect.getIsPercentage() ? "%" : "";
        String triggerText = effect.getTrigger() != null ? effect.getTrigger().getDisplayName() : "Siempre";

        String effectText = String.format("%s %s%d%s", effect.getStat(), prefix, effect.getValue(), suffix);

        if (effect.getChancePercentage() != null && effect.getChancePercentage() < 100) {
            effectText = String.format("%s (%d%% de probabilidad)", effectText, effect.getChancePercentage());
        }

        return String.format("%s - %s", effectText, triggerText);
    }

    @Named("mapTriggerToDTO")
    default TriggerConditionDTO mapTriggerToDTO(TriggerCondition trigger) {
        if (trigger == null) return TriggerConditionDTO.ALWAYS;
        return TriggerConditionDTO.valueOf(trigger.name());
    }

    @Named("mapTriggerToEntity")
    default TriggerCondition mapTriggerToEntity(TriggerConditionDTO dto) {
        if (dto == null) return TriggerCondition.ALWAYS;
        return TriggerCondition.valueOf(dto.name());
    }
}