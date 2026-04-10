package com.pet.businessdomain.shareddto.dto;


import lombok.Data;

import java.util.List;

@Data
public class JobApplicationResultDTO {
    private Boolean success;
    private String message;
    private JobApplicationDTO application;
    private Integer matchScore;
    private List<String> matchedSkills;
    private List<String> missingRequirements;
    private List<String> recommendations; // sugerencias para mejorar
}
