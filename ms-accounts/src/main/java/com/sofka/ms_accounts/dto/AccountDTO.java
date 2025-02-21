package com.sofka.ms_accounts.dto;

import com.sofka.ms_accounts.enums.AccountType;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class AccountDTO {
    private UUID accountId;
    private String accountNumber;
    private AccountType type;
    private BigDecimal initialBalance;
    private BigDecimal currentBalance;
    private Boolean status;
    private UUID customerId;
}
