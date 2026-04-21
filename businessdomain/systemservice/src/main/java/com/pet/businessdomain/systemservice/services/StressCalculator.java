package com.pet.businessdomain.systemservice.services;

import org.springframework.stereotype.Component;

@Component
public class StressCalculator {

    private static final int STRESS_REDUCTION_PER_REST_HOUR = -3;  // -3 estrés por hora de descanso
    private static final int STRESS_INCREASE_PER_STUDY_HOUR = 2;   // +2 estrés por hora de estudio
    private static final int STRESS_INCREASE_PER_WORK_HOUR = 4;    // +4 estrés por hora de trabajo

    public int calculateForRest(long hours) {
        return (int) (hours * STRESS_REDUCTION_PER_REST_HOUR);
    }

    public int calculateForStudy(long hours) {
        return (int) (hours * STRESS_INCREASE_PER_STUDY_HOUR);
    }

    public int calculateForWork(long hours) {
        return (int) (hours * STRESS_INCREASE_PER_WORK_HOUR);
    }
}
