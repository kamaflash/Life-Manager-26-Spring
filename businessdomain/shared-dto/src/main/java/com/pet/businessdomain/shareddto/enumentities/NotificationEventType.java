package com.pet.businessdomain.shareddto.enumentities;

public enum NotificationEventType {
    // COURSE events
    COURSE_COMPLETED,
    EXAM_PASSED,
    EXAM_FAILED,
    NEW_COURSE,
    COURSE_ENROLLMENT,

    // SCHOLARSHIP events
    SCHOLARSHIP_APPROVED,
    SCHOLARSHIP_REJECTED,
    SCHOLARSHIP_APPLIED,

    // JOB events
    JOB_OFFER,
    JOB_APPLICATION_ACCEPTED,
    JOB_APPLICATION_REJECTED,

    // GENERAL
    WELCOME,
    ACHIEVEMENT_UNLOCKED,
    LEVEL_UP,
    //PRODUCT
    NEW_PRODUCT
}

