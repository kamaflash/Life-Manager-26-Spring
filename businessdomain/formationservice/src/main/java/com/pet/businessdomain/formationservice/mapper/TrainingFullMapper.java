package com.pet.businessdomain.formationservice.mapper;

import com.pet.businessdomain.formationservice.entities.CharacterExam;
import com.pet.businessdomain.formationservice.entities.CharacterTraining;
import com.pet.businessdomain.formationservice.entities.FormationExam;
import com.pet.businessdomain.shareddto.dto.TrainingWithExamDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrainingFullMapper {

    @Mapping(source = "training", target = "training")
    @Mapping(source = "exam", target = "exam")
    @Mapping(source = "attempt", target = "lastAttempt")
    TrainingWithExamDto toDto(
            CharacterTraining training,
            FormationExam exam,
            CharacterExam attempt
    );
}
