package com.pet.businessdomain.formationservice.entities;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "formation_exams")
@Data
public class FormationExam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "formation_id")
    private Long formationId;

    private String name; // Ej: "Examen final Java"

    private Integer requiredHours; // ej: 500

    @Enumerated(EnumType.STRING)
    private EnumAll.DifficultyLevel difficulty;

    private Integer maxScore; // ej: 10 o 100

    private Boolean mandatory; // si es obligatorio aprobar

    private Integer minPassingScore; // ej: 5

    private Integer maxAttempts;

    private Boolean active = true;

    private Integer weight;
    @ManyToOne
    @JoinColumn(name = "formation_id", insertable = false, updatable = false)
    private Formation formation;
    @OneToMany(mappedBy = "formationExam")
    private List<CharacterExam> attempts;
}
