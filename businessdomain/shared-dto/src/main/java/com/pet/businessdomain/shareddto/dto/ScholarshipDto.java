package com.pet.businessdomain.shareddto.dto;

import lombok.Data;

import java.time.LocalDate;

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
