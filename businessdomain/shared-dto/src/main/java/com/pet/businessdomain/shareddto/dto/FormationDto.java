package com.pet.businessdomain.shareddto.dto;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Data
public class FormationDto {
    private Long id;

    private String code;
    private String name;
    private String description;

    private EnumAll.CareerInterest category;
    private EnumAll.TrainingType type;
    private EnumAll.DifficultyLevel difficulty;

    private EnumAll.EducationLevel minEducationLevel;

    private Integer minAcademicLevel;
    private Integer minAcademicXp;
    private Integer maxAcademicXp;

    private List<EnumAll.CareerInterest> allowedCareers;

    private Integer durationHours;
    private BigDecimal cost;
    private Integer effort;

    private Integer academicXpReward;

    private List<String> skillsUnlocked;

    private Boolean repeatable;
    private Boolean active;

    private Integer level;
    private Boolean locked;

    private LocalTime startTime;
    private LocalTime endTime;

    private List<EnumAll.WorkingDay> workingDays;
}
