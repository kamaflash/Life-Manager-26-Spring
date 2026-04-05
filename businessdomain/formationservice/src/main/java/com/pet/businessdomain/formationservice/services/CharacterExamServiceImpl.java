package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.formationservice.entities.CharacterExam;
import com.pet.businessdomain.formationservice.repository.CharacterExamRepository;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CharacterExamServiceImpl implements CharacterExamService {

    private final CharacterExamRepository examRepository;

    @Override
    public CharacterExam getById(Long id) {
        log.debug("Buscando examen de personaje con ID: {}", id);
        return examRepository.findById(id)
                .orElse(null);
    }

    @Override
    public List<CharacterExam> getByCharacterId(Long characterId) {
        log.debug("Buscando exámenes para personaje: {}", characterId);
        return examRepository.findByCharacterId(characterId);
    }

    @Override
    public List<CharacterExam> getByCharacterTrainingId(Long trainingId) {
        log.debug("Buscando exámenes para entrenamiento: {}", trainingId);
        return examRepository.findByCharacterTrainingId(trainingId);
    }

    @Override
    public Optional<CharacterExam> getLastAttempt(Long characterTrainingId) {
        log.debug("Buscando último intento para entrenamiento: {}", characterTrainingId);
        return examRepository.findTopByCharacterTrainingIdOrderByAttemptNumberDesc(characterTrainingId);
    }

    @Override
    public CharacterExam save(CharacterExam exam) {
        if (exam == null) {
            log.warn("Intento de guardar examen de personaje nulo");
            throw new IllegalArgumentException("El examen no puede ser nulo");
        }
        
        log.info("Guardando examen para personaje: {}", exam.getCharacterId());
        return examRepository.save(exam);
    }

    @Override
    public int countAttempts(Long characterTrainingId, Long formationExamId) {
        log.debug("Contando intentos para entrenamiento: {}, examen: {}", characterTrainingId, formationExamId);
        return examRepository.countByCharacterTrainingIdAndFormationExamId(characterTrainingId, formationExamId);
    }

    @Override
    public boolean existsPassed(Long characterTrainingId) {
        log.debug("Verificando si hay examen pasado para entrenamiento: {}", characterTrainingId);
        return examRepository.existsByCharacterTrainingIdAndStatus(characterTrainingId, EnumAll.ExamStatus.PASSED);
    }
}
