package myproject.microservice.webservice;

import myproject.microservice.webservice.service.WebAccountService;
import myproject.microservice.webservice.web.WebAccountController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(useDefaultFilters=false)  // Disable component scanner
public class WebServer {
    // Case insensitive: could also use: http://account-service
    public static final String ACCOUNTS_SERVICE_URL = "http://ACCOUNT-SERVICE";

    public static void main(String[] args) {
        // Will configure using web-server.yml
        System.setProperty("spring.config.name", "web-server");
        SpringApplication.run(WebServer.class, args);
    }

    @LoadBalanced    // Make sure to create the load-balanced template
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebAccountService webAccountService() {
        return new WebAccountService(ACCOUNTS_SERVICE_URL);
    }

//    @Bean
//    public String serviceUrl() {
//        return ACCOUNTS_SERVICE_URL;
//    }

    @Bean
    public WebAccountController webAccountController() {
        return new WebAccountController(webAccountService());  // plug in account-service
    }
}
