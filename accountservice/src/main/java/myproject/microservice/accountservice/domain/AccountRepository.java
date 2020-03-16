package myproject.microservice.accountservice.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountRepository extends CrudRepository<Account, Long> {

    Account findById(long id);

    List<Account> findByOwner(String name);

    Account findByNumber(String number);

    public List<Account> findByOwnerContainingIgnoreCase(String partialName);

    @Query("SELECT count(*) from Account")
    public int countAccounts();

}
