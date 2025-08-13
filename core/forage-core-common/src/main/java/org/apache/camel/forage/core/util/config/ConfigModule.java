package org.apache.camel.forage.core.util.config;

/**
 * Represents a configuration module that associates a configuration class with a specific configuration parameter.
 *
 * <p>A ConfigModule serves as a unique identifier for a specific configuration parameter within a module.
 * It combines the configuration class type with a parameter name to create a distinct key that can be
 * used by the {@link ConfigStore} to store and retrieve configuration values.
 *
 * <p>This record is typically used as a static constant within configuration classes to define
 * individual configuration parameters. Each ConfigModule represents one configurable aspect of a module.
 *
 * <p><strong>Usage Example:</strong>
 * <pre>{@code
 * public class DatabaseConfig implements Config {
 *     private static final ConfigModule CONNECTION_URL = ConfigModule.of(DatabaseConfig.class, "connection-url");
 *     private static final ConfigModule MAX_POOL_SIZE = ConfigModule.of(DatabaseConfig.class, "max-pool-size");
 *
 *     public DatabaseConfig() {
 *         ConfigStore.getInstance().add(CONNECTION_URL, ConfigEntry.fromEnv("DB_CONNECTION_URL"));
 *         ConfigStore.getInstance().add(MAX_POOL_SIZE, ConfigEntry.fromEnv("DB_MAX_POOL_SIZE"));
 *     }
 *
 *     public String connectionUrl() {
 *         return ConfigStore.getInstance().get(CONNECTION_URL)
 *                 .orElseThrow(() -> new MissingConfigException("Missing database connection URL"));
 *     }
 * }
 * }</pre>
 *
 * <p>The combination of configuration class and parameter name ensures that configuration parameters
 * are unique across the entire application, even if different modules use similar parameter names.
 *
 * <p>This record is immutable and thread-safe.
 *
 * @param config the configuration class that owns this parameter, must not be null
 * @param name the name of the configuration parameter within the module, must not be null
 * @see ConfigStore
 * @see Config
 * @see ConfigEntry
 * @since 1.0
 */
public record ConfigModule(Class<? extends Config> config, String name) {

    /**
     * Creates a new ConfigModule with the specified configuration class and parameter name.
     *
     * <p>This factory method provides a convenient way to create ConfigModule instances,
     * making the code more readable when defining configuration parameters as static constants.
     *
     * @param config the configuration class that owns this parameter, must not be null
     * @param name the name of the configuration parameter within the module, must not be null
     * @return a new ConfigModule instance
     * @throws NullPointerException if config or name is null
     */
    public static ConfigModule of(Class<? extends Config> config, String name) {
        return new ConfigModule(config, name);
    }
}
