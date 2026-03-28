package com.pet.businessdomain.formationservice.repository;

import com.pet.businessdomain.formationservice.entities.CharacterTraining;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICharacterTrainingRepository  extends JpaRepository<CharacterTraining, Long> {

    // Todos los cursos de un personaje
    List<CharacterTraining> findByCharacterId(Long characterId);

    // Solo los cursos completados
    List<CharacterTraining> findByCharacterIdAndStatus(Long characterId, EnumAll.TrainingStatus status);
    boolean existsByCharacterIdAndTrainingId(Long characterId, Long trainingId);
    CharacterTraining getByCharacterIdAndTrainingId(Long id, Long trainingId);
}
