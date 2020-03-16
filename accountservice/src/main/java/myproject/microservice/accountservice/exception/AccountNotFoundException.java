package myproject.microservice.accountservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public AccountNotFoundException(String accountNumber) {
        super("No such account: " + accountNumber);
    }
}
