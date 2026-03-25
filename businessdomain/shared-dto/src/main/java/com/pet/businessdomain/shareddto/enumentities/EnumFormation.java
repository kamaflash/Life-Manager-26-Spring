package com.pet.businessdomain.shareddto.enumentities;

public class EnumFormation {

    public enum TrainingStatus {
        AVAILABLE,
        IN_PROGRESS,
        COMPLETED,
        FAILED
    }

    public enum TrainingType {
        COURSE,
        DEGREE,
        MASTER,
        CERTIFICATION,
        WORKSHOP,
        VOCATIONAL_TRAINING,
        HIGH_SCHOOL
    }

    public enum DifficultyLevel {
        BASIC,
        INTERMEDIATE,
        ADVANCED,
        EXPERT
    }
    public enum EducationLevel {
        none,
        basic,
        secondary,
        vocational,
        technical,
        highschool,
        university
    }
    public enum CareerInterest {
        technology,
        health,
        arts,
        business,
        education,
        sports,
        construction,
        creative,
        social,
        science,
        hospitality,
        other
    }
    public enum ApplicationStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}