package com.pet.businessdomain.shareddto.dto;


import lombok.Data;

@Data
public class CompanySummaryDTO {
    private Long id;
    private String name;
    private String category;
    private String location;
    private String logoUrl;
    private String size;
    private Integer reputation;
    private Integer openPositions;
    private Integer totalEmployees;
}
