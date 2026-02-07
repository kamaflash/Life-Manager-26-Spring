package com.pet.businessdomain.financeservice.entities;

import com.pet.businessdomain.financeservice.entities.enumentities.Enum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "finance_transactions")
@Data
@NoArgsConstructor
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación obligatoria con la cuenta financiera
    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private FinanceAccountEntity account;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Enum.TransactionType type; // INCOME, EXPENSE, TRANSFER

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime executedAt;
}
