package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.formationservice.entities.FormationExam;
import com.pet.businessdomain.formationservice.repository.FormationExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormationExamServiceImpl implements FormationExamService {

    private final FormationExamRepository examRepository;

    @Override
    public FormationExam getById(Long id) {
        return examRepository.findById(id).orElse(null);
    }

    @Override
    public List<FormationExam> getByFormationId(Long formationId) {
        return examRepository.findByFormationIdAndActiveTrue(formationId);
    }

    @Override
    public FormationExam save(FormationExam exam) {
        return examRepository.save(exam);
    }
}
