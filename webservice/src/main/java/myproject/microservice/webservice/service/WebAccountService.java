package myproject.microservice.webservice.service;

//import myproject.microservice.accountservice.domain.Account;
//import myproject.microservice.accountservice.exception.AccountNotFoundException;
import myproject.microservice.webservice.web.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class WebAccountService {
    @Autowired        // NO LONGER auto-created by Spring Cloud (see below)
    @LoadBalanced     // Explicitly request the load-balanced template with Ribbon built-in
    private RestTemplate restTemplate;

    private String serviceUrl;

    private static final Logger logger = Logger.getLogger(WebAccountService.class.getName());

    public WebAccountService(String serviceUrl) {
        this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl : "http://" + serviceUrl;
    }

    public Optional<AccountDTO> getByNumber(String accountNumber) {
        Objects.requireNonNull(accountNumber, "account number is required");
        Optional<AccountDTO> account = Optional.of(restTemplate.getForObject(serviceUrl
                + "/api/account/{number}", AccountDTO.class, accountNumber));
        return account;
    }

    public List<AccountDTO> byOwnerContains(String name) {
        logger.info("byOwnerContains() invoked:  for " + name);
        AccountDTO[] accounts = null;

        try {
            accounts = restTemplate.getForObject(serviceUrl
                    + "/api/account/owner/{name}", AccountDTO[].class, name);
        } catch (HttpClientErrorException e) { // 404
            // Nothing found
        }

        if (accounts == null || accounts.length == 0)
            return null;
        else
            return Arrays.asList(accounts);
    }

}
