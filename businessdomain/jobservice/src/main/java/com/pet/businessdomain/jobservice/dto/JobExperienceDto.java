package com.pet.businessdomain.jobservice.dto;

import com.pet.businessdomain.jobservice.entities.enumjobs.JobCategory;
import lombok.Data;

import java.util.List;

@Data
public class JobExperienceDto {

    private Long id;

    private JobCategory category;

    private Integer minYears;
    private Integer minLevel;
    private Integer minXp;

    private List<String> requiredSkills;
    private List<String> optionalSkills;
}
