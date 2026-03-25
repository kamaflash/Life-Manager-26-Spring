package com.pet.businessdomain.shareddto.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class JobVacancyDto {

    private Long id;

    // 💰 Salario
    private BigDecimal salary;
    private Integer payNumber;

    // 📅 Estado
    private Boolean active;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private Integer availableSlots;

    // 📍 Info
    private String location;
    private String contractType;
    private String perks;
    private Boolean remoteFriendly;
    private Boolean visaSponsorship;

    // ⏰ Horario
    private Integer weeklyHours;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<String> workingDays;

    /** Referencia al puesto */
    private Long positionId;
    private String positionTitle; // Opcional para mostrar directamente el título del puesto
}
