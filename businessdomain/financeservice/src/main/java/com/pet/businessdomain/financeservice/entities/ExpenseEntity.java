package com.pet.businessdomain.financeservice.entities;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "finance_expenses")
@Data
@NoArgsConstructor
public class ExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con la cuenta financiera
    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private FinanceAccountEntity account;

    @Column(nullable = false)
    private String concept; // Ej: Rent, Food, Transport, Internet, Education

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private EnumAll.ExpenseCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumAll.Frequency frequency; // DAILY, WEEKLY, MONTHLY, YEARLY

    @Column(nullable = false)
    private boolean essential; // Esencial o no

    private LocalDate startDate;
    private LocalDate endDate;

    // Enlace opcional a otros servicios o referencias externas
    private String externalRefType; // RENT, EDUCATION, SUBSCRIPTION
    private Long externalRefId;

    @Column(nullable = false)
    private boolean active = true;
}
