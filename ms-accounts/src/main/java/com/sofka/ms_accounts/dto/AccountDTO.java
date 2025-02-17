package com.sofka.ms_accounts.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class AccountDTO {
    private UUID accountId;
    private String accountNumber;
    private String type;
    private BigDecimal initialBalance;
    private Boolean status;
    private UUID customerId;
}
