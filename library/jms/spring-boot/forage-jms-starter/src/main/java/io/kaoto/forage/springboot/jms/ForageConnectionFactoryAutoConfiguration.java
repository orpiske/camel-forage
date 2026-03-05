package io.kaoto.forage.springboot.jms;

import jakarta.jms.ConnectionFactory;

import java.util.List;
import java.util.ServiceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import io.kaoto.forage.core.annotations.FactoryType;
import io.kaoto.forage.core.annotations.FactoryVariant;
import io.kaoto.forage.core.annotations.ForageFactory;
import io.kaoto.forage.core.jms.ConnectionFactoryProvider;
import io.kaoto.forage.jms.common.ConnectionFactoryConfig;
import io.kaoto.forage.jms.common.JmsModuleDescriptor;
import io.kaoto.forage.springboot.common.ForageSpringBootModuleAdapter;

/**
 * Auto-configuration for Forage JMS ConnectionFactory creation using ServiceLoader discovery.
 * Automatically creates ConnectionFactory beans from JMS configuration properties,
 * supporting both single and multi-instance (prefixed) configurations.
 *
 * <p>Named/prefixed connection factories (e.g., {@code forage.mq1.jms.url}) are registered
 * dynamically by {@link ForageSpringBootModuleAdapter} using the {@link JmsModuleDescriptor}.
 */
@ForageFactory(
        value = "JMS Connection (Spring Boot)",
        components = {"camel-jms"},
        description = "Auto-configured JMS ConnectionFactory for Spring Boot with transaction management",
        type = FactoryType.CONNECTION_FACTORY,
        autowired = true,
        configClass = ConnectionFactoryConfig.class,
        variant = FactoryVariant.SPRING_BOOT)
@Configuration
public class ForageConnectionFactoryAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ForageConnectionFactoryAutoConfiguration.class);

    /**
     * Transaction management configuration that enables Spring transaction support
     * when JMS transactions are configured.
     */
    @Configuration
    @ConditionalOnProperty(value = "forage.jms.transaction.enabled", havingValue = "true")
    @EnableTransactionManagement
    static class ForageTransactionManagement {

        @jakarta.annotation.PostConstruct
        public void init() {
            log.info("ForageTransactionManagement configuration enabled");
        }
    }

    /**
     * Registers the generic module adapter that discovers prefixed ConnectionFactory
     * configurations and registers them as proper bean definitions using the
     * {@link JmsModuleDescriptor}.
     */
    @Bean
    static ForageSpringBootModuleAdapter<ConnectionFactoryConfig, ConnectionFactoryProvider> forageJmsModuleAdapter(
            Environment environment) {
        return new ForageSpringBootModuleAdapter<>(new JmsModuleDescriptor(), environment);
    }

    /**
     * Fallback ConnectionFactory bean created when exactly one ConnectionFactoryProvider
     * is on the classpath and no named/prefixed configurations are found.
     */
    @Bean("jmsConnectionFactory")
    @ConditionalOnMissingBean(name = "jmsConnectionFactory")
    public ConnectionFactory forageDefaultConnectionFactory() {
        List<ServiceLoader.Provider<ConnectionFactoryProvider>> providers =
                ServiceLoader.load(ConnectionFactoryProvider.class).stream().toList();
        if (providers.size() == 1) {
            log.info(
                    "Creating default ConnectionFactory using single provider: {}",
                    providers.get(0).type().getName());
            ConnectionFactory connectionFactory = providers.get(0).get().create(null);
            log.info("Registered default ConnectionFactory bean");
            return connectionFactory;
        }
        throw new IllegalStateException(
                "Expected exactly one ConnectionFactoryProvider on the classpath, but found " + providers.size());
    }
}
