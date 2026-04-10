package com.pet.businessdomain.shareddto.dto;
import com.pet.businessdomain.shareddto.enumentities.JobCategory;
import lombok.Data;
import java.util.List;

@Data
public class JobPositionDTO {
    private Long id;
    private String title;
    private String level; // INTERN, JUNIOR, SEMI_SENIOR, SENIOR, LEAD, MANAGER, DIRECTOR, VP, C_LEVEL
    private String description;
    private JobCategory category;
    private Long companyId;
    private String companyName;
    private Boolean active;
    private String careerPath; // TECHNOLOGY, BUSINESS, HEALTH, CREATIVE, CONSTRUCTION, SOCIAL
    private List<String> perks;
    private List<JobVacancyDTO> vacancies;
}