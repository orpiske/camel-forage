package io.kaoto.forage.policy.flip;

import io.kaoto.forage.core.util.config.ConfigEntries;
import io.kaoto.forage.core.util.config.ConfigEntry;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigStore;
import io.kaoto.forage.core.util.config.ConfigTag;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Configuration entries for the flip route policy.
 *
 * <p>Configuration follows the pattern:
 * {@code camel.forage.route.policy.<routeId>.flip.<option>}
 *
 * @since 1.0
 */
public final class FlipRoutePolicyConfigEntries extends ConfigEntries {

    /**
     * The base configuration prefix for route policy configuration.
     */
    public static final String CONFIG_PREFIX = "camel.forage.route.policy";

    private static final String ROUTE_ID_PLACEHOLDER = "<routeId>";

    private static final Map<ConfigModule, ConfigEntry> CONFIG_MODULES = new ConcurrentHashMap<>();

    private FlipRoutePolicyConfigEntries() {}

    /**
     * Creates a ConfigModule for the paired-route configuration with the given routeId.
     *
     * @param routeId the route ID
     * @return a ConfigModule for the paired-route configuration
     */
    public static ConfigModule pairedRoute(String routeId) {
        String name = CONFIG_PREFIX + "." + routeId + ".flip.paired-route";
        return ConfigModule.of(
                FlipRoutePolicyConfig.class,
                name,
                "ID of the paired route to flip with",
                "Paired Route",
                null,
                "string",
                true,
                ConfigTag.COMMON);
    }

    /**
     * Creates a ConfigModule for the initially-active configuration with the given routeId.
     *
     * @param routeId the route ID
     * @return a ConfigModule for the initially-active configuration
     */
    public static ConfigModule initiallyActive(String routeId) {
        String name = CONFIG_PREFIX + "." + routeId + ".flip.initially-active";
        return ConfigModule.of(
                FlipRoutePolicyConfig.class,
                name,
                "Whether this route should be active initially",
                "Initially Active",
                "true",
                "boolean",
                false,
                ConfigTag.COMMON);
    }

    public static Map<ConfigModule, ConfigEntry> entries() {
        return Collections.unmodifiableMap(CONFIG_MODULES);
    }

    public static Optional<ConfigModule> find(String routeId, String name) {
        if (routeId == null) {
            return Optional.empty();
        }
        String fullName = CONFIG_PREFIX + "." + routeId + ".flip." + extractPropertyName(name);
        return CONFIG_MODULES.keySet().stream()
                .filter(m -> m.name().equals(fullName))
                .findFirst();
    }

    private static String extractPropertyName(String name) {
        // Extract the property name from various input formats
        if (name.contains(".flip.")) {
            return name.substring(name.lastIndexOf(".flip.") + 6);
        }
        return name;
    }

    public static void register(String routeId) {
        if (routeId != null) {
            CONFIG_MODULES.put(pairedRoute(routeId), ConfigEntry.fromModule());
            CONFIG_MODULES.put(initiallyActive(routeId), ConfigEntry.fromModule());
        }
    }

    public static void loadOverrides(String routeId) {
        if (routeId != null) {
            loadForModule(pairedRoute(routeId));
            loadForModule(initiallyActive(routeId));
        }
    }

    private static void loadForModule(ConfigModule module) {
        // Check environment variable
        String envName = module.envName();
        if (envName != null) {
            String envValue = System.getenv(envName);
            if (envValue != null) {
                ConfigStore.getInstance().set(module, envValue);
                return;
            }
        }

        // Check system property
        String propName = module.propertyName();
        if (propName != null) {
            String propValue = System.getProperty(propName);
            if (propValue != null) {
                ConfigStore.getInstance().set(module, propValue);
            }
        }
    }
}
