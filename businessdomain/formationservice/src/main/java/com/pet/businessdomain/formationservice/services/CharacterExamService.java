package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.formationservice.entities.CharacterExam;

import java.util.List;
import java.util.Optional;

public interface CharacterExamService {

    CharacterExam getById(Long id);

    List<CharacterExam> getByCharacterId(Long characterId);

    List<CharacterExam> getByCharacterTrainingId(Long trainingId);

    Optional<CharacterExam> getLastAttempt(Long characterTrainingId);

    CharacterExam save(CharacterExam exam);

    int countAttempts(Long characterTrainingId, Long formationExamId);

    boolean existsPassed(Long characterTrainingId);

}
