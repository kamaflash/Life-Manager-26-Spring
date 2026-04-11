package com.pet.businessdomain.shareddto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobContractDTO {

    // ========== DATOS DEL CONTRATO ==========
    private Long contractId;
    private String contractNumber;
    private LocalDateTime issuedAt;
    private LocalDateTime validUntil;
    private String status; // PENDING, ACCEPTED, REJECTED, SIGNED
    private String contractUrl;

    // ========== DATOS DEL EMPLEADO (PERSONAJE) ==========
    private Long characterId;
    private String characterName;
    private String characterEmail;
    private String characterPhone;
    private String characterAddress;
    private String characterDni; // o NIE
    private Integer characterAge;
    private LocalDate characterBirthDate;

    // ========== DATOS DE LA EMPRESA ==========
    private Long companyId;
    private String companyName;
    private String companyTaxId; // CIF/NIF
    private String companyAddress;
    private String companyPhone;
    private String companyEmail;
    private String companyWebsite;

    // ========== DATOS DEL PUESTO ==========
    private Long positionId;
    private String positionTitle;
    private String positionLevel; // JUNIOR, SEMI_SENIOR, SENIOR
    private String positionCategory;
    private String positionDescription;

    // ========== DATOS DE LA OFERTA ECONÓMICA ==========
    private BigDecimal baseSalary;      // Salario base anual
    private BigDecimal monthlySalary;   // Salario mensual (12 pagas)
    private BigDecimal extraPayments;   // Pagas extra (si las hay)
    private String salaryCurrency;      // EUR, USD, etc.
    private BigDecimal variableBonus;   // Bonus variable (opcional)
    private String salaryPeriod;        // ANNUAL, MONTHLY, HOURLY

    // ========== DATOS DEL MATCH SCORE ==========
    private Integer matchScore;
    private BigDecimal minSalaryRange;
    private BigDecimal maxSalaryRange;
    private String salaryCalculationNote; // Explicación de cómo se calculó

    // ========== DATOS DE JORNADA Y HORARIO ==========
    private String contractType;         // FULL_TIME, PART_TIME, INTERNSHIP
    private String workModality;         // ONSITE, REMOTE, HYBRID
    private Integer weeklyHours;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<String> workingDays;    // LUNES, MARTES, MIÉRCOLES, etc.
    private String scheduleNote;         // Notas adicionales sobre horario

    // ========== DATOS DE VACACIONES Y BENEFICIOS ==========
    private Integer vacationDays;        // Días de vacaciones al año
    private List<String> benefits;       // Beneficios adicionales
    private Boolean visaSponsorship;
    private String trainingPlan;         // Plan de formación

    // ========== CONDICIONES LABORALES ==========
    private LocalDate startDate;
    private LocalDate endDate;           // Para contratos temporales (opcional)
    private String probationPeriod;      // Periodo de prueba (ej: "3 meses")
    private String terminationNotice;    // Preaviso de terminación

    // ========== CLAUSULAS ADICIONALES ==========
    private String confidentialityClause;
    private String exclusivityClause;
    private String intellectualProperty;
    private List<String> specialConditions;

    // ========== DATOS DE LA APLICACIÓN ==========
    private Long applicationId;
    private LocalDateTime interviewDate;
    private LocalDateTime contractGeneratedAt;
}