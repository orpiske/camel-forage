package io.kaoto.forage.policy.factory;

import java.util.Optional;
import io.kaoto.forage.core.util.config.Config;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigStore;

/**
 * Configuration class for the route policy factory.
 *
 * <p>This class provides access to the policy configuration for each route.
 * The configuration follows the pattern:
 * {@code camel.forage.route.policy.<routeId>.name}
 *
 * @see Config
 * @see RoutePolicyFactoryConfigEntries
 * @since 1.0
 */
public class RoutePolicyFactoryConfig implements Config {

    private final String prefix;

    /**
     * Creates a new RoutePolicyFactoryConfig with no prefix.
     */
    public RoutePolicyFactoryConfig() {
        this(null);
    }

    /**
     * Creates a new RoutePolicyFactoryConfig with the given prefix.
     *
     * @param prefix optional prefix for named configurations
     */
    public RoutePolicyFactoryConfig(String prefix) {
        this.prefix = prefix;

        RoutePolicyFactoryConfigEntries.register(prefix);
        ConfigStore.getInstance().load(RoutePolicyFactoryConfig.class, this, this::register);
        RoutePolicyFactoryConfigEntries.loadOverrides(prefix);
    }

    @Override
    public String name() {
        return "forage-policy-factory";
    }

    @Override
    public void register(String name, String value) {
        Optional<ConfigModule> config = RoutePolicyFactoryConfigEntries.find(prefix, name);
        config.ifPresent(module -> ConfigStore.getInstance().set(module, value));
    }

    /**
     * Returns whether the route policy factory is enabled.
     *
     * <p>When disabled, no route policies will be created by this factory.
     * Default is true (enabled).
     *
     * <p>Configuration sources (in order of precedence):
     * <ol>
     *   <li>Environment variable: CAMEL_FORAGE_ROUTE_POLICY_ENABLED</li>
     *   <li>System property: camel.forage.route.policy.enabled</li>
     *   <li>Properties file: camel.forage.route.policy.enabled</li>
     *   <li>Default: true</li>
     * </ol>
     *
     * @return true if the factory is enabled, false otherwise
     */
    public boolean isEnabled() {
        // Load fresh from configuration sources (env vars, system properties, etc.)
        ConfigStore.getInstance().load(RoutePolicyFactoryConfigEntries.ENABLED);

        return ConfigStore.getInstance()
                .get(RoutePolicyFactoryConfigEntries.ENABLED)
                .map(Boolean::parseBoolean)
                .orElse(true);
    }

    /**
     * Returns the policy names configured for a specific route.
     *
     * <p>The policy names are comma-separated in the configuration.
     *
     * @param routeId the route ID
     * @return an Optional containing the policy names, or empty if not configured
     */
    public Optional<String> getPolicyNames(String routeId) {
        ConfigModule module = RoutePolicyFactoryConfigEntries.policyNameModule(routeId);
        ConfigStore.getInstance().load(module);
        return ConfigStore.getInstance().get(module);
    }
}
