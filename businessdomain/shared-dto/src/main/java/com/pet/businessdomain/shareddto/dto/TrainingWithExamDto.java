package com.pet.businessdomain.shareddto.dto;

import lombok.Data;

@Data
public class TrainingWithExamDto {

    private CharacterTrainingDto training;
    private FormationExamDto exam;
    private CharacterExamDto lastAttempt;
}
