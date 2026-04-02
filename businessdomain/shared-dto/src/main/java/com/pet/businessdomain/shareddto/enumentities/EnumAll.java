package com.pet.businessdomain.shareddto.enumentities;

public class EnumAll {

    public enum CurrentSituation {
        high_school,
        vocational_training,
        university,
        unemployed,
        working
    }

    public enum FamilySituation {
        stable_family,
        working_family,
        difficult_situation
    }
    public enum ExamStatus {
        PENDING, PASSED, FAILED
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

    public enum AcademicPerformance {
        low,
        average,
        high,
        excellent
    }

    public enum CareerInterest {
        TECHNOLOGY,
        HEALTH,
        CONSTRUCTION,
        BUSINESS,
        CREATIVE,
        SOCIAL,
        SCIENCE,
        HOSPITALITY,
        ARTS,
        EDUCATION,
        SPORTS,
        OTHER
    }

    public enum Aspiration {
        university,
        vocational_training,
        employment,
        entrepreneurship
    }

    public enum AvailableTime {
        full_time,
        part_time,
        limited
    }

    public enum EconomicSupport {
        none,
        partial_support,
        full_support
    }

    public enum GameDifficulty {
        easy,
        realistic,
        hard
    }

    public enum GameFocus {
        narrative,
        strategic,
        economic
    }
    public enum Gender {
        male,
        female,
        nonbinary,
        unspecified
    }


    public enum ProgressLevel {
        LEVEL_1,
        LEVEL_2,
        LEVEL_3,
        LEVEL_4,
        LEVEL_5
    }
    public enum OwnerType {
        CHARACTER,
        COMPANY,
        NPC
    }
    public enum Frequency {
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY,
        OTHER
    }
    public enum TransactionType {
        INCOME,
        EXPENSE,
        TRANSFER
    }
    public enum ExpenseCategory {
        FOOD,
        HOUSING,
        TRANSPORT,
        EDUCATION,
        HEALTH,
        LEISURE,
        OTHER
    }
    public enum IncomeCategory {
        SALARY,
        BUSINESS,
        INVESTMENT,
        SCHOLARSHIP,
        OTHER
    }

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
    public enum NotificationResourceType {
        PRODUCT,
        COURSE,
        CHARACTER,
        CHAT,
        POST,
        SYSTEM
    }

    public enum ProductUsageType {
        CONSUMABLE,   // se consume (1 uso)
        REUSABLE,     // se queda para siempre
        SUBSCRIPTION  // efecto periódico (mensual, etc.)
    }

    public enum WorkingDay {
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY
    }
}