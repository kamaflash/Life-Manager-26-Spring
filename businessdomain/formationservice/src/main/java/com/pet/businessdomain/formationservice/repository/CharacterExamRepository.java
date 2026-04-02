package com.pet.businessdomain.formationservice.repository;

import com.pet.businessdomain.formationservice.entities.CharacterExam;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CharacterExamRepository extends JpaRepository<CharacterExam, Long> {

    List<CharacterExam> findByCharacterId(Long characterId);

    List<CharacterExam> findByCharacterTrainingId(Long characterTrainingId);

    List<CharacterExam> findByFormationExamId(Long formationExamId);

    // Último intento
    Optional<CharacterExam> findTopByCharacterTrainingIdOrderByAttemptNumberDesc(Long characterTrainingId);

    // Número de intentos
    int countByCharacterTrainingIdAndFormationExamId(Long characterTrainingId, Long formationExamId);

    // Saber si ya aprobó
    boolean existsByCharacterTrainingIdAndStatus(Long characterTrainingId, EnumAll.ExamStatus status);


}
