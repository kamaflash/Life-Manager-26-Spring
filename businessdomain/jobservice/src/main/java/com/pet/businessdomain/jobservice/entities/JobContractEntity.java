package com.pet.businessdomain.jobservice.entities;

import com.pet.businessdomain.shareddto.enumentities.JobCategory;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "job_contracts")
@Data
public class JobContractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ========== DATOS DEL CONTRATO ==========
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
    private String characterDni;
    private Integer characterAge;
    private LocalDate characterBirthDate;

    // ========== DATOS DE LA EMPRESA ==========
    private Long companyId;
    private String companyName;
    private String companyTaxId;
    private String companyAddress;
    private String companyPhone;
    private String companyEmail;
    private String companyWebsite;

    // ========== DATOS DEL PUESTO ==========
    private Long positionId;
    private String positionTitle;
    private String positionLevel;
    private JobCategory positionCategory;
    private String positionDescription;

    // ========== DATOS DE LA OFERTA ECONÓMICA ==========
    private BigDecimal baseSalary;
    private BigDecimal monthlySalary;
    private BigDecimal extraPayments;
    private String salaryCurrency;
    private BigDecimal variableBonus;
    private String salaryPeriod;

    // ========== DATOS DEL MATCH SCORE ==========
    private Integer matchScore;
    private BigDecimal minSalaryRange;
    private BigDecimal maxSalaryRange;
    private String salaryCalculationNote;

    // ========== DATOS DE JORNADA Y HORARIO ==========
    private String contractType;
    private String workModality;
    private Integer weeklyHours;
    private LocalTime startTime;
    private LocalTime endTime;

    @ElementCollection
    @CollectionTable(name = "contract_working_days", joinColumns = @JoinColumn(name = "contract_id"))
    @Column(name = "working_day")
    private List<String> workingDays = new ArrayList<>();

    private String scheduleNote;

    // ========== DATOS DE VACACIONES Y BENEFICIOS ==========
    private Integer vacationDays;

    @ElementCollection
    @CollectionTable(name = "contract_benefits", joinColumns = @JoinColumn(name = "contract_id"))
    @Column(name = "benefit")
    private List<String> benefits = new ArrayList<>();

    private Boolean visaSponsorship;
    private String trainingPlan;

    // ========== CONDICIONES LABORALES ==========
    private LocalDate startDate;
    private LocalDate endDate;
    private String probationPeriod;
    private String terminationNotice;

    // ========== CLAUSULAS ADICIONALES ==========
    @Column(length = 2000)
    private String confidentialityClause;

    @Column(length = 2000)
    private String exclusivityClause;

    @Column(length = 2000)
    private String intellectualProperty;

    @ElementCollection
    @CollectionTable(name = "contract_special_conditions", joinColumns = @JoinColumn(name = "contract_id"))
    @Column(name = "condition", length = 1000)
    private List<String> specialConditions = new ArrayList<>();

    // ========== DATOS DE LA APLICACIÓN ==========
    private Long applicationId;
    private LocalDateTime interviewDate;
    private LocalDateTime contractGeneratedAt;

    // ========== TIMESTAMPS ==========
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime rejectedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        issuedAt = LocalDateTime.now();
        validUntil = LocalDateTime.now().plusDays(7);
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
