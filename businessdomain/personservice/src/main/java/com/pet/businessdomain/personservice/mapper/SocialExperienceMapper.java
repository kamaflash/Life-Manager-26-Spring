package com.pet.businessdomain.personservice.mapper;

import com.pet.businessdomain.personservice.dto.SocialExperienceDto;
import com.pet.businessdomain.personservice.entities.SocialExperienceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SocialExperienceMapper {
    SocialExperienceDto toDto(SocialExperienceEntity entity);
    SocialExperienceEntity toEntity(SocialExperienceDto dto);
}

