package com.pet.businessdomain.personservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "job_experience")
@Data
public class JobExperienceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Empresa / Rol
    private String company;
    private String role;

    // Fechas
    private String startDate;
    private String endDate;

    // Economía / rendimiento
    private Double monthlySalary;
    private Integer performanceScore; // 0-100

    // Responsabilidades
    @ElementCollection
    @CollectionTable(
            name = "job_responsibilities",
            joinColumns = @JoinColumn(name = "job_id")
    )
    @Column(name = "responsibility")
    private List<String> responsibilities;

    // Skills ganadas
    @ElementCollection
    @CollectionTable(
            name = "job_skills_gained",
            joinColumns = @JoinColumn(name = "job_id")
    )
    private List<SkillXpEmbeddable> skillsGained;
}
