package com.pet.businessdomain.formationservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ScholarshipDto {

    private Long id;
    private String title;
    private String description;
    private Double amount;
    private String country;
    private Boolean active;
    private Integer minXpRequired;
    private LocalDate startDate;
    private LocalDate endDate;

}
