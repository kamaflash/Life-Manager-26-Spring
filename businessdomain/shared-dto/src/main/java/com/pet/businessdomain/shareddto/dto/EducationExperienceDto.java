package com.pet.businessdomain.shareddto.dto;

import lombok.Data;

import java.util.List;

@Data
public class EducationExperienceDto {

    private Long id;

    private String institution;
    private String courseName;

    private String startDate;
    private String endDate;

    private Integer hours;
    private String level;
    private Integer grade;

    private List<SkillXpDto> skillsGained;

    private String notes;

    private String type;        // formal, online, self-taught
    private Integer performance; // 0-100
    private Boolean completed;
}

