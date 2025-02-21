package com.sofka.ms_accounts.mapper;
import com.sofka.ms_accounts.dto.AccountDTO;
import com.sofka.ms_accounts.entity.Account;

public class AccountMapper {
    public static AccountDTO toDTO(Account account) {
        return AccountDTO.builder()
                .accountId(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .type(account.getType())
                .initialBalance(account.getInitialBalance())
                .currentBalance(account.getCurrentBalance())
                .status(account.getStatus())
                .customerId(account.getCustomerId())
                .build();
    }

    public static Account toEntity(AccountDTO accountDTO) {
        return Account.builder()
                .accountId(accountDTO.getAccountId())
                .accountNumber(accountDTO.getAccountNumber())
                .type(accountDTO.getType())
                .initialBalance(accountDTO.getInitialBalance())
                .currentBalance(accountDTO.getCurrentBalance())
                .status(accountDTO.getStatus())
                .customerId(accountDTO.getCustomerId())
                .build();
    }
}
