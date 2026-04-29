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
    // 🔥 NUEVOS ENUMS
    public enum JobLevel {
        INTERN, JUNIOR, SEMI_SENIOR, SENIOR, LEAD, MANAGER, DIRECTOR, VP, C_LEVEL
    }

    public enum CareerPath {
        TECHNOLOGY,      // Desarrollo, software, IT, datos, ciberseguridad
        BUSINESS,        // Finanzas, administración, marketing, ventas
        HEALTH,          // Medicina, enfermería, fisioterapia, salud mental
        CREATIVE,        // Diseño, arte, audiovisual, música, escritura
        CONSTRUCTION,    // Obra, arquitectura, electricidad, fontanería
        SOCIAL,          // Educación, trabajo social, psicología, orientación
        SCIENCE,         // Biología, química, física, investigación
        HOSPITALITY,     // Hostelería, turismo, restauración, hoteles
        SPORTS,          // Deporte, entrenamiento, actividad física
        ARTS,            // Artes plásticas, galerías, museos, conservación
        LEGAL,           // Derecho, abogacía, asesoría legal
        ENGINEERING,     // Ingeniería industrial, mecánica, eléctrica
        LOGISTICS,       // Transporte, logística, cadena de suministro
        AGRICULTURE,     // Agricultura, ganadería, agroindustria
        COMMUNICATION    // Periodismo, comunicación, relaciones públicas
    }

    // 🔥 NUEVOS ENUMS
    public enum ContractType {
        FULL_TIME, PART_TIME, INTERNSHIP, FREELANCE, TEMPORAL
    }

    public enum WorkModality {
        ONSITE, REMOTE, HYBRID
    }

    public enum ApplicationStatus {
        PENDING, REVIEWING, INTERVIEW_SCHEDULED, OFFERED, HIRED, REJECTED, WITHDRAWN, CONTRACTED
    }

    public enum ApplicationStage {
        APPLICATION, CV_REVIEW, PHONE_SCREEN, TECHNICAL_TEST, TECHNICAL_INTERVIEW, HR_INTERVIEW, OFFER, HIRED,CONTRACTED
    }

    public enum EventType {
        BONUS, PROMOTION, DEMOTION, CONFLICT, PROJECT_SUCCESS, PROJECT_FAILURE,
        BURNOUT, OFFER_FROM_RIVAL, MENTOR_LEAVES, TEAM_RESTRUCTURE, QUARTERLY_REVIEW,JOB_OFFER
    }

    public enum MissionCategory {
        STUDY, WORK, SOCIAL, ROMANCE, EXPLORATION, TRAINING, PERSONAL, SPECIAL
    }

    public enum MissionType {
        ONE_TIME, DAILY, WEEKLY, MONTHLY, REPEATABLE, STORY
    }

    public enum MissionStatus {
        ASSIGNED, ACTIVE, PAUSED, COMPLETED, FAILED, ABANDONED, EXPIRED
    }

    public enum MissionDifficulty {
        EASY, MEDIUM, HARD, EPIC
    }

    public enum EventCategory {
        STUDY, WORK, SOCIAL, ROMANCE, REST, TRAVEL, SPECIAL, RANDOM
    }

    public enum EventStatus {
        SCHEDULED, IN_PROGRESS, COMPLETED, SKIPPED, FAILED, CANCELLED
    }

    public enum InteractionType {
        NONE, SINGLE_CLICK, MULTI_CHOICE, TIMER, MINIGAME, QTE, FORM
    }

    public enum AssignmentSource {
        AUTOMATIC, TRIGGERED, MANUAL, STORY, DAILY_SCHEDULE
    }

    public enum RecurrencePattern {
        ONCE, DAILY, WEEKLY, WEEKDAYS, WEEKENDS, CUSTOM
    }

    // ObjectiveType.java
    public enum ObjectiveType {
        REACH_STAT, REACH_XP, COMPLETE_ACTIONS, BUY_ITEM,
        MAKE_FRIEND, GET_PARTNER, REACH_AGE, EARN_MONEY,
        ATTEND_EVENT, COMPLETE_MISSION
    }
    // RewardType.java
    public enum RewardType {
        XP_JOBS, XP_ACADEMY, STAT_BOOST, SKILL_BOOST,
        MONEY, ITEM, TITLE, UNLOCK_MISSION, UNLOCK_EVENT,
        RELATIONSHIP_BONUS, BADGE
    }
    // RequirementType.java
    public enum RequirementType {
        MIN_AGE, MIN_STAT, MIN_XP_JOBS, MIN_XP_ACADEMY,
        MISSION_COMPLETED, EVENT_ATTENDED, HAS_ITEM,
        HAS_PARTNER, HAS_FRIENDS_COUNT, MIN_MONEY, HAS_TITLE
    }
    // EventType.java
    // EventScope.java
    public enum EventScope { GLOBAL, CITY, PERSONAL }
    // EventParticipationStatus.java
    public enum EventParticipationStatus { INVITED, REGISTERED, ATTENDED, MISSED, SKIPPED }
    // EventOutcome.java
    public enum EventOutcome { SUCCESS, PARTIAL, FAILURE, NEUTRAL }
}