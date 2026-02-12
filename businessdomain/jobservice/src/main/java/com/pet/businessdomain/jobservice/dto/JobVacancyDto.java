package com.pet.businessdomain.jobservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class JobVacancyDto {

    private Long id;

    private BigDecimal salary;
    private Integer payNumber;
    private Boolean active;

    /** Referencia al puesto */
    private Long positionId;
}
