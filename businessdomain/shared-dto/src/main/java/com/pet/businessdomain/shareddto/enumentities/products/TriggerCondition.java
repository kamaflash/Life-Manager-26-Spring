package com.pet.businessdomain.shareddto.enumentities.products;

public enum TriggerCondition {
    ON_USE("Al usar"),
    DAILY_RESET("Cada día"),
    ON_WORK_START("Al empezar a trabajar"),
    ON_WORK_END("Al terminar de trabajar"),
    ON_STUDY_START("Al empezar a estudiar"),
    ON_STUDY_END("Al terminar de estudiar"),
    ON_TRAVEL("Al viajar"),
    ALWAYS("Siempre activo");

    private final String displayName;

    TriggerCondition(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }
}
