package com.pet.businessdomain.eventservice.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnumMapper {

    default String map(Enum<?> value) {
        return value != null ? value.name() : null;
    }

    default <T extends Enum<T>> T map(String value, Class<T> enumClass) {
        return value != null ? Enum.valueOf(enumClass, value) : null;
    }
}
