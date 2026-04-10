package com.pet.businessdomain.shareddto.dto;


import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class JobApplicationDTO {
    private Long id;
    private Long characterId;
    private String characterName;
    private Long vacancyId;
    private String positionTitle;
    private String companyName;
    private LocalDateTime appliedAt;
    private String status; // PENDING, REVIEWING, INTERVIEW_SCHEDULED, OFFERED, HIRED, REJECTED, WITHDRAWN
    private String stage; // APPLICATION, CV_REVIEW, PHONE_SCREEN, TECHNICAL_TEST, TECHNICAL_INTERVIEW, HR_INTERVIEW, OFFER, HIRED
    private Integer matchScore;
    private LocalDateTime interviewDate;
    private String interviewNotes;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private List<String> exceededSkills;
}
