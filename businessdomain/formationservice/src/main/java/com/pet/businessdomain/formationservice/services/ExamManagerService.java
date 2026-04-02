package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.formationservice.entities.CharacterExam;

public interface ExamManagerService {

    /**
     * Permite a un personaje realizar un examen de una formación
     *
     * @param characterId id del personaje
     * @param trainingId id del CharacterTraining
     * @return CharacterExam creado con resultado
     * @throws IllegalStateException si no puede hacer el examen
     */
    CharacterExam takeExam(Long characterId, Long trainingId);
}
