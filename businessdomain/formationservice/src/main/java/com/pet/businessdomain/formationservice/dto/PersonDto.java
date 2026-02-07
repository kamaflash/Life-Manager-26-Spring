package com.pet.businessdomain.formationservice.dto;

import com.pet.businessdomain.formationservice.entities.CharacterExpenses;
import com.pet.businessdomain.formationservice.entities.enumentities.EnumPerson;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PersonDto {

    private Long id;
    private Long uid;

    // ===== IDENTIDAD =====
    private String fullName;
    private Integer age;
    private String birthDate;
    private String city;

    private EnumPerson.Gender gender;

    private LocalDateTime createdAt;

    // ===== SITUACIÓN VITAL =====
    private EnumPerson.CurrentSituation currentSituation;
    private EnumPerson.FamilySituation familySituation;
    private List<String> familyExpectations;

    // ===== EDUCACIÓN =====
    private EnumPerson.EducationLevel educationLevel;
    private String educationField;
    private String educationSpecialization;

    private EnumPerson.AcademicPerformance academicPerformance;
    private List<String> educationGaps;

    // ===== PROFESIONAL / FUTURO =====
    private EnumPerson.CareerInterest careerInterest;
    private EnumPerson.Aspiration aspiration;
    private EnumPerson.AvailableTime availableTime;

    private List<String> learningResources;
    private List<String> technicalSkills;
    private List<String> strengths;

    // ===== ECONOMÍA =====
    private EnumPerson.EconomicSupport economicSupport;
    private Double monthlyIncome;
    private List<String> incomeSources;
    private Double savings;
    private Double debts;

    private CharacterExpenses expenses;

    // ===== RETOS, APOYO Y METAS =====
    private List<String> challenges;
    private List<String> supportSystem;

    private List<String> longTermGoals;
    private List<String> shortTermGoals;

    private Integer academicXp;
    private Integer workXp;
    private EnumPerson.ProgressLevel academicLevel;
    private EnumPerson.ProgressLevel workLevel;

}
