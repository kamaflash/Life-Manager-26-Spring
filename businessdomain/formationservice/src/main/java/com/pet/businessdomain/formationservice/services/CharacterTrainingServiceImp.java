package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.formationservice.entities.*;
import com.pet.businessdomain.formationservice.mapper.ICharacterTrainingMapper;
import com.pet.businessdomain.formationservice.repository.ICharacterTrainingRepository;
import com.pet.businessdomain.formationservice.repository.FormationExamRepository;
import com.pet.businessdomain.formationservice.repository.FormationRepository;
import com.pet.businessdomain.shareddto.dto.CharacterTrainingDto;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.formationservice.exceptions.BusinessRuleException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CharacterTrainingServiceImp implements ICharacterTrainingService {

    private final ICharacterTrainingRepository characterTrainingRepository;
    private final FormationRepository formationRepository;
    private final FormationExamRepository formationExamRepository;
    private final ICharacterTrainingMapper characterTrainingMapper;

    // =========================
    // Consultas básicas
    // =========================
    @Override
    public CharacterTraining getById(Long id) {
        return characterTrainingRepository.findById(id).orElse(null);
    }

    @Override
    public List<CharacterTraining> getTrainingsForCharacter(Long characterId) {
        return characterTrainingRepository.findByCharacterId(characterId);
    }

    @Override
    public List<CharacterTraining> getCompletedTrainings(Long characterId) {
        return characterTrainingRepository.findByCharacterIdAndStatus(characterId, EnumAll.TrainingStatus.COMPLETED);
    }

    @Override
    public List<CharacterTraining> getByCharacterIdAndStatus(Long characterId, EnumAll.TrainingStatus status) {
        return characterTrainingRepository.findByCharacterIdAndStatus(characterId, status);
    }

    // =========================
    // Suscripción y actualización
    // =========================
    @Override
    public CharacterTrainingDto subscribeToCourse(CharacterTrainingDto dto, Long characterId) throws BusinessRuleException {
        if (dto.getTrainingId() == null) {
            throw new BusinessRuleException("2001", "TrainingId es obligatorio", null);
        }

        Formation formation = formationRepository.findById(dto.getTrainingId())
                .orElseThrow(() -> new BusinessRuleException("2002", "Formación no encontrada", null));


        CharacterTraining existing = characterTrainingRepository
                .findByCharacterIdAndTrainingId(characterId, formation.getId())
                .orElse(null);

        if (existing != null) {
            return characterTrainingMapper.toDto(existing); // ya suscrito
        }

        CharacterTraining training = new CharacterTraining();
        training.setCharacterId(characterId);
        training.setTrainingId(formation.getId());
        training.setTrainingName(formation.getName());
        training.setTrainingType(formation.getType());
        training.setTrainingDifficulty(formation.getDifficulty());
        training.setInvestedHours(0);
        training.setAcademicXpGained(0);
        training.setStatus(EnumAll.TrainingStatus.IN_PROGRESS);
        training.setApplied(true);
        training.setCost(formation.getCost());
        training.setStartedAt(LocalDateTime.now());
        training.setStats(new CharacterStats()); // stats iniciales

        CharacterTraining saved = characterTrainingRepository.save(training);

        return characterTrainingMapper.toDto(saved);
    }

    @Override
    public CharacterTrainingDto updateTraining(Long id, CharacterTrainingDto dto) throws BusinessRuleException {
        CharacterTraining training = characterTrainingRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("2004", "Entrenamiento no encontrado", null));

        // Actualizar horas invertidas y progreso RPG
        if (dto.getInvestedHours() != null) {
            training.setInvestedHours(dto.getInvestedHours());
            int progress = Math.min(100, training.getInvestedHours() * 100 / 500); // ejemplo: examen a 500h
            training.setProgress(progress);
        }

        if (dto.getStatus() != null) {
            training.setStatus(dto.getStatus());
        }

        CharacterTraining updated = characterTrainingRepository.save(training);
        return characterTrainingMapper.toDto(updated);
    }

    // =========================
    // Cursos disponibles RPG
    // =========================
    @Override
    public List<Formation> getAvailableCoursesForCharacter(Long characterId) {
        // Aquí podrías filtrar por educación, XP, carreras, etc.
        // Ejemplo simplificado: todas las activas
        return formationRepository.findByActiveTrue();
    }

    @Override
    public Page<Formation> getAvailableCoursesForCharacter(Long characterId, Pageable pageable) {
        List<Formation> list = getAvailableCoursesForCharacter(characterId);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

    // =========================
    // Exámenes RPG
    // =========================
    @Override
    public List<FormationExam> getExamsForTraining(Long trainingId) {
        return formationExamRepository.findByFormationId(trainingId);
    }

//    @Override
//    public List<CharacterExam> getExamsForCharacter(Long characterId) {
//        // Aquí podrías traer todos los exámenes asociados a los trainings del personaje
//        List<CharacterTraining> trainings = getTrainingsForCharacter(characterId);
//        List<Long> trainingIds = trainings.stream().map(CharacterTraining::getId).collect(Collectors.toList());
//        return formationExamRepository.findCharacterExamsByCharacterTrainingId(trainingIds);
//    }

    // =========================
    // Guardar y eliminar
    // =========================
    @Override
    public CharacterTraining save(CharacterTraining training) {
        return characterTrainingRepository.save(training);
    }
    @Override
    public CharacterTraining getByCharacterIdAndTrainingId(Long id, Long trainingId) {
        return characterTrainingRepository.getByCharacterIdAndTrainingId(id,trainingId);
    }
    @Override
    public void deleteById(Long id) {
        characterTrainingRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        characterTrainingRepository.deleteAll();
    }
}
