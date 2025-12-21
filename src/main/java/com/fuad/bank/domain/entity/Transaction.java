package com.fuad.bank.domain.entity;

import com.fuad.bank.domain.enums.TransactionStatus;
import com.fuad.bank.domain.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = true)
    private BigDecimal oldBalance;

    @Column(nullable = true)
    private BigDecimal newBalance;

    @Column(name = "old_to_balance", nullable = true)
    private BigDecimal oldToBalance;

    @Column(name = "new_to_balance", nullable = true)
    private BigDecimal newToBalance;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id", nullable = true)
    private Account fromAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id", nullable = true)
    private Account toAccount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status")
    private TransactionStatus transactionStatus;

    @Column(name = "error_message", length = 255)
    private String errorMessage;

    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }
}
