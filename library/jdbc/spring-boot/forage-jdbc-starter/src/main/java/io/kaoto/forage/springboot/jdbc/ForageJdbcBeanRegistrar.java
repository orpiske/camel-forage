package io.kaoto.forage.springboot.jdbc;

import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import io.kaoto.forage.core.util.config.ConfigHelper;
import io.kaoto.forage.jdbc.common.JdbcModuleDescriptor;
import io.kaoto.forage.springboot.common.ForageSpringBootModuleAdapter;
import io.kaoto.forage.springboot.common.SpringPropertyHelper;

/**
 * Registers Forage JDBC DataSource bean definitions during Spring's configuration class processing phase.
 *
 * <p>Unlike {@link ForageSpringBootModuleAdapter} (a {@code BeanDefinitionRegistryPostProcessor} that runs
 * after condition evaluation), this registrar runs during the same phase as {@code @ConditionalOnMissingBean}
 * evaluation. This ensures that Spring Boot's {@code DataSourceAutoConfiguration} sees the Forage-registered
 * DataSource beans and does not attempt to create its own HikariCP DataSource.
 *
 * <p>Used via {@code @Import(ForageJdbcBeanRegistrar.class)} on {@link ForageDataSourceAutoConfiguration}.
 *
 * @since 1.1
 */
class ForageJdbcBeanRegistrar implements ImportBeanDefinitionRegistrar, org.springframework.context.EnvironmentAware {

    private static final Logger LOG = LoggerFactory.getLogger(ForageJdbcBeanRegistrar.class);

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        JdbcModuleDescriptor descriptor = new JdbcModuleDescriptor();
        Set<String> prefixes = discoverPrefixes(descriptor);

        if (prefixes.isEmpty()) {
            LOG.debug("No Forage JDBC prefixed configurations found");
            return;
        }

        LOG.info("Discovered Forage JDBC configuration prefixes: {}", prefixes);
        boolean isFirst = true;
        for (String name : prefixes.stream().sorted().toList()) {
            if (!registry.containsBeanDefinition(name)) {
                registerBean(registry, descriptor, name);
                if (isFirst) {
                    String defaultName = descriptor.defaultBeanName();
                    if (!registry.containsBeanDefinition(defaultName)) {
                        registerBean(registry, descriptor, defaultName, name);
                        LOG.info("Registered default JDBC bean '{}' using prefix: {}", defaultName, name);
                    }
                    isFirst = false;
                }
            }
        }
    }

    private void registerBean(BeanDefinitionRegistry registry, JdbcModuleDescriptor descriptor, String beanName) {
        registerBean(registry, descriptor, beanName, beanName);
    }

    private void registerBean(
            BeanDefinitionRegistry registry, JdbcModuleDescriptor descriptor, String beanName, String prefix) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(descriptor.primaryBeanClass());
        beanDefinition.setInstanceSupplier(
                () -> new ForageSpringBootModuleAdapter<>(descriptor, environment).createBean(prefix));
        registry.registerBeanDefinition(beanName, beanDefinition);
        LOG.info("Registered JDBC bean definition: {}", beanName);
    }

    private Set<String> discoverPrefixes(JdbcModuleDescriptor descriptor) {
        return SpringPropertyHelper.discoverPrefixes(
                environment, ConfigHelper.getNamedPropertyRegexp(descriptor.modulePrefix()));
    }
}
