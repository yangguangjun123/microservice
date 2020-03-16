package myproject.microservice.accountservice;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

@Configuration
@ComponentScan
@EntityScan("myproject.microservice.accountservice.domain")
@EnableJpaRepositories("myproject.microservice.accountservice.domain")
@PropertySource("classpath:db-config.properties")
public class AccountWebApplication {
    private static final Logger logger = Logger.getLogger(AccountWebApplication.class.getName());

    @Bean
    public DataSource dataSource() {
        logger.info("dataSource() invoked");

        // Create an in-memory H2 relational database containing some demo accounts.
        DataSource dataSource = (new EmbeddedDatabaseBuilder()).addScript("classpath:testdb/schema.sql")
                .addScript("classpath:testdb/data.sql").build();

        logger.info("dataSource = " + dataSource);

        // Sanity check
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Map<String, Object>> accounts = jdbcTemplate.queryForList("SELECT number FROM T_ACCOUNT");
        logger.info("System has " + accounts.size() + " accounts");

        // Populate with random balances
        Random rand = new Random();

        for (Map<String, Object> item : accounts) {
            String number = (String) item.get("number");
            BigDecimal balance = new BigDecimal(rand.nextInt(10000000) / 100.0)
                                                        .setScale(2, RoundingMode.HALF_UP);
            jdbcTemplate.update("UPDATE T_ACCOUNT SET balance = ? WHERE number = ?", balance, number);
        }

        return dataSource;
    }
}
