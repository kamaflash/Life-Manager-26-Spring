package com.pet.businessdomain.userservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class JobExperienceDto {

    private Long id;

    private String company;
    private String role;

    private String startDate;
    private String endDate;

    private Double monthlySalary;
    private Integer performanceScore;

    private List<String> responsibilities;

    private List<SkillXpDto> skillsGained;
}
