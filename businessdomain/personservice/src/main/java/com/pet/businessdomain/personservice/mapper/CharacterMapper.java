package com.pet.businessdomain.personservice.mapper;

import com.pet.businessdomain.personservice.dto.CharacterDto;
import com.pet.businessdomain.personservice.entities.CharacterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {
                StatsMapper.class,
                SkillStateMapper.class,
                EducationExperienceMapper.class,
                JobExperienceMapper.class,
                SocialExperienceMapper.class,
                ItemMapper.class,
                RelationshipMapper.class
        }
)
public interface CharacterMapper {

    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    CharacterEntity toEntity(CharacterDto dto);

    CharacterDto toDto(CharacterEntity entity);
}
