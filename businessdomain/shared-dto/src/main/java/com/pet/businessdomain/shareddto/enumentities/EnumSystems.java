package com.pet.businessdomain.shareddto.enumentities;

public class EnumSystems {
    // AdvanceType.java
    public enum AdvanceType {
        NORMAL_DAY("Avance normal - jornada laboral/estudio"),
        WEEKEND("Avance fin de semana - descanso"),
        VACATION("Avance vacaciones - descanso extendido"),
        WORK_SHIFT("Avance turno de trabajo - próximamente"),
        STUDY_SESSION("Avance sesión de estudio - próximamente"),
        SLEEP("Avance dormir - próximamente"),
        FAST_TRAVEL("Avance rápido - próximamente");

        private final String description;

        AdvanceType(String description) { this.description = description; }
        public String getDescription() { return description; }
    }

    // DayType.java
    public enum DayType {
        WEEKDAY("Día laboral"),
        WEEKEND("Fin de semana"),
        VACATION("Vacaciones"),
        HOLIDAY("Festivo");

        private final String label;
        DayType(String label) { this.label = label; }
        public String getLabel() { return label; }
    }
}
