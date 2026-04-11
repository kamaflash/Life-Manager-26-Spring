package com.pet.businessdomain.jobservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "job_requirements")
@Data
public class RequirementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String skill_key;
    private Integer minValue;
    private Boolean mandatory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacancy_id", nullable = false)
    @ToString.Exclude  // Evita recursión infinita
    private JobVacancyEntity vacancy;
}