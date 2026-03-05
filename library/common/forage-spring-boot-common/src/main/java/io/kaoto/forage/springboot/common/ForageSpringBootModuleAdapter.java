package io.kaoto.forage.springboot.common;

import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.env.Environment;
import io.kaoto.forage.core.common.AuxiliaryBeanDescriptor;
import io.kaoto.forage.core.common.BeanProvider;
import io.kaoto.forage.core.common.ForageModuleDescriptor;
import io.kaoto.forage.core.common.ServiceLoaderHelper;
import io.kaoto.forage.core.util.config.Config;
import io.kaoto.forage.core.util.config.ConfigHelper;

/**
 * Generic Spring Boot adapter that consumes any {@link ForageModuleDescriptor} to dynamically
 * register beans via {@link BeanDefinitionRegistryPostProcessor}.
 *
 * <p>This replaces per-module post-processors (e.g., {@code ForageJdbcBeanDefinitionRegistryPostProcessor},
 * {@code ForageJmsBeanDefinitionRegistryPostProcessor}) with a single reusable class.
 * Adding a new Forage module only requires writing a descriptor — no new post-processor needed.
 *
 * @param <C> the module's configuration type
 * @param <P> the module's provider type
 * @since 1.1
 */
public class ForageSpringBootModuleAdapter<C extends Config, P extends BeanProvider<?>>
        implements BeanDefinitionRegistryPostProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(ForageSpringBootModuleAdapter.class);

    private final ForageModuleDescriptor<C, P> descriptor;
    private final Environment environment;

    public ForageSpringBootModuleAdapter(ForageModuleDescriptor<C, P> descriptor, Environment environment) {
        this.descriptor = descriptor;
        this.environment = environment;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        Set<String> prefixes = discoverPrefixes();
        if (!prefixes.isEmpty()) {
            LOG.info("Discovered Forage {} configuration prefixes: {}", descriptor.modulePrefix(), prefixes);
            registerBeans(registry, prefixes);
        } else if (hasDefaultProperties()) {
            // Single (non-prefixed) configuration: primary bean is created by the
            // auto-configuration's @Bean method, but auxiliary beans (aggregation repo,
            // idempotent repo) still need to be registered here.
            LOG.info(
                    "Discovered default Forage {} configuration, registering auxiliary beans",
                    descriptor.modulePrefix());
            registerAuxiliaryBeans(registry, null);
        } else {
            LOG.debug("No Forage {} configuration found", descriptor.modulePrefix());
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // No-op — all work is done in postProcessBeanDefinitionRegistry
    }

    private boolean hasDefaultProperties() {
        return SpringPropertyHelper.hasProperties(
                environment, ConfigHelper.getDefaultPropertyRegexp(descriptor.modulePrefix()));
    }

    private Set<String> discoverPrefixes() {
        return SpringPropertyHelper.discoverPrefixes(
                environment, ConfigHelper.getNamedPropertyRegexp(descriptor.modulePrefix()));
    }

    private void registerBeans(BeanDefinitionRegistry registry, Set<String> prefixes) {
        LOG.info("Registering Forage {} beans for prefixes: {}", descriptor.modulePrefix(), prefixes);
        boolean isFirst = true;
        for (String name : prefixes) {
            if (!registry.containsBeanDefinition(name)) {
                registerPrimaryBean(registry, name, isFirst);
            } else {
                LOG.debug("Bean '{}' already defined, skipping primary registration", name);
                // Still register auxiliary beans even if the primary bean was already
                // registered (e.g., by ForageJdbcBeanRegistrar during config processing)
                if (isFirst) {
                    registerAuxiliaryBeans(registry, name);
                }
            }
            isFirst = false;
        }
    }

    private void registerPrimaryBean(BeanDefinitionRegistry registry, String name, boolean isFirst) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(descriptor.primaryBeanClass());
        beanDefinition.setInstanceSupplier(() -> createPrimaryBean(name));
        registry.registerBeanDefinition(name, beanDefinition);
        LOG.info("Registered {} bean definition: {}", descriptor.modulePrefix(), name);

        String defaultName = descriptor.defaultBeanName();
        if (isFirst && !registry.containsBeanDefinition(defaultName)) {
            GenericBeanDefinition defaultDef = new GenericBeanDefinition();
            defaultDef.setBeanClass(descriptor.primaryBeanClass());
            defaultDef.setInstanceSupplier(() -> createPrimaryBean(name));
            registry.registerBeanDefinition(defaultName, defaultDef);
            LOG.info("Registered default {} bean definition using: {}", descriptor.modulePrefix(), name);

            registerAuxiliaryBeans(registry, name);
        }
    }

    private void registerAuxiliaryBeans(BeanDefinitionRegistry registry, String prefix) {
        List<AuxiliaryBeanDescriptor> auxiliaryBeans = descriptor.auxiliaryBeans(prefix);
        for (AuxiliaryBeanDescriptor aux : auxiliaryBeans) {
            if (!registry.containsBeanDefinition(aux.name())) {
                GenericBeanDefinition beanDef = new GenericBeanDefinition();
                beanDef.setBeanClass(aux.type());
                beanDef.setInstanceSupplier(aux.factory());
                registry.registerBeanDefinition(aux.name(), beanDef);
                LOG.info("Registered auxiliary bean definition: {}", aux.name());
            }
        }
    }

    /**
     * Creates the primary bean for the given prefix/name using ServiceLoader discovery.
     * Exposed for use by {@code ImportBeanDefinitionRegistrar} implementations that need
     * to delegate bean creation to the adapter.
     *
     * @param name the configuration prefix or bean name
     * @return the created bean, or null if no provider is found
     */
    public Object createBean(String name) {
        return createPrimaryBean(name);
    }

    private Object createPrimaryBean(String name) {
        C config = descriptor.createConfig(name);
        String providerClassName = descriptor.resolveProviderClassName(config);
        List<ServiceLoader.Provider<P>> providers =
                ServiceLoader.load(descriptor.providerClass()).stream().toList();

        ServiceLoader.Provider<P> provider;
        if (providers.size() == 1) {
            provider = providers.get(0);
        } else {
            provider = ServiceLoaderHelper.findProviderByClassName(providers, providerClassName);
        }

        if (provider == null) {
            LOG.error("No {} provider found for class: {}", descriptor.modulePrefix(), providerClassName);
            return null;
        }

        return provider.get().create(name);
    }
}
