package com.pet.businessdomain.financeservice.entities;

import com.pet.businessdomain.financeservice.entities.enumentities.Enum;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "finance_accounts")
@Data
public class FinanceAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Enum.OwnerType ownerType; // Tipo de propietario (Character, Empresa, etc.)

    @Column(nullable = false)
    private Long ownerId; // ID del propietario (characterUid)

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    private BigDecimal savings = BigDecimal.ZERO;

    private BigDecimal debt = BigDecimal.ZERO;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    // Relación con Incomes
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IncomeEntity> incomes = new ArrayList<>();

    // Relación con Expenses
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpenseEntity> expenses = new ArrayList<>();

    // Relación con Transactions (opcional, recomendable para histórico)
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionEntity> transactions = new ArrayList<>();
}

