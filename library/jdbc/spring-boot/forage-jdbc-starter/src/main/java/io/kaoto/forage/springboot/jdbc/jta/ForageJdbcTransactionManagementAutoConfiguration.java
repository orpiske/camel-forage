package io.kaoto.forage.springboot.jdbc.jta;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ConditionalOnProperty(value = "forage.jdbc.transaction.enabled", havingValue = "true")
@EnableTransactionManagement
public class ForageJdbcTransactionManagementAutoConfiguration
        extends io.kaoto.forage.springboot.common.jta.ForageTransactionManagementAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ForageJdbcTransactionManagementAutoConfiguration.class);

    @PostConstruct
    public void init() {
        log.info("ForageJdbcTransactionManagementAutoConfiguration initialized - transaction management enabled");
    }
}
