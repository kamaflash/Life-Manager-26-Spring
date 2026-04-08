package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.formationservice.entities.CharacterExam;
import com.pet.businessdomain.formationservice.entities.CharacterTraining;
import com.pet.businessdomain.formationservice.entities.Formation;
import com.pet.businessdomain.formationservice.entities.FormationExam;
import com.pet.businessdomain.formationservice.exceptions.BusinessRuleException;
import com.pet.businessdomain.shareddto.dto.CharacterTrainingDto;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICharacterTrainingService {

    // =========================
    // Consultas básicas
    // =========================
    CharacterTraining getById(Long id);

    List<CharacterTraining> getTrainingsForCharacter(Long characterId);

    List<CharacterTraining> getCompletedTrainings(Long characterId);

    List<CharacterTraining> getByCharacterIdAndStatus(Long characterId, EnumAll.TrainingStatus status);
    CharacterTraining getByCharacterIdAndTrainingId(Long id, Long trainingId);
    // =========================
    // Suscripción y creación
    // =========================
    CharacterTrainingDto subscribeToCourse(CharacterTrainingDto dto, Long characterId) throws BusinessRuleException;

    CharacterTrainingDto updateTraining(Long id, CharacterTrainingDto dto) throws BusinessRuleException;

    // =========================
    // Entrenamientos disponibles según perfil RPG
    // =========================
    List<Formation> getAvailableCoursesForCharacter(Long characterId);

    Page<Formation> getAvailableCoursesForCharacter(Long characterId, Pageable pageable);

    // =========================
    // Exámenes RPG
    // =========================
    List<FormationExam> getExamsForTraining(Long trainingId);

//    List<CharacterExam> getExamsForCharacter(Long characterId);

    // =========================
    // Acciones de estudio
    // =========================
    CharacterTrainingDto study(Long trainingId, int hours) throws BusinessRuleException;

    // =========================
    // Guardar y eliminar
    // =========================
    CharacterTraining save(CharacterTraining training);

    void deleteById(Long id);

    void deleteAll();

    void setStasCharacter(CharacterTrainingDto dto);
}
