package com.pet.businessdomain.formationservice.repository;

import com.pet.businessdomain.formationservice.entities.CharacterTraining;
import com.pet.businessdomain.formationservice.entities.enumentities.Enum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICharacterTrainingRepository  extends JpaRepository<CharacterTraining, Long> {

    // Todos los cursos de un personaje
    List<CharacterTraining> findByCharacterId(Long characterId);

    // Solo los cursos completados
    List<CharacterTraining> findByCharacterIdAndStatus(Long characterId, Enum.TrainingStatus status);
    boolean existsByCharacterIdAndTrainingId(Long characterId, Long trainingId);
}
