package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.formationservice.entities.FormationExam;
import com.pet.businessdomain.shareddto.dto.FormationExamDto;

import java.util.List;

public interface FormationExamService {

    FormationExam getById(Long id);

    List<FormationExamDto> getByFormationId(Long formationId);

    FormationExam save(FormationExam exam);
}
