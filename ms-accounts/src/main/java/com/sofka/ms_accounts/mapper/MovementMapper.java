package com.sofka.ms_accounts.mapper;

import com.sofka.ms_accounts.dto.MovementDTO;
import com.sofka.ms_accounts.entity.Account;
import com.sofka.ms_accounts.entity.Movement;
import com.sofka.ms_accounts.enums.MovementType;

public class MovementMapper {
    public static MovementDTO toDTO(Movement movement) {
        return MovementDTO.builder()
                .movementId(movement.getMovementId())
                .date(movement.getDate())
                .type(MovementType.fromValue(movement.getType()))
                .value(movement.getValue())
                .balance(movement.getBalance())
                .accountId(movement.getAccount().getAccountId())
                .build();
    }

    public static Movement toEntity(MovementDTO movementDTO) {
        Movement movement = new Movement();
        movement.setMovementId(movementDTO.getMovementId());
        movement.setDate(movementDTO.getDate());
        movement.setType(movementDTO.getType().getValue());
        movement.setValue(movementDTO.getValue());
        movement.setBalance(movementDTO.getBalance());

        Account account = new Account();
        account.setAccountId(movementDTO.getAccountId());
        movement.setAccount(account);

        return movement;
    }
}
