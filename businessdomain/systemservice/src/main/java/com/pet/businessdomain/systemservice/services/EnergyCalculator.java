package com.pet.businessdomain.systemservice.services;

import org.springframework.stereotype.Component;

@Component
public class EnergyCalculator {

    private static final int ENERGY_GAIN_PER_REST_HOUR = 8;   // +8 energía por hora de descanso
    private static final int ENERGY_COST_PER_STUDY_HOUR = -5; // -5 energía por hora de estudio
    private static final int ENERGY_COST_PER_WORK_HOUR = -8;  // -8 energía por hora de trabajo

    public int calculateForRest(long hours) {
        return (int) (hours * ENERGY_GAIN_PER_REST_HOUR);
    }

    public int calculateForStudy(long hours) {
        return (int) (hours * ENERGY_COST_PER_STUDY_HOUR);
    }

    public int calculateForWork(long hours) {
        return (int) (hours * ENERGY_COST_PER_WORK_HOUR);
    }
}
