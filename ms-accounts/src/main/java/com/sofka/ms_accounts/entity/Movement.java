package com.sofka.ms_accounts.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "movement")
public class Movement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID movementId;

    private LocalDateTime date;
    private String type;
    private BigDecimal value;
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "accountId")
    private Account account;

}
