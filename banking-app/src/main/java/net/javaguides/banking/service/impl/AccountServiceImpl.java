package net.javaguides.banking.service.impl;

import net.javaguides.banking.dto.AccountDto;
import net.javaguides.banking.entity.Account;
import net.javaguides.banking.mapper.AccountMapper;
import net.javaguides.banking.repository.AccountRepository;
import net.javaguides.banking.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository)
    {
        this.accountRepository=accountRepository;
    }


    @Override
    public AccountDto createAccount(AccountDto accountDto) {

        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedaccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedaccount);
    }

    @Override
    public AccountDto getAccountById(Long id) {

       Account account = accountRepository.findById(id).
                    orElseThrow(()->new RuntimeException("Account Doesn't exist"));
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto deposit(Long id, double amount) {
      Account account =  accountRepository.findById(id).
                          orElseThrow(()->new RuntimeException("Account does not exist"));

      double total = account.getBalance()+amount;
      account.setBalance(total);
      Account savedAccount = accountRepository.save(account);
      return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {

     Account account  =   accountRepository
                .findById(id)
                .orElseThrow(()->new RuntimeException("Account does not exist"));

     if(amount>account.getBalance())
     {
         throw new RuntimeException("Not have sufficient amount");
     }

     double total = account.getBalance()-amount;
     account.setBalance(total);
     Account savedAccount = accountRepository.save(account);
     return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public List<AccountDto> getAllAccounts() {

        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map((account) -> AccountMapper.mapToAccountDto(account))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAccount(Long id) {
        Account account  =   accountRepository
                .findById(id)
                .orElseThrow(()->new RuntimeException("Account does not exist"));

        accountRepository.deleteById(id);
    }
}
