package myproject.microservice.accountservice;

import myproject.microservice.accountservice.domain.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;

@EnableAutoConfiguration
@EnableEurekaClient
@Import(AccountWebApplication.class)
public class AccountServer {
    @Autowired
    private AccountRepository accountRepository;

    public static void main(String[] args) {
        // Will configure using accounts-server.yml
        System.setProperty("spring.config.name", "account-server");

        SpringApplication.run(AccountServer.class, args);

    }
}
