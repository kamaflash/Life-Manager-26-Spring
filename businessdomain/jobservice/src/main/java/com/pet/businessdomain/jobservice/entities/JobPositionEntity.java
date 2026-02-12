package com.pet.businessdomain.jobservice.entities;

import com.pet.businessdomain.jobservice.entities.enumjobs.JobCategory;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "job_positions")
@Data
public class JobPositionEntity {

    @Id
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private JobCategory category;

    private String requiredEducationLevel;
    private Integer minXp;
    private Boolean active = true;

    @ElementCollection
    @CollectionTable(
            name = "job_position_required_skills",
            joinColumns = @JoinColumn(name = "position_id")
    )
    @Column(name = "skill")
    private List<String> requiredSkills;

    // 👉 Empresa
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    // 👉 Experiencia requerida
    @OneToOne(mappedBy = "jobPosition", cascade = CascadeType.ALL, orphanRemoval = true)
    private JobExperienceEntity experienceRequired;

    // 👉 Vacantes
    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobVacancyEntity> vacancies;
}
