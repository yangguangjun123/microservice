package myproject.microservice.accountservice.controller;

import myproject.microservice.accountservice.domain.Account;
import myproject.microservice.accountservice.domain.AccountRepository;
import myproject.microservice.accountservice.exception.AccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private AccountRepository accountRepository;

    private Logger logger = Logger.getLogger(AccountController.class.getName());

    @Autowired
    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;

        logger.info("AccountRepository says system has " + accountRepository.count()+ " accounts");
    }

    @RequestMapping("/{accountNumber}")
    public Account byNumber(@PathVariable("accountNumber") String accountNumber) {
        logger.info("accounts-service byNumber() invoked: " + accountNumber);
        Optional<Account> account = Optional.ofNullable(accountRepository.findByNumber(accountNumber));
        logger.info("accounts-service byNumber() found: " + account);
        return account.orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

    @RequestMapping("/owner/{name}")
    public List<Account> byOwner(@PathVariable("name") String partialName) {
        logger.info("accounts-service byOwner() invoked: " +
                accountRepository.getClass().getName() + " for " + partialName);
        Optional<List<Account>> accounts = Optional.of(accountRepository.findByOwnerContainingIgnoreCase(partialName));
        if(accounts.isEmpty() || accounts.get().isEmpty()) {
            throw new AccountNotFoundException(partialName);
        } else {
            return accounts.get();
        }

    }


}
