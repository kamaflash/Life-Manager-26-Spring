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

    private String requiredEducationLevel;   // HIGH_SCHOOL, VOCATIONAL, BACHELOR, etc.
    private Integer minXp;                   // Experiencia mínima en XP del juego
    private Boolean active = true;

    private String contractType;             // FULL_TIME, PART_TIME, INTERNSHIP, FREELANCE
    private String workModality;             // ONSITE, REMOTE, HYBRID
    private Integer salaryMin;               // Salario mínimo estimado
    private Integer salaryMax;               // Salario máximo estimado
    private String location;                 // Ciudad/País donde se ubica el trabajo

    @ElementCollection
    @CollectionTable(
            name = "job_position_required_skills",
            joinColumns = @JoinColumn(name = "position_id")
    )
    @Column(name = "skill")
    private List<String> requiredSkills;

    @ElementCollection
    @CollectionTable(
            name = "job_position_optional_skills",
            joinColumns = @JoinColumn(name = "position_id")
    )
    @Column(name = "skill")
    private List<String> optionalSkills;

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

    // Otros campos opcionales para realismo
    private Boolean remoteFriendly;          // Si acepta teletrabajo
    private Boolean visaSponsorship;         // Si ofrece soporte de visa
    private String perks;                     // Beneficios adicionales (seguros, bonos, etc.)
}
