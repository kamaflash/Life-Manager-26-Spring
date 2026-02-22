package com.pet.businessdomain.jobservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class SocialExperienceDto {

    private Long id;

    private String eventType;

    private String date;

    private Integer durationHours;
    private Integer participants;

    private Integer socialScore;

    private List<SkillXpDto> skillsGained;
}
