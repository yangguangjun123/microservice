package myproject.microservice.webservice.web;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * DTO for Account entity
 */
@JsonRootName("Account")
public class AccountDTO {

    private Long id;
    private String number;
    private String owner;
    private BigDecimal balance;

    public AccountDTO() {
        balance = BigDecimal.ZERO;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String accountNumber) {
        this.number = accountNumber;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public BigDecimal getBalance() {
        return balance.setScale(2, RoundingMode.HALF_EVEN);
    }

    public void setBalance(BigDecimal value) {
        balance = value;
        balance.setScale(2, RoundingMode.HALF_EVEN);
    }

    @Override
    public String toString() {
        return String.format(
                "AccountDTO[id=%d, number='%s', name='%s', balance='%.2f']",
                id, number, owner, balance);
    }

}
