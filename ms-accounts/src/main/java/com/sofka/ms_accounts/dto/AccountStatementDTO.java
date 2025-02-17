package com.sofka.ms_accounts.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class AccountStatementDTO {
    private UUID accountId;
    private String accountNumber;
    private BigDecimal balance;
    private List<MovementDetailDTO> movements;
}
