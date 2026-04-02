package com.pet.businessdomain.formationservice.repository;

import com.pet.businessdomain.formationservice.entities.CharacterTraining;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ICharacterTrainingRepository  extends JpaRepository<CharacterTraining, Long> {

    List<CharacterTraining> findByCharacterId(Long characterId);

    Optional<CharacterTraining> findByCharacterIdAndTrainingId(Long characterId, Long trainingId);

    List<CharacterTraining> findByCharacterIdAndStatus(Long characterId, EnumAll.TrainingStatus status);

    List<CharacterTraining> findByStatus(EnumAll.TrainingStatus status);
    CharacterTraining getByCharacterIdAndTrainingId(Long id, Long trainingId);

}
