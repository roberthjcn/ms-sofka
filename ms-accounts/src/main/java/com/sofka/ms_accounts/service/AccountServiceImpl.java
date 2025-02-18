package com.sofka.ms_accounts.service;

import com.sofka.ms_accounts.dto.AccountDTO;
import com.sofka.ms_accounts.entity.Account;
import com.sofka.ms_accounts.exception.AccountNotFoundException;
import com.sofka.ms_accounts.exception.DuplicateAccountException;
import com.sofka.ms_accounts.mapper.AccountMapper;
import com.sofka.ms_accounts.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final CustomerValidationPublisher validationPublisher;

    @Override
    public List<AccountDTO> getAccountsPerCustomer(UUID customerId) {
        return accountRepository.findByCustomerId(customerId)
                .stream()
                .map(AccountMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDTO getAccountById(UUID id) {
        return accountRepository.findById(id)
                .map(AccountMapper::toDTO)
                .orElseThrow(() -> new AccountNotFoundException("Cuenta no encontrada con ID: " + id));
    }

    @Override
    @Transactional
    public AccountDTO createAccount(AccountDTO accountDTO) {
        if (accountRepository.existsByAccountNumber(accountDTO.getAccountNumber())) {
            throw new DuplicateAccountException("Ya existe una cuenta con el número: " + accountDTO.getAccountNumber());
        }

        validationPublisher.validateCustomer(accountDTO.getCustomerId());

        Account account = AccountMapper.toEntity(accountDTO);
        account = accountRepository.save(account);

        return AccountMapper.toDTO(account);
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(AccountMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAccount(UUID id) {
        accountRepository.deleteById(id);
    }
}
