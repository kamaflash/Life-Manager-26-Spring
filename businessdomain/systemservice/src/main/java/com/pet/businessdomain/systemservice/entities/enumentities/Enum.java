package com.pet.businessdomain.systemservice.entities.enumentities;

public class Enum {

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
}
