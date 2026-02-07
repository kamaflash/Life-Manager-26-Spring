package com.pet.businessdomain.personservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "education_experience")
@Data
public class EducationExperienceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String institution;
    private String courseName;
    private String startDate;
    private String endDate;

    private Integer hours;
    private String level;
    private Integer grade;

    @ElementCollection
    private List<SkillXpEmbeddable> skillsGained;

    private String notes;
    private String type;
    private Integer performance;
    private Boolean completed;
}

