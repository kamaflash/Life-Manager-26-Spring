package com.pet.businessdomain.userservice.dto;

import com.pet.businessdomain.userservice.entities.enumentities.JobCategory;
import lombok.Data;

import java.util.List;

@Data
public class JobPositionDto {

    private Long id;

    private String title;
    private String description;

    private JobCategory category;

    private String requiredEducationLevel;
    private Integer minXp;
    private Boolean active;

    private String contractType;             // FULL_TIME, PART_TIME, INTERNSHIP, FREELANCE
    private String workModality;             // ONSITE, REMOTE, HYBRID
    private Integer salaryMin;               // Salario mínimo estimado
    private Integer salaryMax;               // Salario máximo estimado
    private String location;
    // Otros campos opcionales para realismo
    private Boolean remoteFriendly;          // Si acepta teletrabajo
    private Boolean visaSponsorship;         // Si ofrece soporte de visa
    private String perks;
    private List<String> requiredSkills;

    /** Empresa */
    private Long companyId;
    private String companyName;

    /** Experiencia requerida */
    private JobExperienceDto experienceRequired;

    /** Vacantes */
    private List<JobVacancyDto> vacancies;
}
