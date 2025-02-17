package com.sofka.ms_accounts.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class MovementDetailDTO {
    private LocalDateTime date;
    private String type;
    private BigDecimal value;
    private BigDecimal balance;
}
