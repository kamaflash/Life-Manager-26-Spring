package com.pet.businessdomain.formationservice.entities;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "character_exams")
@Data
public class CharacterExam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long characterId;
    @Column(name = "formation_exam_id")
    private Long formationExamId;
    @Column(name = "character_training_id")
    private Long characterTrainingId;
    private Boolean passed;
    @Enumerated(EnumType.STRING)
    private EnumAll.ExamStatus status;
    // PENDING, PASSED, FAILED

    private Integer score;

    private Integer attemptNumber;

    private LocalDateTime examDate;

    @ManyToOne
    @JoinColumn(name = "formation_exam_id", insertable = false, updatable = false)
    private FormationExam formationExam;

    @ManyToOne
    @JoinColumn(name = "character_training_id", insertable = false, updatable = false)
    private CharacterTraining characterTraining;
}
