package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.formationservice.entities.CharacterTraining;
import com.pet.businessdomain.formationservice.entities.Formation;
import com.pet.businessdomain.formationservice.exceptions.BusinessRuleException;
import com.pet.businessdomain.shareddto.dto.CharacterTrainingDto;

import java.util.List;

public interface ICharacterTrainingService {
    List<CharacterTraining> getTrainingsForCharacter(Long characterId);
    List<CharacterTraining> getCompletedTrainings(Long characterId);
    com.pet.businessdomain.shareddto.dto.CharacterTrainingDto subscribeToCourse(CharacterTrainingDto dto, Long id) throws BusinessRuleException;
    List<Formation> getAvailableCoursesForCharacter(Long characterId);
}
