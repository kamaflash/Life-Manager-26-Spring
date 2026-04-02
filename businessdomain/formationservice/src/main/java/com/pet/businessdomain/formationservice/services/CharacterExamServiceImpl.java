package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.formationservice.entities.CharacterExam;
import com.pet.businessdomain.formationservice.repository.CharacterExamRepository;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CharacterExamServiceImpl implements CharacterExamService {

    private final CharacterExamRepository examRepository;

    @Override
    public CharacterExam getById(Long id) {
        return examRepository.findById(id).orElse(null);
    }

    @Override
    public List<CharacterExam> getByCharacterId(Long characterId) {
        return examRepository.findByCharacterId(characterId);
    }

    @Override
    public List<CharacterExam> getByCharacterTrainingId(Long trainingId) {
        return examRepository.findByCharacterTrainingId(trainingId);
    }

    @Override
    public Optional<CharacterExam> getLastAttempt(Long characterTrainingId) {
        return examRepository.findTopByCharacterTrainingIdOrderByAttemptNumberDesc(characterTrainingId);
    }

    @Override
    public CharacterExam save(CharacterExam exam) {
        return examRepository.save(exam);
    }

    @Override
    public int countAttempts(Long characterTrainingId, Long formationExamId) {
        return examRepository.countByCharacterTrainingIdAndFormationExamId(characterTrainingId, formationExamId);
    }

    @Override
    public boolean existsPassed(Long characterTrainingId) {
        return examRepository.existsByCharacterTrainingIdAndStatus(characterTrainingId, EnumAll.ExamStatus.PASSED);
    }
}
