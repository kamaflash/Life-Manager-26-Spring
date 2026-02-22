package com.pet.businessdomain.userservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class JobVacancyDto {

    private Long id;

    /** Salario y pagos */
    private BigDecimal salary;
    private Integer payNumber;
    private Boolean active;

    /** Fechas y slots */
    private LocalDate openingDate;
    private LocalDate closingDate;
    private Integer availableSlots;

    /** Detalles de la vacante */
    private String location;
    private String contractType;
    private String perks;
    private Boolean remoteFriendly;
    private Boolean visaSponsorship;

    /** Referencia al puesto */
    private Long positionId;
    private String positionTitle; // Opcional para mostrar directamente el título del puesto
}
