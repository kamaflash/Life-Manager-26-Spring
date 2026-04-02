package com.pet.businessdomain.shareddto.dto;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CharacterExamDto {

    private Long id;

    private Long characterId;
    private Long formationExamId;
    private Long characterTrainingId;

    private EnumAll.ExamStatus status;

    private Integer score;

    private Integer attemptNumber;

    private LocalDateTime examDate;
}
