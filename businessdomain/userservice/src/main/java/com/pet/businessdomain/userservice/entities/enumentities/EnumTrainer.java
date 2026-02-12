package com.pet.businessdomain.userservice.entities.enumentities;

public class EnumTrainer {

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
}