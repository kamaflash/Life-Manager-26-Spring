package com.pet.businessdomain.jobservice.dto;

import com.pet.businessdomain.jobservice.entities.enumjobs.JobCategory;
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

    private List<String> requiredSkills;

    /** Empresa */
    private Long companyId;
    private String companyName;

    /** Experiencia requerida */
    private JobExperienceDto experienceRequired;

    /** Vacantes */
    private List<JobVacancyDto> vacancies;
}
