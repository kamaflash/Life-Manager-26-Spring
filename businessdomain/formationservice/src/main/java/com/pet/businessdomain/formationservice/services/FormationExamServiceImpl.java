package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.formationservice.entities.FormationExam;
import com.pet.businessdomain.formationservice.mapper.FormationExamMapper;
import com.pet.businessdomain.formationservice.mapper.FormationMapper;
import com.pet.businessdomain.formationservice.repository.FormationExamRepository;
import com.pet.businessdomain.shareddto.dto.FormationExamDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FormationExamServiceImpl implements FormationExamService {

    @Autowired
    private FormationExamRepository examRepository;
    @Autowired
    private FormationExamMapper examMapper;

    @Override
    public FormationExam getById(Long id) {
        log.debug("Buscando examen con ID: {}", id);
        return examRepository.findById(id)
                .orElse(null);
    }

    @Override
    public List<FormationExamDto> getByFormationId(Long formationId) {
        log.debug("Buscando exámenes para formación: {}", formationId);
        return examMapper.toDtoList(examRepository.findByFormationIdAndActiveTrue(formationId));
    }

    @Override
    public FormationExam save(FormationExam exam) {
        if (exam == null) {
            log.warn("Intento de guardar examen nulo");
            throw new IllegalArgumentException("El examen no puede ser nulo");
        }
        
        if (exam.getFormationId() == null) {
            log.warn("Intento de guardar examen sin formacionId");
            throw new IllegalArgumentException("El formationId es requerido");
        }
        
        log.info("Guardando examen para formación: {}", exam.getFormationId());
        return examRepository.save(exam);
    }
}
