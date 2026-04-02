package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.formationservice.entities.FormationExam;

import java.util.List;

public interface FormationExamService {

    FormationExam getById(Long id);

    List<FormationExam> getByFormationId(Long formationId);

    FormationExam save(FormationExam exam);
}
