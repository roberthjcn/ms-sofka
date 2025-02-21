package com.sofka.ms_accounts.service;

import com.sofka.ms_accounts.dto.MovementDTO;
import com.sofka.ms_accounts.entity.Account;
import com.sofka.ms_accounts.entity.Movement;
import com.sofka.ms_accounts.enums.MovementType;
import com.sofka.ms_accounts.exception.AccountNotFoundException;
import com.sofka.ms_accounts.exception.InsufficientBalanceException;
import com.sofka.ms_accounts.exception.MovementNotFoundException;
import com.sofka.ms_accounts.mapper.MovementMapper;
import com.sofka.ms_accounts.repository.AccountRepository;
import com.sofka.ms_accounts.repository.MovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovementServiceImpl implements MovementService {
    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public MovementDTO createMovement(MovementDTO movementDTO) {
        Account account = validateAndGetAccount(movementDTO.getAccountId());
        BigDecimal newBalance = calculateNewBalance(account.getCurrentBalance(), movementDTO);
        validateBalance(newBalance);

        Movement movement = createAndSaveMovement(movementDTO, account, newBalance);

        updateAccountBalance(account, newBalance);

        return MovementMapper.toDTO(movement);
    }

    @Override
    @Transactional
    public MovementDTO updateMovement(MovementDTO movementDTO) {
        Movement existingMovement = movementRepository.findById(movementDTO.getMovementId())
                .orElseThrow(() -> new MovementNotFoundException("Movimiento no encontrado con ID: " + movementDTO.getMovementId()));
        Account account = validateAndGetAccount(existingMovement.getAccount().getAccountId());

        if (movementDTO.getType() != null) {
            existingMovement.setType(movementDTO.getType().getValue());
        }
        if (movementDTO.getValue() != null) {
            existingMovement.setValue(movementDTO.getValue());
        }
        if (movementDTO.getDate() != null) {
            existingMovement.setDate(movementDTO.getDate());
        }

        if (movementDTO.getValue() != null && !movementDTO.getValue().equals(existingMovement.getValue())) {
            BigDecimal newBalance = calculateNewBalance(account.getCurrentBalance(), movementDTO);
            validateBalance(newBalance);
            existingMovement.setBalance(newBalance);
            updateAccountBalance(account, newBalance);
        }

        Movement updatedMovement = movementRepository.save(existingMovement);

        return MovementMapper.toDTO(updatedMovement);
    }

    @Override
    public MovementDTO getMovementById(UUID movementId) {
        Movement movement = movementRepository.findById(movementId)
                .orElseThrow(() -> new MovementNotFoundException("Movimiento no encontrado con ID: " + movementId));
        return MovementMapper.toDTO(movement);
    }

    @Override
    public List<MovementDTO> getMovementsByAccount(UUID accountId) {
        return movementRepository.findByAccount_AccountId(accountId)
                .stream()
                .map(MovementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovementDTO> getAllMovements() {
        return movementRepository.findAll()
                .stream()
                .map(MovementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteMovement(UUID movementId) {
        Movement movement = movementRepository.findById(movementId)
                .orElseThrow(() -> new MovementNotFoundException("Movimiento no encontrado con ID: " + movementId));

        Account account = movement.getAccount();

        BigDecimal newBalance = revertBalance(account.getCurrentBalance(), movement);
        validateBalance(newBalance);

        account.setCurrentBalance(newBalance);
        accountRepository.save(account);

        movementRepository.delete(movement);
    }


    private Account validateAndGetAccount(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("La cuenta no existe con ID: " + accountId));
    }

    private BigDecimal calculateNewBalance(BigDecimal currentBalance, MovementDTO movementDTO) {
        MovementType movementType = movementDTO.getType();

        switch (movementType) {
            case DEPOSITO:
                return currentBalance.add(movementDTO.getValue());
            case RETIRO:
                return currentBalance.subtract(movementDTO.getValue());
            default:
                throw new IllegalArgumentException("Tipo de movimiento no válido: " + movementType);
        }
    }

    private void validateBalance(BigDecimal newBalance) {
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException("Saldo no disponible");
        }
    }

    private Movement createAndSaveMovement(MovementDTO movementDTO, Account account, BigDecimal newBalance) {
        Movement movement = new Movement();
        movement.setDate(LocalDateTime.now());
        movement.setType(movementDTO.getType().getValue());
        movement.setValue(movementDTO.getValue());
        movement.setBalance(newBalance);
        movement.setAccount(account);

        return movementRepository.save(movement);
    }

    private void updateAccountBalance(Account account, BigDecimal newBalance) {
        account.setCurrentBalance(newBalance);
        accountRepository.save(account);
    }

    private BigDecimal revertBalance(BigDecimal currentBalance, Movement movement) {
        MovementType movementType = MovementType.fromValue(movement.getType());

        switch (movementType) {
            case DEPOSITO:
                return currentBalance.subtract(movement.getValue());
            case RETIRO:
                return currentBalance.add(movement.getValue());
            default:
                throw new IllegalArgumentException("Tipo de movimiento no válido: " + movementType);
        }
    }
}
