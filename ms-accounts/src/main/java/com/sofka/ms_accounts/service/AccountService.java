package com.sofka.ms_accounts.service;

import com.sofka.ms_accounts.dto.AccountDTO;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    AccountDTO createAccount(AccountDTO cuentaDTO);
    List<AccountDTO> getAccountsPerCustomer(UUID customerId);
    AccountDTO getAccountById(UUID id);
    List<AccountDTO> getAllAccounts();
    void deleteAccount(UUID id);
}
