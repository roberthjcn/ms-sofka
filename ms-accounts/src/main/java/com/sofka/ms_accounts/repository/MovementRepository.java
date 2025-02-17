package com.sofka.ms_accounts.repository;

import com.sofka.ms_accounts.entity.Account;
import com.sofka.ms_accounts.entity.Movement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MovementRepository extends JpaRepository<Movement, UUID> {
    List<Movement> findByAccount_AccountId(UUID numeroCuenta);
    List<Movement> findByAccount_AccountIdAndDateBetween(UUID accountId, LocalDateTime startDate, LocalDateTime endDate);
}
