package com.sofka.ms_accounts.dto;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.sofka.ms_accounts.enums.MovementType;
import lombok.Builder;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovementDTO {
    private UUID movementId;
    private LocalDateTime date;
    private MovementType type;
    private BigDecimal value;
    private BigDecimal balance;
    private UUID accountId;
}