package com.pet.businessdomain.financeservice.entities;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "finance_incomes")
@Data
@NoArgsConstructor
public class IncomeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String source; // Ej: Job, Scholarship, FamilyHelp

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumAll.Frequency frequency; // DAILY, WEEKLY, MONTHLY, YEARLY

    @Column(nullable = false)
    private boolean active = true;
    @Enumerated(EnumType.STRING)
    private EnumAll.ExpenseCategory category;
    // Enlace opcional a servicios externos
    private String externalRefType; // JOB, EDUCATION, etc.
    private Long externalRefId;

    // Relación con la cuenta financiera
    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private FinanceAccountEntity account;
}
