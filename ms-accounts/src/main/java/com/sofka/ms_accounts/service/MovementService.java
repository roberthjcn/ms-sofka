package com.sofka.ms_accounts.service;

import com.sofka.ms_accounts.dto.MovementDTO;

import java.util.List;
import java.util.UUID;

public interface MovementService {
    MovementDTO createMovement(MovementDTO movementDTO);
    MovementDTO updateMovement(MovementDTO movementDTO);
    MovementDTO getMovementById(UUID movementId);
    List<MovementDTO> getMovementsByAccount(UUID accountId);
    List<MovementDTO> getAllMovements();
    void deleteMovement(UUID movementId);
}
