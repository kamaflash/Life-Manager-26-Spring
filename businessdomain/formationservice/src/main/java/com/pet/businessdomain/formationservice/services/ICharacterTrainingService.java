package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.formationservice.dto.CharacterTrainingDto;
import com.pet.businessdomain.formationservice.entities.CharacterTraining;
import com.pet.businessdomain.formationservice.entities.Formation;
import com.pet.businessdomain.formationservice.entities.enumentities.Enum;
import com.pet.businessdomain.formationservice.exceptions.BusinessRuleException;

import java.util.List;

public interface ICharacterTrainingService {
    List<CharacterTraining> getTrainingsForCharacter(Long characterId);
    List<CharacterTraining> getCompletedTrainings(Long characterId);
    List<Formation> getAvailableFormations(Long characterId, com.pet.businessdomain.formationservice.entities.enumentities.Enum.EducationLevel eduLevel, int academicXp, int academicLevel, Enum.CareerInterest career);
    CharacterTrainingDto subscribeToCourse(CharacterTrainingDto dto) throws BusinessRuleException;
    List<Formation> getAvailableCoursesForCharacter(Long characterId);
}
