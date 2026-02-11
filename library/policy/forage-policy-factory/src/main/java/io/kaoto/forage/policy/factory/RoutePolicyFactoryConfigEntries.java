package io.kaoto.forage.policy.factory;

import io.kaoto.forage.core.util.config.ConfigEntries;
import io.kaoto.forage.core.util.config.ConfigEntry;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigTag;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Configuration entries for the route policy factory.
 *
 * <p>This class defines the configuration modules for the factory itself,
 * primarily the policy name assignment for each route. The configuration
 * follows the pattern: {@code camel.forage.route.policy.<routeId>.name}
 *
 * @since 1.0
 */
public final class RoutePolicyFactoryConfigEntries extends ConfigEntries {

    /**
     * The base configuration prefix for route policy configuration.
     */
    public static final String CONFIG_PREFIX = "camel.forage.route.policy";

    /**
     * Configuration module for enabling/disabling the route policy factory.
     *
     * <p>When set to false, no route policies will be created by this factory.
     * Default is true (enabled).
     */
    public static final ConfigModule ENABLED = ConfigModule.of(
            RoutePolicyFactoryConfig.class,
            CONFIG_PREFIX + ".enabled",
            "Enable or disable the route policy factory",
            "Enabled",
            "false",
            "boolean",
            false,
            ConfigTag.COMMON);

    private static final Map<ConfigModule, ConfigEntry> CONFIG_MODULES = new ConcurrentHashMap<>();

    static {
        init();
    }

    static void init() {
        CONFIG_MODULES.put(ENABLED, ConfigEntry.fromModule());
    }

    /**
     * Returns all registered configuration entries.
     *
     * @return an unmodifiable map of configuration entries
     */
    public static Map<ConfigModule, ConfigEntry> entries() {
        return Collections.unmodifiableMap(CONFIG_MODULES);
    }

    /**
     * Finds a configuration module by prefix and name.
     *
     * @param prefix optional prefix for named configurations
     * @param name the configuration name
     * @return an Optional containing the ConfigModule, or empty if not found
     */
    public static Optional<ConfigModule> find(String prefix, String name) {
        return find(CONFIG_MODULES, prefix, name);
    }

    /**
     * Registers configuration modules for a specific prefix.
     *
     * @param prefix the prefix to register
     */
    public static void register(String prefix) {
        if (prefix != null) {
            for (Map.Entry<ConfigModule, ConfigEntry> entry : entries().entrySet()) {
                ConfigModule configModule = entry.getKey().asNamed(prefix);
                CONFIG_MODULES.put(configModule, ConfigEntry.fromModule());
            }
        }
    }

    /**
     * Loads override configurations from system properties and environment variables.
     *
     * @param prefix optional prefix to use
     */
    public static void loadOverrides(String prefix) {
        load(CONFIG_MODULES, prefix);
    }

    /**
     * Creates a ConfigModule for the policy name of a specific route.
     *
     * @param routeId the route ID
     * @return a ConfigModule for the policy name configuration
     */
    public static ConfigModule policyNameModule(String routeId) {
        return ConfigModule.of(
                RoutePolicyFactoryConfig.class,
                CONFIG_PREFIX + "." + routeId + ".name",
                "Comma-separated list of policy names for route " + routeId,
                "Policy Names",
                null,
                "string",
                false,
                ConfigTag.COMMON);
    }
}
