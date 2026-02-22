package com.pet.businessdomain.userservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class CharacterDto {

    private Long id;
    private Long uid;

    private String name;
    private String avatar;

    private Integer age;
    private LocalDateTime birthDate;
    private String gender;

    private BigDecimal newIncome;
    private BigDecimal newExpense;
    private String city;
    private String residentialCity;
    private StatsDto stats;
    private Integer xpAcademy;
    private Integer xpJobs;
    private Map<String, SkillStateDto> skills;

    private List<CharacterTrainingDto> education;
    private List<JobPositionDto> jobs;
    private List<SocialExperienceDto> social;

    private List<String> interests;
    private List<String> friends;

    private String partnerId;
    private List<String> familyIds;

    private List<ItemDto> inventory;

    private String createdAt;
    private String updatedAt;
    private List<SFinanceAccountResponseDto> accounts;
}
