package myproject.microservice.accountservice.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "T_ACCOUNT")
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", unique = true)
    private String number;

    @Column(name = "name")
    private String owner;

    private BigDecimal balance;

    /**
     * Default constructor for JPA only.
     */
    protected Account() {
        balance = BigDecimal.ZERO;
    }

    public Account(String number, String owner) {
        this.number = number;
        this.owner = owner;
        balance = BigDecimal.ZERO;
    }

    public long getId() {
        return id;
    }

    protected void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    protected void setNumber(String accountNumber) {
        this.number = accountNumber;
    }

    public String getOwner() {
        return owner;
    }

    protected void setOwner(String owner) {
        this.owner = owner;
    }

    public BigDecimal getBalance() {
        return balance.setScale(2, RoundingMode.HALF_EVEN);
    }

//    public void withdraw(BigDecimal amount) {
//        balance.subtract(amount);
//    }
//
//    public void deposit(BigDecimal amount) {
//        balance.add(amount);
//    }

    @Override
    public String toString() {
        return String.format(
                "Account[id=%d, number='%s', name='%s', balance='%.2f']",
                id, number, owner, balance);
    }

}