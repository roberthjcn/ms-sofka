package com.sofka.ms_accounts.service;

import com.sofka.ms_accounts.dto.AccountStatementDTO;
import com.sofka.ms_accounts.dto.MovementDetailDTO;
import com.sofka.ms_accounts.entity.Account;
import com.sofka.ms_accounts.entity.Movement;
import com.sofka.ms_accounts.repository.AccountRepository;
import com.sofka.ms_accounts.repository.MovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final AccountRepository accountRepository;
    private final MovementRepository movementRepository;

    public List<AccountStatementDTO> generateAccountStatement(LocalDate startDate, LocalDate endDate) {
        List<Account> accounts = accountRepository.findAll();

        return accounts.stream()
                .map(account -> {
                    List<Movement> movements = movementRepository.findByAccount_AccountIdAndDateBetween(
                            account.getAccountId(), startDate.atStartOfDay(), endDate.atTime(23, 59, 59));

                    List<MovementDetailDTO> movementDetails = movements.stream()
                            .map(movement -> MovementDetailDTO.builder()
                                    .date(movement.getDate())
                                    .type(movement.getType())
                                    .value(movement.getValue())
                                    .balance(movement.getBalance())
                                    .build())
                            .collect(Collectors.toList());

                    return AccountStatementDTO.builder()
                            .accountId(account.getAccountId())
                            .accountNumber(account.getAccountNumber())
                            .balance(account.getInitialBalance())
                            .movements(movementDetails)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
